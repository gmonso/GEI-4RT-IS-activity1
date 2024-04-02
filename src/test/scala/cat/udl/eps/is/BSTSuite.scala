package cat.udl.eps.is

import cat.udl.eps.is.BST.*
import munit.FunSuite

import scala.List

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
    assertEquals(t.find(1)(_ > _), true)
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
    assertEquals(t.find(1)(_ < _), true)
    assertEquals(t.find(3)(_ < _), true)
    assertEquals(t.find(4)(_ < _), false)
  }

  // Your existing test cases
  test("insert") {
    val t = Node(Empty, 2, Empty)
    assertEquals(t.insert(1)(_ < _), Node(Node(Empty, 1, Empty), 2, Empty))
    assertEquals(t.insert(3)(_ < _), Node(Empty, 2, Node(Empty, 3, Empty)))
  }

  test("find") {
    val t = Node(Node(Empty, 1, Empty), 2, Node(Empty, 3, Empty))
    assertEquals(t.find(1)(_ < _), true)
    assertEquals(t.find(3)(_ < _), true)
    assertEquals(t.find(4)(_ < _), false)
  }

  // Additional test cases
  test("insert into empty tree") {
    val t = Empty
    assertEquals(t.insert(1)(_ < _), Node(Empty, 1, Empty))
  }

  test("insert into tree with multiple levels") {
    val t = Node(Node(Empty, 1, Empty), 2, Node(Empty, 3, Empty))
    assertEquals(t.insert(4)(_ < _), Node(Node(Empty, 1, Empty), 2, Node(Empty, 3, Node(Empty, 4, Empty))))
  }

  test("find in empty tree") {
    val t = Empty
    assertEquals(t.find(1)(_ < _), false)
  }

  test("find non-existent element") {
    val t = Node(Node(Empty, 1, Empty), 2, Node(Empty, 3, Empty))
    assertEquals(t.find(5)(_ < _), false)
  }

  test("find in tree with one node") {
    val t = Node(Empty, 1, Empty)
    assertEquals(t.find(1)(_ < _), true)
    assertEquals(t.find(2)(_ < _), false)
  }

  test("fold") {
    val tree: BST[Int] = Node(Node(Empty, 1, Empty), 2, Node(Empty, 3, Empty))
    val result = tree.fold(0)((a1, a2, a3) => a1 + a2 + a3)
    assert(result == 6)
  }

  test("fold - sum") {
    val tree: BST[Int] = Node(Node(Empty, 1, Empty), 2, Node(Empty, 3, Empty))
    val result = tree.fold(0)(_ + _ + _)
    assert(result == 6)
  }

  test("fold - product") {
    val tree: BST[Int] = Node(Node(Empty, 1, Empty), 2, Node(Empty, 3, Empty))
    val result = tree.fold(1)(_ * _ * _)
    assert(result == 6)
  }

  test("inorder") {
    val list = List(3, 1, 2)
    val tree = BST.fromList(list)((a, b) => a < b)
    val inorderList = BST.inorder(tree)
    assert(inorderList == List(1, 2, 3))
  }

  test("fromList") {
    val list = List(3, 1, 2)
    val tree = BST.fromList(list)((a, b) => a < b)
    assert(tree == Node(Node(Empty, 1, Empty), 2, Node(Empty, 3, Empty)))
  }

  test("fromList - ascending") {
    val list = List(1, 2, 3)
    val tree = BST.fromList(list)(_ > _)
    assert(tree == Node(Empty, 3, Node(Empty, 2, Node(Empty, 1, Empty))))
  }

  test("fromList - descending") {
    val list = List(3, 2, 1)
    val tree = BST.fromList(list)(_ < _)
    assert(tree == Node(Empty, 1, Node(Empty, 2, Node(Empty, 3, Empty))))
  }

  test("fromList - random") {
    val list = List(5, 2, 8, 1, 9, 6, 3)
    val tree = BST.fromList(list)(_ < _)
    assert(tree == Node(Node(Empty, 1, Node(Empty, 2, Empty)), 3, Node(Node(Empty, 5, Empty), 6, Node(Node(Empty, 8, Empty), 9, Empty))))
  }

  test("inorder - empty tree") {
    val tree: BST[Int] = Empty
    val inorderList = BST.inorder(tree)
    assert(inorderList.isEmpty)
  }

  test("inorder - non-empty tree") {
    val tree: BST[Int] = Node(Node(Empty, 1, Empty), 2, Node(Empty, 3, Empty))
    val inorderList = BST.inorder(tree)
    assert(inorderList == List(1, 2, 3))
  }

}
