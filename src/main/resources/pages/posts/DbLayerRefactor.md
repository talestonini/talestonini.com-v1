{%
  class.name = DbLayerRefactor
%}
<div class="aside">
  <img src="/img/refactoring.png" />
</div>

A few weeks ago I took on the task of updating the database access layer of this website. Some parts of this page are
stored in a Cloud Firestore database, like *comments* and *likes* (when I actually implement *likes*), and are retrieved
via Cloud Firestore's REST API straight from the browser (there is not a *backend for frontend* running at the server
side). The motivation for the update? Despite so thin and intuitive, library [RösHTTP](https://github.com/hmil/RosHTTP)
is not being maintained anymore, and that would hold me back when I am finally able to upgrade the code from Scala 2.13
to Scala 3. At the moment, even [ThoughtWorks Binding](https://github.com/ThoughtWorksInc/Binding.scala) - the
data-binding library at the core of this website - is not yet ready for Scala 3, but that's another story.

Back to the database layer update, my first choice as a substitute to RösHTTP was
[sttp](https://sttp.softwaremill.com/en/v2/). Because everything runs on the browser, I needed an HTTP library that
provides a JavaScript backend, which sttp does via the
[Fetch API](https://sttp.softwaremill.com/en/v2/backends/javascript/fetch.html). It was all well and good until I hit a
wall: this backend implementation would require me to change server side Cloud Firestore's REST API to overcome a
[CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS) issue, which is not an option.

Back to the drawing board, I needed another HTTP library with a JavaScript backend. So I decided to try
[http4s](https://http4s.org/), after all it also supports lots of backends and favours the *pure functional* side of
Scala through the use of [Cats Effect](https://typelevel.org/cats-effect/). It turned out to be an interesting
learning opportunity on Scala.js as a whole. Bear with me.

On choosing an http4s backend, I started off by searching for the ones with a corresponding Scala.js offering. *Ember*
looked good at first: it offers binaries for Scala.js
[2.13 and 3](https://http4s.org/v1/docs/client.html#creating-the-client) and has an
example on [creating the client](https://http4s.org/v1/docs/client.html#creating-the-client) in the http4s
documentation. It turns out that **just because a library transpiles to JavaScript, it does not mean that it can run on
the browser**. That is the case with ember, as it relies on plain TCP sockets, which are not available in the browser
(check [this issue](https://stackoverflow.com/questions/40599069/node-js-net-socket-is-not-a-constructor) on stack
overflow). And so I learned that ember clients are fine for Node.js, but not for the browser.

Ok, I still needed a suitable http4s backend for my refactor. At the corner of http4s documentation page there are some
*related projects*. One of them - [http4s-dom](https://http4s.github.io/http4s-dom/) - looked very promising...

<div class="aside">
  <img src="/img/http4s-dom.png" />
  <figcaption>Fig.1 - http4s-dom documentation snippet</figcaption>
</div>

...until I read the dreadind words "backed by fetch"... No, *fetch* again! What if I ran into the CORS issue once more?
I had to try it. I already had all TDD tests waiting for the code.

Changing from ember to the *fetch* client was super simple. Apart from building the client itself, which ember "wraps"
in a [Cats Effect Resource](https://typelevel.org/cats-effect/docs/std/resource) typeclass, the REST API calls used the
very same interfaces, as expected from a very well designed library such as http4s. (You can check the final code
[here](https://github.com/talestonini/talestonini.com/blob/master/src/main/scala/com/talestonini/db/CloudFirestore.scala).)
But... as soon as I tried to compile without dependency *http4s-ember-client* and with new dependency *http4s-dom*, I
got an annoying *binary incompatibility*:

<div class="aside">
  <img src="/img/binary-incompatibility.png" />
  <figcaption>Fig.2 - Binary incompatibility across versions of dependency scalajs-dom</figcaption>
</div>

**A *binary incompatibility* happens when your code depends on different breaking versions of a library.** My website
already had the following dependencies:

- *org.scala-js : scalajs-dom* version *1.1.0*
- *com.thoughtworks.binding : route*, which in turn depends on *scalajs-dom* version *1.0.0*
- *org.lrng.binding : html*, which in turn depends on *scalajs-dom* version *0.9.8*

Versions *1.1.0*, *1.0.0* and *0.9.8* of *scalajs-dom* are all compatible among themselves (i.e. non-breaking), so the
compiler/linker never had an issue. However, when I added dependency:

- *org.http4s : http4s-dom*, which in turn depends on *scalajs-dom* version *2.1.0*

a *breaking* change between *scalajs-dom* version *2.1.0* and its previous versions was introduced. Panicking, I tried
updating the first dependency (*scalajs-dom*) to version *2.1.0*, hoping the error would magically disappear somehow,
but that still revealed the binary incompatibility. The 3 original dependencies listed above are non-negotiable - they
are the bricks and mortar of the website build. This time, after all the journey I had already been to choose an HTTP
client library with a JavaScript backend to replace RösHTTP, I was not in the mood to search for another HTTP library.

In the end, "brute force" was needed, and I thank once again open source projects. I'll tell you why. Having:

- no version alternative to remove the binary incompatibility;
- no desire to rebuild the whole website without ThoughtWorks Binding, and;
- no desire to go search for yet another HTTP library with JavaScript backend,

I resorted to **forking** *scalajs-dom* from version *1.1.0* including all the missing components from version *2.1.0*
needed by *http4s-dom*. That is essentially a **merge** between versions *1.1.0* and parts of *2.1.0*, which constitute
my new website dependency:

- *org.scala-js : scalajs-dom* version *1.1.0+179-fa23209f-SNAPSHOT*

All the code compiles fine now. For this to finally work, I also excluded transitive dependency on *scalajs-dom* from
all involved libraries *route*, *html* and *http4s-dom*.

You can check out the complete list of dependencies in
[build.sbt](https://github.com/talestonini/talestonini.com/blob/master/build.sbt), my fork of
[scalajs-dom](https://github.com/talestonini/scala-js-dom), and some more info on
[preventing version conflicts with versionscheme](https://www.scala-lang.org/blog/2021/02/16/preventing-version-conflicts-with-versionscheme.html)
(however, this is more for Scala library writers than for library users, like me in this case).
