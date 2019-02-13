package givers.scala.namedargument.test

import scala.annotation.StaticAnnotation

class ForceNamedArgument extends StaticAnnotation

class Test {

  def main(args: Array[String]): Unit = {
    testFunction(
      a = false
    )
    testFunction(
      a = true
    )
  }

  @ForceNamedArgument
  def testFunction(a: Boolean): Unit = {

  }
}
