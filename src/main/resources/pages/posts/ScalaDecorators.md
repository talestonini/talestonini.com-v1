{%
  class.name = ScalaDecorators
%}
Decorator is a structural Design Pattern whose intent, according to Erich Gamma and others in their classic book
**Design Patterns: Elements of Reusable Object-Oriented Software**, is:

> Attach additional responsibilities to an object dynamically. Decorators provide a flexible alternative to subclassing
> for extending functionality. (Erich Gamma and others, 1994, 175)

Also known as *wrappers*, decorators are the *preferred* alternative to inheritance, as they are more pluggable, less
static and over time are friendlier to code refactoring.

In this post, I would like to show how to take advantage of some really nice features of the Scala language to write
elegant decorators. Besides that, I'll explore how we can extend functionality of third-party libraries even when they
are explicitly marked to not allow inheritance (i.e. *final* classes in Java, or *sealed* in Scala).

## The problem: a *sealed* library class

Imagine we want to extend functionality of a library class that is marked *sealed*, like the one below:

``` lang-scala
{CodeSnippets.ScalaDecorators.thirdPartyStuff()}
```
<div class="aside"><figcaption>Snippet.1 - Third-party library class marked sealed</figcaption></div>

Sealed classes can only be extended in their own Scala file. Since this is a third-party library, we are not able to do
that and if we stubbornly tried to extend the ThirdPartyStuff class above in our own code, the Scala compiler would
complain.

## How can a *decorator* help here?

With a decorator like the following, we can overcome that issue and attach new functionality to the third-party library
class *dynamically*:

``` lang-scala
{CodeSnippets.ScalaDecorators.traditionalDecorator()}
```
<div class="aside"><figcaption>Snippet.2 - A traditional decorator</figcaption></div>

Note how at creation time the decorator will expect to be passed an instance of the decorated class, i.e. the object to
be wrapped. And because the decorator must adhere to the interface of the wrapped class (after all that's what makes it
look like it's extending the original class), we need to "repeat" the original functionality, forwarding calls to the
wrapped object.

The following snippet shows how we can invoke the newly attached functionality through the decorator instance:

``` lang-scala
{CodeSnippets.ScalaDecorators.usingTraditionalDecorator()}
```
<div class="aside"><figcaption>Snippet.3 - Using a traditional decorator</figcaption></div>

Now our third-party object can do something special. But isn't it so annoying how we needed to repeat the whole
interface of the original class to add the special function?

## Using Scala's *implicit conversions*

Scala has this (somewhat) controversial feature called **implicits** and with it, implicit conversions. Implicits allow
us to declare a value (or variable or function) *implicit* within a scope. Then, if there is a function call in *that
scope* that expects a value of *that type*, and the implicit value happens to be *the only* implicit value of that type
in that scope, then it is passed in to the function, with no need for explicitly mentioning it in the call. Implicits
can make our code look much cleaner and elegant (and sometimes a little harder to follow too, so use it wisely). They
resemble the [Spring framework's component *autowiring*](https://www.baeldung.com/spring-autowire), in a way, or
*Dependency Injection*, more broadly speaking.

But what about implicit *conversions*? Simply put, **implicit conversions** are a feature in the Scala language by which
an implicit function definition can *automatically* coerce a type into another type. Consider a scope that has such
function coercing type **A** into type **B**. The scope also has a value of type **A** and a statement where **B** is
expected. In such scope, missing a value of type **B** would not be a problem, because the implicit function would
automatically "kick in" to convert the value of type **A** into a **B**.

Let's look at some code to make things clearer:

``` lang-scala
{CodeSnippets.ScalaDecorators.scala2Decorator()}
```
<div class="aside"><figcaption>Snippet.4 - A Scala 2 decorator, created with implicit conversion</figcaption></div>

The implicit converter function definition is in object Scala2Decorator (it could have any name, really). The object has
an accompanying class where functionality of the third-party library is extended with a special function. Note here how
the decorator did not have to "repeat" the interface of the decorated class!

The following snippet shows how we can invoke the dynamically attached special function:

``` lang-scala
{CodeSnippets.ScalaDecorators.usingScala2Decorator()}
```
<div class="aside"><figcaption>Snippet.5 - Using a Scala 2 decorator</figcaption></div>

Note how we can call the special function straight from the wrapped class instance. The fact that there is a decorator
wrapping our library class is actually barely noticeable! But how does that work? The special function belongs to the
decorator, not to *tps*, isn't it!? Well, the implicit converter function imported into the scope is able to kick in and
coerce *tps* into a Scala2Decorator, because it "knows" how to convert a ThirdPartyStuff into a Scala2Decorator,
allowing the newly attached special function to be invoked. Really nice, isn't it?

Now, can we improve this even further?

## Using Scala 3's *extension methods*

Scala 3 was recently launched and many improvements were added to the whole family of implicit features. As part of
these, **extension methods** bring new syntax to simplify the extension of classes. As stated in the
[documentation](https://docs.scala-lang.org/scala3/reference/contextual/extension-methods.html), *"extension methods
allow one to add methods to a type after the type is defined"*. It sounds perfect to write a new decorator, right?

Check out the following code snippet for a taste of extension methods:

``` lang-scala
{CodeSnippets.ScalaDecorators.scala3Decorator()}
```
<div class="aside"><figcaption>Snippet.6 - A Scala 3 decorator, created with an extension method</figcaption></div>

Not even a new type is needed here! Following is how we can invoke our decorated library class now:

``` lang-scala
{CodeSnippets.ScalaDecorators.usingScala3Decorator()}
```
<div class="aside"><figcaption>Snippet.7 - Using a Scala 3 decorator</figcaption></div>

Here we need to import the extension method into the code, if not in the same package, as you'd expect.

Extension methods are very rich, allowing the definition of operators, generic and collective extensions. I'm not going
to exhaust the topic of extension methods here, since the intent of this post is to elaborate on decorators in Scala.

## Conclusion

In this post we saw what a *decorator* is in the realm of Object Oriented Design Patterns and defined three ways of
writing decorators in Scala, from a traditional way to more elegant ones, utilising features of the Scala language in
its versions 2 and 3. Check out the [full working code](https://github.com/talestonini/scala-decorators) of the snippets
above in GitHub.

To finalise, I'd like to share that my favourite application of decorators in Scala is actually defining new *functional
style* methods for third-party library API's such as Apache Spark's
DataFrame/[Dataset](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/sql/Dataset.html) or
Apache Kafka Streams' [KStream](https://kafka.apache.org/27/javadoc/org/apache/kafka/streams/kstream/KStream.html).

If you got down to here, thank you! I hope you enjoyed the read and would really like to receive your feedback.

Cheers!
