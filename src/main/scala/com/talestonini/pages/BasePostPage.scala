package com.talestonini.pages

import cats.effect.unsafe.implicits.global
import com.raquo.laminar.api.L.{*, given}
import com.talestonini.components.{CommentList, InputComment}
import com.talestonini.db.CloudFirestore
import com.talestonini.db.model.*
import com.talestonini.Main.isLoading
import com.talestonini.utils.*
import scala.concurrent.Promise
import scala.scalajs.concurrent.JSExecutionContext.queue
import scala.util.{Failure, Success}
import scala.xml.Elem

trait BasePostPage {

  private val lin = "https://www.linkedin.com/sharing/share-offsite/?mini=true&url="
  private val tt  = "https%3A%2F%2Ftalestonini.com%2F%23%2F"

  // --- state ---------------------------------------------------------------------------------------------------------

  // the post document backing this post page
  private val postDoc: Var[Doc[Post]] = Var(Doc("", Post(None, None, None, None), "", ""))

  // the comments on this page
  private val comments: Var[Docs[Comment]] = Var(Seq.empty)

  // --- UI ------------------------------------------------------------------------------------------------------------

  // where each post builds its content, converted from MarkDown to HTML by Laika
  def postContent(): Elem

  def apply(): Element = {
    val content = div()
    content.ref.innerHTML = postContent().toString

    div(
      div(
        className := "post-date w3-padding-16 w3-display-container",
        div(
          className := "w3-display-left",
          child <-- postDoc.signal.map(pd => renderPostDate(pd.fields))
        ),
        div(
          className := "share-post w3-display-right",
          child <-- postDoc.signal.map(pd => div(shareOnLinkedIn(pd), copyLink(pd)))
        )
      ),
      div(
        className := "post-title w3-padding-8",
        child <-- postDoc.signal.map(pd => pd.fields.title.getOrElse(""))
      ),
      div(
        className := "w3-padding-16 line-numbers",
        content
      ),
      hr(),
      div(
        className := "post-comments",
        div(
          className := "header w3-bold",
          "Comments (",
          child <-- comments.signal.map(cs => cs.length.toString),
          ")"
        ),
        InputComment(persistComment),
        children <-- comments.signal.map(cs => CommentList(cs))
      )
    )
  }

  private def shareOnLinkedIn(pd: Doc[Post]) =
    renderShareAnchor(
      "fa-linkedin",
      lin + tt + pd.fields.resource.getOrElse(""),
      "Share on LinkedIn"
    )

  private def copyLink(pd: Doc[Post]) =
    renderShareAnchor(
      "fa-link",
      s"javascript:copyToClipboard('${tt + pd.fields.resource.getOrElse("")}')",
      "Copy link",
      "_self"
    )

  private def renderShareAnchor(anchorIcon: String, hRef: String, tooltipText: String,
    anchorTarget: String = "_blank"): Element =
    a(
      href      := hRef,
      className := "w3-tooltip no-decoration share-icon",
      target    := anchorTarget,
      i(className    := s"fa $anchorIcon w3-hover-opacity"),
      span(className := "tooltip w3-text w3-tag w3-small", tooltipText)
    )

  private def renderPostDate(p: Post): Element = {
    val firstPublishDate = p.firstPublishDate.map(fpd => datetime2Str(fpd, SimpleDateFormatter)).getOrElse("")
    val publishDate      = p.publishDate.map(pd => datetime2Str(pd, SimpleDateFormatter)).getOrElse("")

    if (firstPublishDate == publishDate)
      i(publishDate)
    else
      div(
        i(publishDate),
        i(className := "first-published", s"(first $firstPublishDate)")
      )
  }

  // --- public --------------------------------------------------------------------------------------------------------

  // retrieve the comments from db
  // when the promise for the post document which this page's comments belong to fulfills
  val postDocPromise = Promise[Doc[Post]]() // promise for the post document backing this page
  postDocPromise.future
    .onComplete({
      case s: Success[Doc[Post]] =>
        postDoc.update(_ => s.get)
        val retrievingComments = s"retrievingComments_${postDoc.signal.map(postDoc => postDoc.fields.resource)}"
        displayLoading(isLoading, retrievingComments)
        CloudFirestore
          .getComments(s.get.name)
          .unsafeToFuture()
          .onComplete({
            case s: Success[Docs[Comment]] =>
              s.get.foreach(commentDoc => comments.update(list => list :+ commentDoc))
              hideLoading(isLoading, retrievingComments)
            case f: Failure[Docs[Comment]] =>
              println(s"failed getting comments: ${f.exception.getMessage()}")
              hideLoading(isLoading, retrievingComments)
          })(queue)
      case f: Failure[Doc[Post]] =>
        println(s"failed getting post document name: ${f.exception.getMessage()}")
    })(queue)

  // --- private -------------------------------------------------------------------------------------------------------

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
      .createComment(postDoc.signal.now().name, c)
      .unsafeToFuture()
      .onComplete({
        case s: Success[Doc[Comment]] =>
          comments.update(cs => s.get +: cs)
        case f: Failure[Doc[Comment]] =>
          println("failed creating comment")
      })(queue)
  }

}
