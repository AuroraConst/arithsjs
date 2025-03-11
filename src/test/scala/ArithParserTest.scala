package com.axiom

import org.scalatest._
import wordspec._
import matchers._
import scala.scalajs.js

import testingutils.*

import typings.std.Map
import typings.arith.outLanguageGeneratedAstMod.{Module, BinaryExpression, NumberLiteral,Evaluation,isBinaryExpression}
import typings.arith.arithStrings.{Percentsign, Asterisk,Plussign,`-_`,Slash,^}
import scala.concurrent.Future
import scala.collection.mutable
import scala.scalajs.js.JSConverters.*
import scala.util.Try
import typings.vscodeLanguageserverTypes.mod.SemanticTokenTypes.operator
import scala.collection.mutable.ListBuffer

class ArithParserTest extends wordspec.AsyncWordSpec with should.Matchers {
  val fExtension = "arith"
  val filenames = List("math1", "math2").map{filename}
  private def filename(name: String) = testAuroraFiles / s"$name.$fExtension"

  override implicit def executionContext = scala.concurrent.ExecutionContext.Implicits.global

  "Test files" should {
    "exist" in {
      info(s"Platform: ${platform}")
      
      // info(s"checkFiles: $checkFiles")    
      val checkFiles =  filenames
          .map{fn => 
            info(s"filename: $fn")
            checkFileAccess(fn)}
          .map{ _ == true }

      Future(checkFiles) map { l =>
        l.forall(_ == true) should be (true) }
      }
  }

  "math1 file" should {
    "work" in {
      import typings.arith.outCliCliUtilMod.{parse}
      import typings.arith.outLanguageArithEvaluatorMod.interpretEvaluations
      case class BinExp(isBinary:Boolean, left:Double, right:Double, operator:String, value:Double):
        override def toString = s" $left $operator $right =  $value"


      val ast = Try{parse(filenames(0)).toFuture}.recover(e => {
        info(s"error: $e")
        Future.failed(e)
      }).get

      ast.map{ module =>
        val result = interpretEvaluations(module).toScalaMap.map{(k,v) => 
          val isBinary = isBinaryExpression(k.expression)
          val expression = k.expression.asInstanceOf[BinaryExpression]
          val left = expression.left.asInstanceOf[NumberLiteral].value
          val right = expression.right.asInstanceOf[NumberLiteral].value
          val operator = s"${expression.operator}"
          BinExp(isBinary, left, right, operator, v)}
        result.foreach(s => info(s.toString))
        result.size should be (6)
        module.name should be ("binaryexpressions")
      }
      

      }



    }
  }
  


