package com.axiom

import org.scalatest._
import wordspec._
import matchers._
import scala.scalajs.js

import testutils.FileReader
import typings.arith.outLanguageGeneratedAstMod.{Module}
// import typings.auroraone.auroraoneStrings.{Clinical,Issues,Orders}
import scala.concurrent.Future
import typings.std.stdStrings.s

class ArithParserTest extends wordspec.AsyncWordSpec with should.Matchers {
  val fExtension = "arith"
  val filenames = List("math1", "math2").map{filename}
  private def filename(name: String) = FileReader.testAuroraFiles + "\\" +s"$name.$fExtension"

  override implicit def executionContext = scala.concurrent.ExecutionContext.Implicits.global

  "Test files" should {
    "exist" in {
      
      // info(s"checkFiles: $checkFiles")    
      val checkFiles =  filenames
          .map{fn => 
            info(s"filename: $fn")
            FileReader.checkFileAccess(fn)}
          .map{ _ == true }

      Future(checkFiles) map { l =>
        l.forall(_ == true) should be (true) }
      }
  }

   "math1 file" should {
    "work" in {
      import typings.arith.outLanguageArithEvaluatorMod.interpretEvaluations

        Future{ 5+3 should be (8)}

      }
    }
  


}
