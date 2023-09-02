package com.talestonini.pages

import com.raquo.laminar.api.L.{*, given}

object Posts {

  // def apply(): Element =
  //   div(className("post-list"), buildPostLinks())
  //
  // case class PostLink(
  //   title: String, firstPublishDate: String, publishDate: String, resource: String
  // )
  //
  // val postLinks: Var[List[PostLink]] = Var(List.empty)
  //
  // private def buildPostLinks(): Element =
  //   postLinks.signal.map(pls => for (pl <- pls)
  //     yield div(
  //             p(
  //               div( className("post-date"),
  //                 a(href(s"#/${pl.resource}"), i(pl.publishDate), firstPublishDate(pl))
  //               ),
  //               a(className("w3-bold"), href(s"#/${pl.resource}"), pl.title)
  //             )
  //             )
  // )
  //
  // private def firstPublishDate(p: PostLink): Element =
  //   if (p.firstPublishDate == p.publishDate)
  //     span()
  //   else
  //     i(className("first-published"), "first ", p.firstPublishDate)

}
