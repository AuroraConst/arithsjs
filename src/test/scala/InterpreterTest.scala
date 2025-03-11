import org.scalatest._
import wordspec._
import matchers._



class InterpreterTest extends AnyWordSpec with should.Matchers{
  "this" should {
    "work" in {
      import typings.arith.outCliCliUtilMod.extractAstNode
        true should be(true)
    }
  }
}
