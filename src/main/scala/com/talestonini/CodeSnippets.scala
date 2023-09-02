package com.talestonini

object CodeSnippets {

  object ScalaDecorators {
    def thirdPartyStuff(): String =
      """package com.acme
        |
        |sealed class ThirdPartyStuff {
        |  val prop: String = "foo"
        |  var otherProp: String = "bar"
        |
        |  def doSth() =
        |    println(s"doing something with $prop")
        |
        |  def doSthElse() =
        |    println("doing something else")
        |
        |  def doAnotherThing() =
        |    println(s"doing another thing with $otherProp")
        |}
        |""".stripMargin

    def traditionalDecorator(): String =
      """package decorator
        |
        |import com.acme._
        |
        |class TraditionalDecorator(tps: ThirdPartyStuff) {
        |  def doSth() = tps.doSth()
        |  def doSthElse() = tps.doSthElse()
        |  def doAnotherThing() = tps.doAnotherThing()
        |
        |  def doSthSpecial() = {
        |    println(s"doing something special with ${tps.prop}, the traditional way")
        |    tps.otherProp = "baz"
        |  }
        |}
        |""".stripMargin

    def usingTraditionalDecorator(): String =
      """package decorator
        |
        |import com.acme._
        |
        |  ...
        |  val tps = new ThirdPartyStuff()
        |  tps.doSth()
        |
        |  val decorator = new TraditionalDecorator(tps)
        |  decorator.doSthSpecial()
        |  ...
        |
        |""".stripMargin

    def scala2Decorator(): String =
      """package decorator.scala2
        |
        |import com.acme._
        |
        |object Scala2Decorator {
        |  implicit def converter(tps: ThirdPartyStuff): Scala2Decorator =
        |    Scala2Decorator(tps)
        |}
        |
        |class Scala2Decorator(tps: ThirdPartyStuff) {
        |  def doSthSpecial() = {
        |    println(s"doing something special with ${tps.prop}, the Scala 2 way")
        |    tps.otherProp = "baz"
        |  }
        |}
        |""".stripMargin

    def usingScala2Decorator(): String =
      """package decorator
        |
        |import scala2._
        |
        |  ...
        |  import Scala2Decorator.converter
        |
        |  val tps = new ThirdPartyStuff()
        |  tps.doSth()
        |  tps.doSthSpecial()
        |  ...
        |
        |""".stripMargin

    def scala3Decorator(): String =
      """package decorator.scala3
        |
        |import com.acme._
        |
        |extension (tps: ThirdPartyStuff)
        |  def doSthMoreSpecial() = {
        |    println(s"doing something even more special with ${tps.prop}, the Scala 3 way")
        |    tps.otherProp = "baz"
        |  }
        |""".stripMargin

    def usingScala3Decorator(): String =
      """package decorator
        |
        |import scala3.doSthMoreSpecial
        |
        |  ...
        |  val tps = new ThirdPartyStuff()
        |  tps.doSth()
        |  tps.doSthMoreSpecial()
        |  ...
        |
        |""".stripMargin
  }

}
