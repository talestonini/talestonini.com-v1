/** *************************************************************************************************
  *
  * THIS CODE IS GENERATED AT COMPILE TIME BY LAIKA SBT PLUGIN.
  *
  * Do not modify it directly, as compilation will ovewrite your modifications.
  */
package com.talestonini.pages.sourcegen

import com.raquo.laminar.api.L.{*, given}
import com.talestonini.CodeSnippets
import scala.xml._

object About {

  def apply(): Element = {
    val element = div()
    element.ref.innerHTML = elem().toString
    element
  }

  def elem(): Elem =
    <div>
      <h1 id="about-me" class="title">About me</h1>
      <div class="aside">
        <table class="w3-hide-small" style="width:100%">
          <tr>
            <td style="padding-right: 15px; width: 30%;"><img src="/img/talestonini.jpg"/></td>
            <td>
              <p>Hi!, my name is <strong>Tales Tonini</strong>. I&#39;m a software engineer interested in Functional Programming, Distributed Systems and the Scala language.</p>
              <p>For the past 6 years I&#39;ve been working at <a href="https://mantelgroup.com.au/">Mantel Group</a> delivering backend API&#39;s and client applications for Spark and Kafka, platforms that I really enjoy to program in. I&#39;ve also extensive experience programming in Java and other JVM languages like Kotlin and Groovy.</p>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              I live in Melbourne, Australia, but am originally from Brazil. Aside from programming, I like to spend time with family and friends, swimming and the outdoors.
            </td>
          </tr>
        </table>
        <table class="w3-hide-large w3-hide-medium" style="width:100%">
          <tr>
            <td style="padding-right: 10px; width: 30%;"><img src="/img/talestonini.jpg"/></td>
            <td>
              Hi!, my name is <strong>Tales Tonini</strong>. I&#39;m a software engineer interested in Functional Programming, Distributed Systems and the Scala language.
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <p>For the past 6 years I&#39;ve been working at <a href="https://mantelgroup.com.au/">Mantel Group</a> delivering backend API&#39;s and client applications for Spark and Kafka, platforms that I really enjoy to program in. I&#39;ve also extensive experience programming in Java and other JVM languages like Kotlin and Groovy.</p>
              <p>I live in Melbourne, Australia, but am originally from Brazil. Aside from programming, I like to spend time with family and friends, swimming and the outdoors.</p>
            </td>
          </tr>
        </table>
      </div>
      
      <h1 id="about-my-website" class="section">About my website</h1>
      <p>I started this website to share my interests and learnings and as a way to play around with Scala.js. These are some of
      the technologies and libraries that I use to build it:</p>
      <ul>
        <li><a href="https://www.scala-lang.org/">Scala 3</a></li>
        <li><a href="https://www.scala-js.org/">Scala.js</a></li>
        <li><a href="https://laminar.dev/">Laminar</a></li>
        <li><a href="https://vitejs.dev/">Vite</a></li>
        <li><a href="https://typelevel.org/Laika/">Laika</a></li>
        <li><a href="https://firebase.google.com/">Firebase</a></li>
        <li><a href="https://firebase.google.com/docs/firestore">Cloud Firestore</a></li>
        <li><a href="https://www.w3schools.com/w3css/default.asp">W3.CSS</a></li>
        <li><a href="https://prismjs.com/index.html">Prism</a></li>
        <li><a href="https://www.jasondavies.com/wordcloud/">Jason Davies&#39; Word Cloud Generator</a> using <a href="https://d3js.org/">D3</a></li>
      </ul>
      <p>The <strong>source code is open</strong> <a href="https://github.com/talestonini/talestonini.com">in my GitHub account</a> and I would gladly
      receive feedback about it.</p>
      <p>As you can see, I built a little engine to generate Scala.js code for the posts I write in
      <a href="https://en.wikipedia.org/wiki/Markdown">Markdown</a>.</p>
      
      <h1 id="release-notes" class="section">Release notes</h1>
      
      <h3 id="_0-2-x" class="section">0.2.x</h3>
      <ul>
        <li>Replaced <a href="https://github.com/hmil/RosHTTP">RösHTTP</a> for <a href="https://http4s.github.io/http4s-dom/">http4s-dom</a> due to the
        former not being maintained anymore and to give me a reason to play with <a href="https://typelevel.org/cats/">Cats</a>. This is at
        the database layer, implementing API calls to Cloud Firestore.</li>
        <li>Packaging the app with <a href="https://scalacenter.github.io/scalajs-bundler/">scalajs-bundler</a>.</li>
      </ul>
      
      <h3 id="_0-3-x" class="section">0.3.x</h3>
      <ul>
        <li>Added links that allow for sharing a post via LinkedIn and Twitter, and also for copying a post URL to the clipboard.</li>
      </ul>
      
      <h3 id="_0-4-x" class="section">0.4.x</h3>
      <ul>
        <li>Refactored database package to remove usage of <strong>Future</strong> in favour of <strong>Cats IO</strong>.</li>
      </ul>
      
      <h3 id="_0-5-x" class="section">0.5.x</h3>
      <ul>
        <li>Code cleanup.</li>
      </ul>
      
      <h3 id="_1-0-x" class="section">1.0.x</h3>
      <ul>
        <li>Replaced <a href="https://github.com/ThoughtWorksInc/Binding.scala">ThoughtWorks Binging</a> for <a href="https://laminar.dev/">Laminar</a>,
        meaning the whole website UI was rewritten.</li>
        <li>Packaging the app with <a href="http://vitejs.dev/">Vite</a>.</li>
        <li>Added tags to posts and the Tags page.</li>
        <li>Dropped sharing to Twitter in favour of Mastodon.</li>
      </ul>
      
      <h3 id="_1-1-x" class="section">1.1.x</h3>
      <ul>
        <li>Made Tags the home page.</li>
        <li>Configured deployment via GitHub Actions, Dependabot and Scala Steward.</li>
        <li>Bug fixes.</li>
      </ul>
      
      <h3 id="_1-2-x" class="section">1.2.x</h3>
      <ul>
        <li>Added links above the footer to jump back home and to the top of the page.</li>
      </ul>
      
      <h3 id="_1-3-x" class="section">1.3.x</h3>
      <ul>
        <li>Improved toggle mechanism to develop and publish new posts.</li>
        <li>Made most recent post the home page again.</li>
      </ul>
    </div>

}
