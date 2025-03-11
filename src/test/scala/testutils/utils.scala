package testingutils
  export com.axiom.testutils.FileUtils.*
  export com.axiom.testutils.Convertors.*
  export scala.scalajs.js.JSConverters.*
  export scala.concurrent.ExecutionContext.Implicits.global
  export scala.concurrent.Future
  export scala.util.Try
  export typings.arith.outCliCliUtilMod.{parse}
  export typings.arith.outLanguageArithEvaluatorMod.interpretEvaluations

  val fExtension = "arith"
  val testFiles = List("math1", "math2").map{testFullPath}
  private  def testFullPath(name: String) = testAuroraFiles / s"$name.$fExtension"