package com.axiom

import org.scalatest._
import wordspec._
import matchers._
import scala.scalajs.js
import scala.concurrent.Future
import scala.util.{Try, Success, Failure}
import testingutils._
import typings.arith.outLanguageGeneratedAstMod.{Module, Evaluation}
import scala.scalajs.js.JSConverters._

class ArithParserTest extends wordspec.AsyncWordSpec with should.Matchers {

  override implicit def executionContext = global

  
  def safeEvaluate(expression: String): Double = {
    val jsExpression = expression.replaceAll("\\^", "**").replaceAll(";", "")
    js.Dynamic.global.eval(jsExpression).asInstanceOf[Double]
  }


  def readFile(filePath: String): String = {
    val fs = js.Dynamic.global.require("fs")
    fs.readFileSync(filePath, "utf8").toString
  }

  "Test files" should {
    "exist" in {
      info(s"Platform: ${platform}")
      val checkFiles = testFiles.map { fn =>
        info(s"filename: $fn")
        checkFileAccess(fn)
      }.map(_ == true)
      Future(checkFiles) map { l =>
        l.forall(_ == true) should be (true)
      }
    }
  }

  "arith file" should {
    "work for any number of terms" in {
      
      val filePath = testFiles(2)
      val fileContent = readFile(filePath)

      val parseFuture: Future[Module] = Try(parse(filePath).toFuture) match {
        case Success(fut) => fut
        case Failure(e) =>
          info(s"Error during parsing: $e")
          Future.failed(e)
      }

      parseFuture.flatMap { module =>
        val results = interpretEvaluations(module).toScalaMap
        
        val evaluations = module.statements.filter { s =>
          val t = s.asInstanceOf[js.Dynamic].selectDynamic("$type").asInstanceOf[String]
          t == "Evaluation"
        }.map(_.asInstanceOf[Evaluation])
        results.size should be (evaluations.length)
        module.name should be ("binaryexpressions")

        Future.traverse(results.toSeq) { case (evaluation: Evaluation, computedValue: Double) =>
          Future {
           
            val exprDyn = evaluation.expression.asInstanceOf[js.Dynamic]
            val cstNode = exprDyn.selectDynamic("$cstNode")
            val offset = cstNode.offset.asInstanceOf[Int]
            val length = cstNode.length.asInstanceOf[Int]
            val exprText = fileContent.substring(offset, offset + length).trim
            val expectedValue = safeEvaluate(exprText)
            info(s"Testing: $exprText → Computed: $computedValue, Expected: $expectedValue")
            computedValue should equal (expectedValue)
          }
        }.map(_ => succeed)
      }
    }
  }
}
