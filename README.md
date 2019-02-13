scala-named-argument-compiler-plugin
=====================================

This compiler plugin enables you to enforce named arguments on on certain methods' invocations.

If you would like to enforce named arguments on *all* method invocations, then it is better to use [scalastyle](http://scalastyle.org).


Usage
------

In your `build.sbt`, add:

```
resolvers += Resolver.bintrayRepo("givers", "maven")

scalacOptions ++= Seq(
  "-P:named-argument:annotation:framework.ForceNamedArgument"    // Enforce certain method invocations with named arguments
)

addCompilerPlugin("givers.scala.namedargument" %% "scala-named-argument-compiler-plugin" % "0.1.0")
```

Notice that `-P:named-argument:annotation:` (in `scalacOptions`) points the class `framework.ForceNamedArgument` (You can change this class to your liking).

With the `scalacOptions` above, please add the below class to your project:

```
package framework

import scala.annotation.StaticAnnotation

class ForceNamedArgument extends StaticAnnotation
```

Then, for any method that you would like to enforce named arguments, you can annotate the method with `@ForceNamedArgument` as shown below:

```
@framework.ForceNamedArgument
def toJson(showName: Boolean = true, showSecret: Boolean = false): JsValue = { ... }
```
