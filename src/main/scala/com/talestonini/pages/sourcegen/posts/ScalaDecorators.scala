/** *************************************************************************************************
  *
  * THIS CODE IS GENERATED AT COMPILE TIME BY LAIKA SBT PLUGIN.
  *
  * Do not modify it directly, as compilation will ovewrite your modifications.
  */
package com.talestonini.pages.sourcegen.posts

import com.talestonini.CodeSnippets
import com.talestonini.pages.BasePost
import scala.xml.Elem

object ScalaDecorators extends BasePost {

  def postContent(): Elem =
    <div class="markdown-post-body">
      <p>Decorator is a structural Design Pattern whose intent, according to Erich Gamma and others in their classic book
      <strong>Design Patterns: Elements of Reusable Object-Oriented Software</strong>, is:</p>
      <blockquote>Attach additional responsibilities to an object dynamically. Decorators provide a flexible alternative to subclassing
      for extending functionality. (Erich Gamma and others, 1994, 175)</blockquote>
      <p>Also known as <em>wrappers</em>, decorators are the <em>preferred</em> alternative to inheritance, as they are more pluggable, less
      static and over time are friendlier to code refactoring.</p>
      <p>In this post, I would like to show how to take advantage of some really nice features of the Scala language to write
      elegant decorators. Besides that, I&#39;ll explore how we can extend functionality of third-party libraries even when they
      are explicitly marked to not allow inheritance (i.e. <em>final</em> classes in Java, or <em>sealed</em> in Scala).</p>
      <h1 id="the-problem-a-sealed-library-class" class="title">The problem: a <em>sealed</em> library class</h1>
      <p>Imagine we want to extend functionality of a library class that is marked <em>sealed</em>, like the one below:</p>
      <pre><code class="lang-scala">{CodeSnippets.ScalaDecorators.thirdPartyStuff()}</code></pre>
      <div class="aside"><figcaption>Snippet.1 - Third-party library class marked sealed</figcaption></div>
      <p>Sealed classes can only be extended in their own Scala file. Since this is a third-party library, we are not able to do
      that and if we stubbornly tried to extend the ThirdPartyStuff class above in our own code, the Scala compiler would
      complain.</p>
      
