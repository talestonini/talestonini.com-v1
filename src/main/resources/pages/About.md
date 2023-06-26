{%
  class.name = About
%}
#About me

<div class="aside">
  <table class="w3-hide-small" style="width:100%">
    <tr>
      <td style="padding-right: 15px; width: 30%;"><img src="/img/talestonini.jpg" /></td>
      <td>
        <p>Hi!, my name is <strong>Tales Tonini</strong> and I'm a software engineer interested in Functional Programming, Distributed Systems and the Scala language.</p>
        <p>I work in data engineering for an AI consultancy called <a href="https://eliiza.com.au/">Eliiza</a>, and for the past 5 years I've mainly worked with Spark and Kafka, platforms that I really enjoy to program in.</p>
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
      <td style="padding-right: 10px; width: 30%;"><img src="/img/talestonini.jpg" /></td>
      <td>
        Hi!, my name is <strong>Tales Tonini</strong> and I'm a software engineer interested in Functional Programming, Distributed Systems and the Scala language.
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <p>I work in data engineering for an AI consultancy called <a href="https://eliiza.com.au/">Eliiza</a>, and for the past 5 years I've mainly worked with Spark and Kafka, platforms that I really enjoy to program in.</p>
        <p>I live in Melbourne, Australia, but am originally from Brazil. Aside from programming, I like to spend time with family and friends, swimming and the outdoors.</p>
      </td>
    </tr>
  </table>
</div>

#About my website

I started this website to share my interests and learnings and as a way to play around with Scala.js. These are some of
the technologies and libraries that I use to build it:

- [Scala 2.13](https://www.scala-lang.org/)
- [Scala.js](https://www.scala-js.org/)
- [ThoughtWorks Binding](https://github.com/ThoughtWorksInc/Binding.scala)
- [Laika](https://planet42.github.io/Laika/)
- [Firebase](https://firebase.google.com/)
- [Cloud Firestore](https://firebase.google.com/firebase/cloud-firestore)
- [W3.CSS](https://www.w3schools.com/w3css/default.asp)
- [PrismJS](https://prismjs.com/index.html)

The **source code is open** [in my GitHub account](https://github.com/talestonini/talestonini.com) and I would gladly
receive feedback about it.

As you can see, I built a little engine to generate Scala.js code for the posts I write in
[Markdown](https://en.wikipedia.org/wiki/Markdown).

#Release notes

###0.2.x
- Replaced [RösHTTP](https://github.com/hmil/RosHTTP) for [http4s-dom](https://http4s.github.io/http4s-dom/) due to the
former not being maintained anymore and to give me a reason to play with [Cats](https://typelevel.org/cats/). This is at
the database layer, implementing API calls to Cloud Firestore.
- Packaging the app with [scalajs-bundler](https://scalacenter.github.io/scalajs-bundler/).

###0.3.x
- Added links that allow for sharing a post via LinkedIn and Twitter, and also for copying a post URL to the clipboard.

###0.4.x
- Refactored database package to remove usage of **Future** in favour of **Cats IO**.

###0.5.x
- Code cleanup.
