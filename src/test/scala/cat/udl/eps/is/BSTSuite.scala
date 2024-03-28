package cat.udl.eps.is

import munit.FunSuite
import scala.List
import BST.*

class BSTSuite extends FunSuite {

  test("empty") {
    val t = Empty
    assertEquals(t.find(1)(_ < _), false)
  }

  test("singleton gimeno") {
    val t = Node(Empty, 1, Empty)
    assertEquals(t.find(1)(_ < _), true)
  }

  test("singleton gms") {
    val t = Node(Empty, 1, Empty)
    assertEquals(t.find(1)(_ == _), true)
  }

  test("roundtrip") {
    val l = List(3, 2, 1, 4)
    val t = fromList(l)(_ < _)
    assertEquals(inorder(t), l.sorted)
  }

  test("insert") {
    val t = Node(Empty, 2, Empty)
    assertEquals(t.insert(1)(_ < _), Node(Node(Empty, 1, Empty), 2, Empty))
    assertEquals(t.insert(3)(_ < _), Node(Empty, 2, Node(Empty, 3, Empty)))
  }

  test("find") {
    val t = Node(Node(Empty, 1, Empty), 2, Node(Empty, 3, Empty))
    assertEquals(t.find(1)(_ == _), true)
    assertEquals(t.find(3)(_ == _), true)
    assertEquals(t.find(4)(_ == _), false)
  }

  test("fold") {
    val tree: BST[Int] = Node(Node(Empty, 1, Empty), 2, Node(Empty, 3, Empty))
    val result = tree.fold(0)((a1, a2, a3) => a1 + a2 + a3 )
    assert(result == 6)
  }

  test("fromList") {
    val list = List(3, 1, 2)
    val tree = BST.fromList(list)((a, b) => a < b)
    assert(tree == Node(Node(Empty, 1, Empty), 2, Node(Empty, 3, Empty)))
  }

  test("inorder") {
    val list = List(3, 1, 2)
    val tree = BST.fromList(list)((a, b) => a < b)
    val inorderList = BST.inorder(tree)
    assert(inorderList == List(1, 2, 3))
  }

}
