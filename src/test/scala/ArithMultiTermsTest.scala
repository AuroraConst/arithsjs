package com.axiom

import org.scalatest._
import wordspec.AsyncWordSpec
import matchers.should.Matchers
import scala.scalajs.js
import testingutils._
import typings.arith.outLanguageGeneratedAstMod.{Module, BinaryExpression, NumberLiteral, Evaluation, isBinaryExpression}

import scala.concurrent.Future
import scala.util.Try

class ArithMultiTermsTest extends AnyWordSpec with Matchers {

  "math3" should 
    "work" in {

      case class TestExpr(expression: String, expectedValue: Int)

      val testExpressions = List(
        TestExpr("2 + 3 * 4", 14),
        TestExpr("(2 + 3) * 4", 20),
        TestExpr("5 - 2 * 3 + 1", 0),
        TestExpr("2 ^ 3 % 3", 2)
      )

      val astFuture = Try(parse(testFiles(0)).toFuture).getOrElse(Future.failed(new Exception("Parsing failed")))

      astFuture.map { module =>
        val result = interpretEvaluations(module).toScalaMap.map { (k, v) =>
          val exprStr = k.expression.$cstNode.text.trim
          (exprStr, v)
        }

        testExpressions.foreach { te =>
          withClue(s"Expression '${te.expression}' evaluation") {
            result.get(te.expression) should contain (te.expectedValue)
          }
        }

        module.name shouldBe "complexexpressions"
        result.size shouldBe testExpressions.size
      }
    }
  
}