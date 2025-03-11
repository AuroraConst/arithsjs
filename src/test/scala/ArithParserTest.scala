package com.axiom

import org.scalatest._
import wordspec._
import matchers._
import scala.scalajs.js

import testutils.FileReader
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
  import FileReader.*
  val fExtension = "arith"
  val filenames = List("math1", "math2").map{filename}
  private def filename(name: String) = FileReader.testAuroraFiles / s"$name.$fExtension"

  override implicit def executionContext = scala.concurrent.ExecutionContext.Implicits.global

  "Test files" should {
    "exist" in {
      info(s"Platform: ${FileReader.platform}")
      
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
      import typings.arith.outCliCliUtilMod.{parse}
      import typings.arith.outLanguageArithEvaluatorMod.interpretEvaluations
      case class BinExp(isBinary:Boolean, left:Double, right:Double, operator:String, value:Double)

      var result:mutable.ListBuffer[BinExp] = ListBuffer()

      

      val ast = Try{parse(filenames(0)).toFuture}.recover(e => {
        info(s"error: $e")
        Future.failed(e)
      }).get

      ast.map{ module =>
        val keys = interpretEvaluations(module).forEach((value, key,map) => {
          val isBinary = isBinaryExpression(key.expression)
          val expression = key.expression.asInstanceOf[BinaryExpression]
          val operator = expression.operator
          val left = expression.left.asInstanceOf[NumberLiteral].value
          val right = expression.right.asInstanceOf[NumberLiteral].value
          result.addOne(BinExp(isBinary,left, right, s"$operator", value)) 
          info(s"isBinary: $isBinary left: $left right: $right operator: $operator, value: $value")
        })
        
        info(s"$result")
        module.name should be ("binaryexpressions")
      }
      

      }



    }
  }
  