      <h2 id="how-can-a-decorator-help-here" class="section">How can a <em>decorator</em> help here?</h2>
      <p>With a decorator like the following, we can overcome that issue and attach new functionality to the third-party library
      class <em>dynamically</em>:</p>
      <pre><code class="lang-scala">{CodeSnippets.ScalaDecorators.traditionalDecorator()}</code></pre>
      <div class="aside"><figcaption>Snippet.2 - A traditional decorator</figcaption></div>
      <p>Note how at creation time the decorator will expect to be passed an instance of the decorated class, i.e. the object to
      be wrapped. And because the decorator must adhere to the interface of the wrapped class (after all that&#39;s what makes it
      look like it&#39;s extending the original class), we need to &quot;repeat&quot; the original functionality, forwarding calls to the
      wrapped object.</p>
      <p>The following snippet shows how we can invoke the newly attached functionality through the decorator instance:</p>
      <pre><code class="lang-scala">{CodeSnippets.ScalaDecorators.usingTraditionalDecorator()}</code></pre>
      <div class="aside"><figcaption>Snippet.3 - Using a traditional decorator</figcaption></div>
      <p>Now our third-party object can do something special. But isn&#39;t it so annoying how we needed to repeat the whole
      interface of the original class to add the special function?</p>
      
      <h2 id="using-scala-s-implicit-conversions" class="section">Using Scala&#39;s <em>implicit conversions</em></h2>
      <p>Scala has this (somewhat) controversial feature called <strong>implicits</strong> and with it, implicit conversions. Implicits allow
      us to declare a value (or variable or function) <em>implicit</em> within a scope. Then, if there is a function call in <em>that
      scope</em> that expects a value of <em>that type</em>, and the implicit value happens to be <em>the only</em> implicit value of that type
      in that scope, then it is passed in to the function, with no need for explicitly mentioning it in the call. Implicits
      can make our code look much cleaner and elegant (and sometimes a little harder to follow too, so use it wisely). They
      resemble the <a href="https://www.baeldung.com/spring-autowire">Spring framework&#39;s component <em>autowiring</em></a>, in a way, or
      <em>Dependency Injection</em>, more broadly speaking.</p>
      <p>But what about implicit <em>conversions</em>? Simply put, <strong>implicit conversions</strong> are a feature in the Scala language by which
      an implicit function definition can <em>automatically</em> coerce a type into another type. Consider a scope that has such
      function coercing type <strong>A</strong> into type <strong>B</strong>. The scope also has a value of type <strong>A</strong> and a statement where <strong>B</strong> is
      expected. In such scope, missing a value of type <strong>B</strong> would not be a problem, because the implicit function would
      automatically &quot;kick in&quot; to convert the value of type <strong>A</strong> into a <strong>B</strong>.</p>
      <p>Let&#39;s look at some code to make things clearer:</p>
      <pre><code class="lang-scala">{CodeSnippets.ScalaDecorators.scala2Decorator()}</code></pre>
      <div class="aside"><figcaption>Snippet.4 - A Scala 2 decorator, created with implicit conversion</figcaption></div>
      <p>The implicit converter function definition is in object Scala2Decorator (it could have any name, really). The object has
      an accompanying class where functionality of the third-party library is extended with a special function. Note here how
      the decorator did not have to &quot;repeat&quot; the interface of the decorated class!</p>
      <p>The following snippet shows how we can invoke the dynamically attached special function:</p>
      <pre><code class="lang-scala">{CodeSnippets.ScalaDecorators.usingScala2Decorator()}</code></pre>
      <div class="aside"><figcaption>Snippet.5 - Using a Scala 2 decorator</figcaption></div>
      <p>Note how we can call the special function straight from the wrapped class instance. The fact that there is a decorator
      wrapping our library class is actually barely noticeable! But how does that work? The special function belongs to the
      decorator, not to <em>tps</em>, doesn&#39;t it!? Well, the implicit converter function imported into the scope is able to kick in
      and coerce <em>tps</em> into a Scala2Decorator, because it &quot;knows&quot; how to convert a ThirdPartyStuff into a Scala2Decorator,
      allowing the newly attached special function to be invoked. Really nice, isn&#39;t it?</p>
      <p>Now, can we improve this even further?</p>
      
      <h2 id="using-scala-3-s-extension-methods" class="section">Using Scala 3&#39;s <em>extension methods</em></h2>
      <p>Scala 3 was recently launched and many improvements were added to the whole family of implicit features. As part of
      these, <strong>extension methods</strong> bring new syntax to simplify the extension of classes. As stated in the
      <a href="https://docs.scala-lang.org/scala3/reference/contextual/extension-methods.html">documentation</a>, <em>&quot;extension methods
      allow one to add methods to a type after the type is defined&quot;</em>. It sounds perfect to write a new decorator, right?</p>
      <p>Check out the following code snippet for a taste of extension methods:</p>
      <pre><code class="lang-scala">{CodeSnippets.ScalaDecorators.scala3Decorator()}</code></pre>
      <div class="aside"><figcaption>Snippet.6 - A Scala 3 decorator, created with an extension method</figcaption></div>
      <p>Not even a new type is needed here! Following is how we can invoke our decorated library class now:</p>
      <pre><code class="lang-scala">{CodeSnippets.ScalaDecorators.usingScala3Decorator()}</code></pre>
      <div class="aside"><figcaption>Snippet.7 - Using a Scala 3 decorator</figcaption></div>
      <p>Here we need to import the extension method into the code, if not in the same package, as you&#39;d expect.</p>
      <p>Extension methods are very rich, allowing the definition of operators, generic and collective extensions. I&#39;m not going
      to exhaust the topic of extension methods here, since the intent of this post is to elaborate on decorators in Scala.</p>
      
      <h2 id="conclusion" class="section">Conclusion</h2>
      <p>In this post we saw what a <em>decorator</em> is in the realm of Object Oriented Design Patterns and defined three ways of
      writing decorators in Scala, from a traditional way to more elegant ones, utilising features of the Scala language in
      its versions 2 and 3. Check out the <a href="https://github.com/talestonini/scala-decorators">full working code</a> of the snippets
      above in GitHub.</p>
      <p>To finalise, I&#39;d like to share that my favourite application of decorators in Scala is actually defining new <em>functional
      style</em> methods for third-party library API&#39;s such as Apache Spark&#39;s
      DataFrame/<a href="https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/sql/Dataset.html">Dataset</a> or
      Apache Kafka Streams&#39; <a href="https://kafka.apache.org/27/javadoc/org/apache/kafka/streams/kstream/KStream.html">KStream</a>.</p>
      <p>If you got down to here, thank you! I hope you enjoyed the read and would really like to receive your feedback.</p>
      <p>Cheers!</p>
    </div>

}
