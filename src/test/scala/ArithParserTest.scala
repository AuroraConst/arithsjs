package com.axiom

import org.scalatest._
import wordspec._
import matchers._
import scala.scalajs.js
// import typings.std.Map as TSMap
import testingutils.*
import typings.arith.outLanguageGeneratedAstMod.{Module, BinaryExpression, NumberLiteral,Evaluation,isBinaryExpression}




class ArithParserTest extends wordspec.AsyncWordSpec with should.Matchers {
  
  

  override implicit def executionContext = global

  "Test files" should {
    "exist" in {
      info(s"Platform: ${platform}")
      
      // info(s"checkFiles: $checkFiles")    
      val checkFiles =  testFiles
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
  
      case class TestBinaryExpression(isBinary:Boolean, left:Double, right:Double, operator:String, value:Double):
        override def toString = s" $left $operator $right =  $value"


      val ast = Try{parse(testFiles(0)).toFuture}.recover(e => {
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
          TestBinaryExpression(isBinary, left, right, operator, v)}
        result.foreach(r => info(s"$r"))
        result.size should be (6)
        module.name should be ("binaryexpressions")
      }
      

      }



    }
  }
  


