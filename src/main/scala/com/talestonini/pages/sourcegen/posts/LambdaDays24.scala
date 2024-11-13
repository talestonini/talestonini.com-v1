/***************************************************************************************************
 *
 * THIS CODE IS GENERATED AT COMPILE TIME BY LAIKA SBT PLUGIN.
 *
 * Do not modify it directly, as compilation will ovewrite your modifications.
 *
 **************************************************************************************************/
package com.talestonini.pages.sourcegen.posts

import com.talestonini.CodeSnippets
import com.talestonini.pages.BasePost
import scala.xml.Elem

object LambdaDays24 extends BasePost {

  def postContent(): Elem =
    <div class="markdown-post-body">
      <p>This year I was very fortunate to be sponsored by <strong>Mantel Group</strong> to attend the Lambda Days conference in Krakow,
      Poland.  Lambda Days is a conference that gravitates around all things <strong>Functional Programming</strong> &mdash; the
      declarative paradigm that allows for <em>programs that are easier to reason about</em> &mdash;, bringing together academics,
      professionals, industry specialists and enthusiasts of FP to share their knowledge and experiences.</p>
      <div style="display:flex">
        <div style="flex:1">
          <img src="/img/lambda-days-24-welcome.jpg" alt="Welcome to Lambda Days 2024"/>
        </div>
        <div style="flex:1">
          <img src="/img/talk-y-combinator.jpg" alt="Talk on the Y combinator"/>
        </div>
      </div>
      <p>Besides my curiosity and anticipation for the 2-day schedule, my biggest expectation coming to the conference was being
      able to network with other FP programmers and build an understanding of the scale of FP in the European market.  Working
      in Australia, I feel the isolation and sometimes a delay in the adoption of FP concepts in software engineering, despite
      the tremendous effort and quality gains they can bring.  Simple things like <em>immutability</em>, <em>referential transparency</em>,
      <em>pure functions</em> and <em>stateless code</em> lead to more <em>correct and maintainable software</em>.  In the end, I was somewhat
      relieved to see that FP is growing in adoption but still a niche in Europe too, contraty to my impressions from all the
      repositories and online content authored in countries such as the Netherlands, Germany and Poland.</p>
      <p>Because the conference is about a programming paradigm and does not focus on a specific technology, talk topics ranged
      vastly, which was an absolute delight for me.  There were talks on <strong>Property-Based Testing</strong> (PBT), embedding a Domain
      Specific Language (DSL) in a host functional language using the <strong>Tagless Final</strong> style, a deep dive into <strong>Algebraic
      Data Types</strong> (ADTs), using the <strong>Y Combinator</strong> for recursion in any language, <strong>Information Flow Control</strong> (IFC), AI,
      discussions on <em>how programmers think about code</em> and <em>what language features and testing techniques favour people with
      this or that neuro-diverse trait</em>, among many others.  I also heard about FP languages that are quite popular in the
      community, like <strong>Elm</strong> and <strong>Elixir</strong>, which I had never heard of before.</p>
      <div class="aside">
        <img src="/img/last-10-years-of-fp.jpg" alt="Last 10 years of Functional Programming"/>
      </div>
      <p>Below are the recordings of my 2 favourite talks in the conference, which were the opening and closing keynotes on the
      second day.  The first is about recent research on Algebraic Effect Handlers by <strong>Research Scientist Ningning Xie</strong>, who
      starts by explaining the concepts of <strong>Algebraic Effects</strong> and <strong>Effect Handlers</strong> with interesting examples, and then
      goes on to propose a way of parallelizing computations with multiple handlers, offering a sample implementation in
      Haskell.</p>
      <div class="aside">
        <div class="iframe-container">
          <iframe src="https://www.youtube.com/embed/XCVg_cc9Jo4?si=IIUFLuBJGnWV5QlT" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" referrerpolicy="strict-origin-when-cross-origin"></iframe>
        </div>
        <figcaption>Algebraic Effect Handlers with Parallelizable Computations by Ningning Xie</figcaption>
      </div>
      <p>And the second is about the huge efforts of <strong>Planetary Computing Professor Anil Madhavapeddy</strong> to create a platform for
      crunching volumes of satellite data, so to inform policy makers about deforestation in order to conserve flora and fauna
      species.  It was eye-opening to me realising that policy makers need <em>not only explainable data but also data that can
      be comparable</em> over the many decades of their collection.</p>
      <div class="aside">
        <div class="iframe-container">
          <iframe src="https://www.youtube.com/embed/oIvDTM-iF1Y?si=ilIsTPRQAZ6UYfJV" title="Programming for the planet by Anil Madhavapeddy" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" referrerpolicy="strict-origin-when-cross-origin"></iframe>
        </div>
        <figcaption>Programming for the planet by Anil Madhavapeddy</figcaption>
      </div>
      <p>I left the conference inspired to continue to learn FP, despite its generally niche status in the industry and usual
      steep learning curve.  Functional techniques can be adopted slowly and incrementally in any software project and with
      most general-purpose programming languages really (of course, the more functional a language is, the less
      self-disciplined the programmer needs to be).  After all, when it comes to solving problems, plural, complimentary
      approaches work best.</p>
      <p>Tchau tchau, beautiful Krakow!  See you next time!</p>
      <div class="aside">
        <img src="/img/vistula-river.jpg" alt="Vistula River"/>
        <figcaption>Vistula River</figcaption>
      </div>
    </div>

}
