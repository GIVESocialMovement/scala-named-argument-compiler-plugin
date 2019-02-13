package givers.scala.namedargument

import scala.reflect.internal.util.NoPosition
import scala.tools.nsc.{Global, Phase}
import scala.tools.nsc.plugins.{PluginComponent, Plugin => BasePlugin}

case class Position(path: String, line: Int, offset: Int)

class ScalacOptions(global: Global) {
  private[this] var annotationOpt: Option[String] = None

  def setAnnotation(a: String): Unit = { annotationOpt = Some(a) }
  def hasAnnotation: Boolean = annotationOpt.isDefined
  def getAnnotation: String = {
    annotationOpt.getOrElse("")
  }
}

class Plugin(override val global: Global) extends BasePlugin {

  override val name: String = "named-argument"
  override val description: String = "Scala compiler plugin that enforces named arguments on certain method invocations"

  val pluginOptions = new ScalacOptions(global)

  override def init(options: List[String], error: String => Unit): Boolean = {
    for (option <- options) {
      if (option.startsWith("annotation:")) {
        pluginOptions.setAnnotation(option.substring("annotation:".length))
      } else {
        error("Option not understood: " + option)
        return false
      }
    }

    if (pluginOptions.hasAnnotation) {
      true
    } else {
      error("givers.scala.namedargument.Plugin: The annotation isn't defined through the compiler's options.")
      false
    }
  }

  override val optionsHelp: Option[String] = Some(
    "  -P:named-argument:annotation:<fullyQualifiedClassName>             Set the name of the annotation for enforcing named arguments")

  val index = new IndexMethodCall(global)
  override val components: List[PluginComponent] = List(index, new CheckMethodCall(index, pluginOptions, global))
}

class CheckMethodCall(val index: IndexMethodCall, val options: ScalacOptions, val global: Global) extends PluginComponent {
  override val phaseName: String = "named-argument-check-phase"
  override val runsAfter: List[String] = List("typer")

  override def newPhase(prev: Phase): Phase = new StdPhase(prev) {

    override def run(): Unit = {
      val annotationClass = global.findMemberFromRoot(global.TypeName(options.getAnnotation))
      val staticAnnotationClasss = global.findMemberFromRoot(global.TypeName("scala.annotation.StaticAnnotation"))
      if (annotationClass.isClass && annotationClass.baseClasses.contains(staticAnnotationClasss)) {
        super.run()
      } else {
        global.reporter.error(NoPosition, s"givers.scala.namedargument.Plugin: The annotation ${options.getAnnotation} doesn't exist, or it doesn't extend from scala.annotation.StaticAnnotation.")
      }
    }

    override def apply(unit: global.CompilationUnit): Unit = {
      import global._
      for (tree @ Apply(fun, args) <- unit.body if fun.symbol.isInstanceOf[MethodSymbol]) {
        val method = fun.symbol.asInstanceOf[MethodSymbol]

        if (method.annotations.exists(_.symbol.fullName == options.getAnnotation)) {
          if (!index.isNamedArgumentUsed(Position(tree.pos.source.path, tree.pos.line, tree.pos.column))) {
            global.reporter.error(tree.pos, s"An invocation of ${method.name.toString}(..) must use named arguments.")
          }
        }
      }
    }
  }
}

class IndexMethodCall(val global: Global) extends PluginComponent {
  override val phaseName: String = "named-argument-index-phase"
  override val runsAfter: List[String] = List("parser")

  private[this] val nonNamedArgumentCalls: scala.collection.mutable.Set[Position] = scala.collection.mutable.Set.empty[Position]

  def isNamedArgumentUsed(pos: Position): Boolean = {
    !nonNamedArgumentCalls.contains(pos)
  }

  override def newPhase(prev: Phase): Phase = new StdPhase(prev) {
    override def apply(unit: global.CompilationUnit): Unit = {
      import global._

      for (tree @ Apply(_, args) <- unit.body) {
        if (args.exists(!_.isInstanceOf[AssignOrNamedArg])) {
          nonNamedArgumentCalls.add(Position(tree.pos.source.path, tree.pos.line, tree.pos.column))
        }
      }
    }
  }
}