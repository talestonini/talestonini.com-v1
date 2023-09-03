package com.talestonini.pages

import cats.effect.unsafe.implicits.global
import com.raquo.laminar.api.L.{*, given}
import com.talestonini.db.CloudFirestore
import com.talestonini.db.model.*
import com.talestonini.Main.isLoadingVar
import com.talestonini.utils.*
import com.talestonini.utils.javascript.jsDisplay
import org.scalajs.dom
import scala.concurrent.Promise
import scala.scalajs.concurrent.JSExecutionContext.queue
import scala.util.{Failure, Success}
import scala.xml.Elem

trait BasePostPage {

  // --- UI ------------------------------------------------------------------------------------------------------------

  // where each post builds its content, converted from MarkDown to HTML by Laika
  def postContent(): Elem

  private val lin = "https://www.linkedin.com/sharing/share-offsite/?mini=true&url="
  private val tt  = "https%3A%2F%2Ftalestonini.com%2F%23%2F"

  def apply(): Element = {
    val content = div()
    content.ref.innerHTML = postContent().toString

    def shareOnLinkedIn(pd: Doc[Post]) =
      renderShareAnchor("fa-linkedin", lin + tt + pd.fields.resource.getOrElse(""), "Share on LinkedIn")
    def copyLink(pd: Doc[Post]) = renderShareAnchor("fa-link",
      s"javascript:copyToClipboard('${tt + pd.fields.resource.getOrElse("")}')", "Copy link", "_self")

    div(
      div(className("post-date w3-padding-16 w3-display-container"),
        div(className("w3-display-left"), child <-- postDoc.map(pd => renderPostDate(pd.fields))),
        div(className("share-post w3-display-right"),
          child <-- postDoc.map(pd => div(shareOnLinkedIn(pd), copyLink(pd))))),
      div(className("post-title w3-padding-8"), child <-- postDoc.map(pd => pd.fields.title.getOrElse(""))),
      div(className("w3-padding-16 line-numbers"), content),
      hr(),
      div(className("post-comments"),
        div(className("header w3-bold"), "Comments (", child <-- comments.map(cs => cs.length.toString), ")"),
        commentInput(), children <-- renderComments())
    )
  }

  private def renderShareAnchor(anchorIcon: String, hRef: String, tooltipText: String,
    anchorTarget: String = "_blank"): Element =
    a(href(hRef), className("w3-tooltip no-decoration share-icon"), target(anchorTarget),
      i(className(s"fa $anchorIcon w3-hover-opacity")), span(className("tooltip w3-text w3-tag w3-small"), tooltipText))

  private def renderPostDate(p: Post): Element = {
    val firstPublishDate = p.firstPublishDate.map(fpd => datetime2Str(fpd, SimpleDateFormatter)).getOrElse("")
    val publishDate      = p.publishDate.map(pd => datetime2Str(pd, SimpleDateFormatter)).getOrElse("")

    if (firstPublishDate == publishDate)
      i(publishDate)
    else
      div(i(publishDate), i(className("first-published"), s"(first $firstPublishDate)"))
  }

