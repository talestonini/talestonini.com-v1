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

object UrbanForestChallenge extends BasePost {

  def postContent(): Elem =
    <div class="markdown-post-body">
      <p>When I started at <a href="https://eliiza.com.au">Eliiza</a> in September 2018, I was the first <em>data engineer</em> of the team,
      among a few machine learning engineers, data scientists, our CTO and CEO.  We were only 7 people in total then and
      wanted to grow.</p>
      <p>This is the first code challenge that I developed to recruit other data engineers like myself, i.e. <em>software engineers</em>
      that appreciate solving Big Data problems, distributed systems and parallel programming.  As such, it requires the use
      of <a href="https://spark.apache.org">Apache Spark</a> to determine the <strong>greenest suburb of Melbourne</strong>.</p>
      <p>Check it out <a href="https://github.com/eliiza/challenge-urban-forest">here</a>!  Would you give it a crack?</p>
    </div>

}
