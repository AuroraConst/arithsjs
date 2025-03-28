package testingutils
  export com.axiom.testutils.FileUtils.*
  export com.axiom.testutils.Convertors.*
  export scala.scalajs.js.JSConverters.*
  export scala.concurrent.ExecutionContext.Implicits.global
  export scala.concurrent.Future
  export scala.util.Try
  export typings.arith.outCliCliUtilMod.{parse}
  export typings.arith.outLanguageArithEvaluatorMod.interpretEvaluations
  export typings.arith.outLanguageGeneratedAstMod as langGenAst
  
  //the following two imports were helped by exporting them from arith-utils from the typesript project
  // export typings.arith.outLanguageArithUtilMod.AstUtils as langAstUtils
  // export typings.arith.outLanguageArithUtilMod.CstUtils as langCstUtils


  val fExtension = "arith"
  val testFiles = List("math1", "math2", "math3").map{testFullPath}
  
  private  def testFullPath(name: String) = testAuroraFiles / s"$name.$fExtension"