  // widget for inputting a new commment
  private val isInputtingCommentVar = Var(false)
  private val isInputtingComment    = isInputtingCommentVar.signal
  private def commentInput(): Element = {
    val initComment = "What do you think?"
    val commentVar  = Var(initComment)
    val comment     = commentVar.signal
    val rowCount    = Var(1)
    val initName    = "Name"
    val nameVar     = Var(initName)
    val name        = nameVar.signal

    def commentFocusHandler() = {
      commentVar.update(_ => "")
      isInputtingCommentVar.update(_ => true)
    }

    def nameFocusHandler() = {
      nameVar.update(_ => "")
    }

    val textAreaControl: Element =
      textArea(className("w3-input w3-border"), placeholder(initComment), rows <-- rowCount, value <-- comment,
        onClick --> (_ => rowCount.update(_ => 5)), onFocus --> (_ => commentFocusHandler()),
        onInput.mapToValue --> (newComment => commentVar.update(_ => newComment)))

    val inputControl: Element =
      input(className("w3-input w3-border"), typ("text"), placeholder(initName), value <-- name,
        onFocus --> (_ => nameFocusHandler()), onInput.mapToValue --> (newName => nameVar.update(_ => newName)))

    def clearCommentInputs() = {
      commentVar.update(_ => initComment)
      nameVar.update(_ => initName)
      isInputtingCommentVar.update(_ => false)
      rowCount.update(_ => 1)
    }

    def commentButtonHandler() = {
      if (comment.now().nonEmpty) {
        persistComment(name.now(), comment.now())
        clearCommentInputs()
      }
    }

    val buttonClasses = "w3-button w3-ripple w3-padding w3-black"
    val commentInputControls: Element =
      div(className("w3-panel w3-light-grey w3-leftbar w3-padding-16"), textAreaControl,
        div(className("w3-padding-8"), styleAttr <-- isInputtingComment.map(b => s"display: ${jsDisplay(b)}"),
          inputControl),
        div(className("w3-right"), styleAttr <-- isInputtingComment.map(b => s"display: ${jsDisplay(b)}"),
          div(className("w3-bar"),
            button(typ("button"), className(buttonClasses), onClick --> (_ => commentButtonHandler()), "Comment"),
            button(typ("button"), className(buttonClasses), onClick --> (_ => clearCommentInputs()), "Cancel"))))

    commentInputControls
  }

  private def renderComments(): Signal[Seq[Element]] =
    comments.map(cs => for (c <- cs) yield renderAComment(c.fields))

  private def renderAComment(c: Comment): Element =
    div(className("w3-panel w3-light-grey w3-leftbar"), p(i(c.text)),
      p(c.author.get.name.get, span(styleAttr("padding: 0 15px 0 15px"), "|"), i(datetime2Str(c.date))))

  // --- public --------------------------------------------------------------------------------------------------------

  // retrieve the comments from db
  // when the promise for the post document which this page's comments belong to fulfills
  val postDocPromise = Promise[Doc[Post]]() // promise for the post document backing this page
  postDocPromise.future
    .onComplete({
      case s: Success[Doc[Post]] =>
        postDocVar.update(_ => s.get)
        val retrievingComments = s"retrievingComments_${postDoc.map(postDoc => postDoc.fields.resource)}"
        displayLoading(isLoadingVar, retrievingComments)
        CloudFirestore
          .getComments(s.get.name)
          .unsafeToFuture()
          .onComplete({
            case s: Success[Docs[Comment]] =>
              s.get.foreach(commentDoc => commentsVar.update(list => list :+ commentDoc))
              hideLoading(isLoadingVar, retrievingComments)
            case f: Failure[Docs[Comment]] =>
              println(s"failed getting comments: ${f.exception.getMessage()}")
              hideLoading(isLoadingVar, retrievingComments)
          })(queue)
      case f: Failure[Doc[Post]] =>
        println(s"failed getting post document name: ${f.exception.getMessage()}")
    })(queue)

  // --- private -------------------------------------------------------------------------------------------------------

  // the binding post document backing this post page
  private val postDocVar: Var[Doc[Post]] = Var(Doc("", Post(None, None, None, None), "", ""))
  private val postDoc                    = postDocVar.signal

  // the comments on this page
  private val commentsVar: Var[Docs[Comment]] = Var(Seq.empty)
  private val comments: Signal[Docs[Comment]] = commentsVar.signal

  // persist new comment into db
  private def persistComment(name: String, comment: String): Unit = {
    val dbUser = com.talestonini.db.model.User(
      name = Option(name),
      email = Option("---"), // there is no auth anymore
      uid = Option("---")    // there is no auth anymore
    )
    val c = Comment(
      author = Option(dbUser),
      date = Option(now()),
      text = Option(comment)
    )
    CloudFirestore
      .createComment(postDoc.now().name, c)
      .unsafeToFuture()
      .onComplete({
        case s: Success[Doc[Comment]] =>
          commentsVar.update(cs => s.get +: cs)
        case f: Failure[Doc[Comment]] =>
          println("failed creating comment")
      })(queue)
  }

}
