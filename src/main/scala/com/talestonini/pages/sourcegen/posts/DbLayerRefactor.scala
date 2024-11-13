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

object DbLayerRefactor extends BasePost {

  def postContent(): Elem =
    <div class="markdown-post-body">
      <div class="aside">
        <p><img src="/img/refactoring.png"/></p>
      </div>
      <p>A few weeks ago I took on the task of updating the database access layer of this website. Some parts of this page are
      stored in a Cloud Firestore database, like <em>comments</em> and <em>likes</em> (when I actually implement <em>likes</em>), and are retrieved
      via Cloud Firestore&#39;s REST API straight from the browser (there is not a <em>backend for frontend</em> running at the server
      side). The motivation for the update? Despite so thin and intuitive, library <a href="https://github.com/hmil/RosHTTP">RösHTTP</a>
      is not being maintained anymore, and that would hold me back when I am finally able to upgrade the code from Scala 2.13
      to Scala 3. At the moment, even <a href="https://github.com/ThoughtWorksInc/Binding.scala">ThoughtWorks Binding</a> - the
      data-binding library at the core of this website - is not yet ready for Scala 3, but that&#39;s another story.</p>
      <p>Back to the database layer update, my first choice as a substitute to RösHTTP was
      <a href="https://sttp.softwaremill.com/en/v2/">sttp</a>. Because everything runs on the browser, I needed an HTTP library that
      provides a JavaScript backend, which sttp does via the
      <a href="https://sttp.softwaremill.com/en/v2/backends/javascript/fetch.html">Fetch API</a>. It was all well and good until I hit a
      wall: this backend implementation would require me to change server side Cloud Firestore&#39;s REST API to overcome a
      <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS">CORS</a> issue, which is not an option.</p>
      <p>Back to the drawing board, I needed another HTTP library with a JavaScript backend. So I decided to try
      <a href="https://http4s.org/">http4s</a>, after all it also supports lots of backends and favours the <em>pure functional</em> side of
      Scala through the use of <a href="https://typelevel.org/cats-effect/">Cats Effect</a>. It turned out to be an interesting
      learning opportunity on Scala.js as a whole. Bear with me.</p>
      <p>On choosing an http4s backend, I started off by searching for the ones with a corresponding Scala.js offering. <em>Ember</em>
      looked good at first: it offers binaries for Scala.js
      <a href="https://http4s.org/v1/docs/client.html#creating-the-client">2.13 and 3</a> and has an
      example on <a href="https://http4s.org/v1/docs/client.html#creating-the-client">creating the client</a> in the http4s
      documentation. It turns out that <strong>just because a library transpiles to JavaScript, it does not mean that it can run on
      the browser</strong>. That is the case with ember, as it relies on plain TCP sockets, which are not available in the browser
      (check <a href="https://stackoverflow.com/questions/40599069/node-js-net-socket-is-not-a-constructor">this issue</a> on stack
      overflow). And so I learned that ember clients are fine for Node.js, but not for the browser.</p>
      <p>Ok, I still needed a suitable http4s backend for my refactor. At the corner of http4s documentation page there are some
      <em>related projects</em>. One of them - <a href="https://http4s.github.io/http4s-dom/">http4s-dom</a> - looked very promising...</p>
      <div class="aside">
        <img src="/img/http4s-dom.png"/>
        <figcaption>Fig.1 - http4s-dom documentation snippet</figcaption>
      </div>
      <p>...until I read the dreadind words &quot;backed by fetch&quot;... No, <em>fetch</em> again! What if I ran into the CORS issue once more?
      I had to try it. I already had all TDD tests waiting for the code.</p>
      <p>Changing from ember to the <em>fetch</em> client was super simple. Apart from building the client itself, which ember &quot;wraps&quot;
      in a <a href="https://typelevel.org/cats-effect/docs/std/resource">Cats Effect Resource</a> typeclass, the REST API calls used the
      very same interfaces, as expected from a very well designed library such as http4s. (You can check the final code
      <a href="https://github.com/talestonini/talestonini.com/blob/master/src/main/scala/com/talestonini/db/CloudFirestore.scala">here</a>.)
      But... as soon as I tried to compile without dependency <em>http4s-ember-client</em> and with new dependency <em>http4s-dom</em>, I
      got an annoying <em>binary incompatibility</em>:</p>
      <div class="aside">
        <img src="/img/binary-incompatibility.png"/>
        <figcaption>Fig.2 - Binary incompatibility across versions of dependency scalajs-dom</figcaption>
      </div>
      <p><strong>A <em>binary incompatibility</em> happens when your code depends on different breaking versions of a library.</strong> My website
      already had the following dependencies:</p>
      <ul>
        <li><em>org.scala-js : scalajs-dom</em> version <em>1.1.0</em></li>
        <li><em>com.thoughtworks.binding : route</em>, which in turn depends on <em>scalajs-dom</em> version <em>1.0.0</em></li>
        <li><em>org.lrng.binding : html</em>, which in turn depends on <em>scalajs-dom</em> version <em>0.9.8</em></li>
      </ul>
      <p>Versions <em>1.1.0</em>, <em>1.0.0</em> and <em>0.9.8</em> of <em>scalajs-dom</em> are all compatible among themselves (i.e. non-breaking), so the
      compiler/linker never had an issue. However, when I added dependency:</p>
      <ul>
        <li><em>org.http4s : http4s-dom</em>, which in turn depends on <em>scalajs-dom</em> version <em>2.1.0</em></li>
      </ul>
      <p>a <em>breaking</em> change between <em>scalajs-dom</em> version <em>2.1.0</em> and its previous versions was introduced. Panicking, I tried
      updating the first dependency (<em>scalajs-dom</em>) to version <em>2.1.0</em>, hoping the error would magically disappear somehow,
      but that still revealed the binary incompatibility. The 3 original dependencies listed above are non-negotiable - they
      are the bricks and mortar of the website build. This time, after all the journey I had already been to choose an HTTP
      client library with a JavaScript backend to replace RösHTTP, I was not in the mood to search for another HTTP library.</p>
      <p>In the end, &quot;brute force&quot; was needed, and I thank once again open source projects. I&#39;ll tell you why. Having:</p>
      <ul>
        <li>no version alternative to remove the binary incompatibility;</li>
        <li>no desire to rebuild the whole website without ThoughtWorks Binding, and;</li>
        <li>no desire to go search for yet another HTTP library with JavaScript backend,</li>
      </ul>
      <p>I resorted to <strong>forking</strong> <em>scalajs-dom</em> from version <em>1.1.0</em> including all the missing components from version <em>2.1.0</em>
      needed by <em>http4s-dom</em>. That is essentially a <strong>merge</strong> between versions <em>1.1.0</em> and parts of <em>2.1.0</em>, which constitute
      my new website dependency:</p>
      <ul>
        <li><em>org.scala-js : scalajs-dom</em> version <em>1.1.0+179-fa23209f-SNAPSHOT</em></li>
      </ul>
      <p>All the code compiles fine now. For this to finally work, I also excluded transitive dependency on <em>scalajs-dom</em> from
      all involved libraries <em>route</em>, <em>html</em> and <em>http4s-dom</em>.</p>
      <p>You can check out the complete list of dependencies in
      <a href="https://github.com/talestonini/talestonini.com/blob/master/build.sbt">build.sbt</a>, my fork of
      <a href="https://github.com/talestonini/scala-js-dom">scalajs-dom</a>, and some more info on
      <a href="https://www.scala-lang.org/blog/2021/02/16/preventing-version-conflicts-with-versionscheme.html">preventing version conflicts with versionscheme</a>
      (however, this is more for Scala library writers than for library users, like me in this case).</p>
    </div>

}
