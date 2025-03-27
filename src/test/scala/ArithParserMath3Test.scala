package com.axiom

import org.scalatest._
import wordspec._
import matchers._
import scala.scalajs.js
import scala.concurrent.Future
import scala.util.Try
import testingutils._
import typings.arith.outLanguageGeneratedAstMod.{Module, BinaryExpression, Evaluation, isBinaryExpression, Expression}

class ArithParserMath3Test extends wordspec.AsyncWordSpec with should.Matchers {

  override implicit def executionContext = global

  "math3.arith file" should {
    "exist" in {
      val checkFiles = testFiles
        .map { fn =>
          info(s"filename: $fn")
          checkFileAccess(fn)
        }
        .map(_ == true)

      Future(checkFiles).map { list =>
        list.forall(_ == true) should be(true)
      }
    }

    "be parsed and evaluated correctly" in {
      // Using a simpler data structure to represent the evaluated expressions
      case class EvaluatedExpression(expressionText: String, value: Double) {
        override def toString = s"$expressionText = $value"
      }

      val ast = Try { parse(testAuroraFiles / "math3.arith").toFuture }.recover { case e =>
        info(s"error: $e")
        Future.failed(e)
      }.get

      ast.map { module =>
        val evaluations = interpretEvaluations(module).toScalaMap

        // Generate nicer output for the expressions
        val result = evaluations.map { case (k, v) =>
          // Get the expression text if possible
          val expressionText = try {
            // Try to get the original text from the AST
            val expr = k.expression.asInstanceOf[js.Dynamic]
            if (js.typeOf(expr.textRegion) != "undefined" &&
              js.typeOf(expr.textRegion.text) != "undefined") {
              expr.textRegion.text.toString
            } else {
              // Fall back to approximating the expression
              approximateExpressionText(k.expression)
            }
          } catch {
            case _: Throwable => "Expression"
          }

          EvaluatedExpression(expressionText, v)
        }.toList

        // Print the results
        result.foreach(r => info(s"$r"))

        // Check the expected values
        result.size should be(7)
        module.name should be("complexexpressions")

        // Verify the expected values
        val expectedValues = Map(
          "2 + 3 * 4" -> 14.0,
          "(2 + 3) * 4" -> 20.0,
          "5 - 2 * 3 + 1" -> 0.0,
          "2 ^ 3 * 2" -> 16.0,
          "10 / 2 + 3" -> 8.0,
          "8 % 3 + 2 * 2" -> 6.0,
          "3 * (10 / (2 + 3))" -> 6.0
        )

        // Check if we have all the expected values
        result.map(_.value).sorted should be(expectedValues.values.toList.sorted)
      }
    }
  }

  // Helper method to approximate the expression text
  def approximateExpressionText(expr: Expression): String = {
    if (isBinaryExpression(expr)) {
      val binExpr = expr.asInstanceOf[BinaryExpression]
      val left = approximateExpressionText(binExpr.left)
      val right = approximateExpressionText(binExpr.right)
      s"($left ${binExpr.operator} $right)"
    } else {
      // For number literals or other expression types
      try {
        val dynamicExpr = expr.asInstanceOf[js.Dynamic]
        if (js.typeOf(dynamicExpr.value) == "number") {
          dynamicExpr.value.toString
        } else {
          "expr"
        }
      } catch {
        case _: Throwable => "expr"
      }
    }
  }
}