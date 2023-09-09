package com.talestonini.pages

import cats.effect.unsafe.implicits.global
import com.raquo.laminar.api.L.{*, given}
import com.talestonini.components.{InputComment, Spinner}
import com.talestonini.db.CloudFirestore
import com.talestonini.db.model.*
import com.talestonini.utils.*
import scala.concurrent.Promise
import scala.scalajs.concurrent.JSExecutionContext.queue
import scala.util.{Failure, Success}
import scala.xml.Elem

trait BasePost {

  private val lin = "https://www.linkedin.com/sharing/share-offsite/?mini=true&url="
  private val tt  = "https%3A%2F%2Ftalestonini.com%2F"

  // --- state ---------------------------------------------------------------------------------------------------------

  // promise for the post document backing this page
  // (public, because the App fulfills it when it retrieves posts data from the database)
  val postDocPromise = Promise[Doc[Post]]()
  setupPostDocPromise() // must be setup from object creation, so that comments are retrieved asap from database

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
          child <-- postDoc.signal.map(pd => postDate(pd.fields))
        ),
        div(
          className := "share-post w3-display-right",
          child <-- postDoc.signal.map(pd => div(linkedInShareAnchor(pd), copyLinkShareAnchor(pd)))
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
        InputComment(persistCommentIntoDb),
        children <-- comments.signal.map(cs => commentList(cs))
      )
    )
  }

  private def shareAnchor(anchorIcon: String, hRef: String, tooltipText: String,
    anchorTarget: String = "_blank"): Element =
    a(
      href      := hRef,
      className := "w3-tooltip no-decoration share-icon",
      target    := anchorTarget,
      i(className    := s"fa $anchorIcon w3-hover-opacity"),
      span(className := "tooltip w3-text w3-tag w3-small", tooltipText)
    )

  private def linkedInShareAnchor(pd: Doc[Post]) =
    shareAnchor(
      "fa-linkedin",
      lin + tt + pd.fields.resource.getOrElse(""),
      "Share on LinkedIn"
    )

  private def copyLinkShareAnchor(pd: Doc[Post]) =
    shareAnchor(
      "fa-link",
      s"javascript:copyToClipboard('${tt + pd.fields.resource.getOrElse("")}')",
      "Copy link",
      "_self"
    )

  private def postDate(p: Post): Element = {
    val firstPublishDate = p.firstPublishDate.map(fpd => datetime2Str(fpd, SimpleDateFormatter)).getOrElse("")
    val publishDate      = p.publishDate.map(pd => datetime2Str(pd, SimpleDateFormatter)).getOrElse("")

    if (firstPublishDate == publishDate)
      i(publishDate)
    else
      div(
        i(publishDate),
        i(
          className := "first-published",
          s" (first $firstPublishDate)"
        )
      )
  }

  private def commentList(comments: Docs[Comment]): Seq[Element] =
    for (c <- comments)
      yield aComment(c.fields)

  private def aComment(c: Comment): Element =
    div(
      className := "w3-panel w3-light-grey w3-leftbar",
      p(i(c.text)),
      p(
        c.author.get.name.get,
        span(styleAttr := "padding: 0 15px 0 15px", "|"),
        i(datetime2Str(c.date))
      )
    )

  // --- private -------------------------------------------------------------------------------------------------------

  // retrieves comments from the database
  // when the promise for the post document which this page's comments belong to fulfills
  private def setupPostDocPromise(): Unit =
    postDocPromise.future
      .onComplete({
        case s: Success[Doc[Post]] =>
          postDoc.update(_ => s.get)
          val retrievingComments = s"retrievingComments_${postDoc.signal.map(postDoc => postDoc.fields.resource)}"
          Spinner.start(retrievingComments)
          CloudFirestore
            .getComments(s.get.name)
            .unsafeToFuture()
            .onComplete({
              case s: Success[Docs[Comment]] =>
                s.get.foreach(commentDoc => comments.update(list => list :+ commentDoc))
                Spinner.stop(retrievingComments)
              case f: Failure[Docs[Comment]] =>
                println(s"failed getting comments: ${f.exception.getMessage()}")
                Spinner.stop(retrievingComments)
            })(queue)
        case f: Failure[Doc[Post]] =>
          println(s"failed getting post document name: ${f.exception.getMessage()}")
      })(queue)

  private def persistCommentIntoDb(name: String, comment: String): Unit = {
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
