package cat.udl.eps.is

import munit.FunSuite
import scala.List
import BST.*

class BSTSuite extends FunSuite {

  test("empty") {
    val t = Empty
    assertEquals(t.find(1)(_ < _), false)
  }

  test("singleton") {
    val t = Node(Empty, 1, Empty)
    assertEquals(t.find(1)(_ < _), true)
  }

  test("roundtrip") {
    val l = List(3, 2, 1, 4)
    val t = fromList(l)(_ < _)
    assertEquals(inorder(t), l.sorted)
  }
}
