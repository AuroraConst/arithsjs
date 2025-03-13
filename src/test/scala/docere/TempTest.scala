package docere

import org.scalatest._
import wordspec._
import matchers._
import testingutils.*




class TempTest extends wordspec.AsyncWordSpec with should.Matchers :
  override implicit def executionContext = global
  val ast = Try{parse(testFiles(0)).toFuture}.recover(e => {
        info(s"error: $e")
        Future.failed(e)
      }).get

  "CstUtils" should {
    "get offset information" in {
      // import langCstUtils.*
      import typings.arith.outLanguageArithUtilMod.CstUtils.*
      ast.map{ module =>
        val result =  findLeafNodeAtOffset(module.$cstNode.toOption.get, 0).toOption.get
        info(s"offset: ${result.offset}")
        result.offset should be(0)
      }
    }
  }  



  "Stream Ast" should {
    "work like this" in {
      import typings.arith.outLanguageArithUtilMod.AstUtils.*
      ast.map {
        streamAllContents(_)
      }.map{ s =>
        s.forEach{ (node,v) =>
          info(s"${node.$type}: ${node.$cstNode.toOption.get.text}, $v")
        }
      }

      Future("not implemented" should be ("not implemented"))
    }
  }

 


