package cat.udl.eps.is

import scala.List
import scala.annotation.tailrec

enum BST[+A] {
  case Empty
  case Node(left: BST[A], value: A, right: BST[A])

  def insert[B >: A](a: B)(ordered: (B, B) => Boolean): BST[B] = {
    this match
      case Node(left, value, right) =>
        if ordered(a, value) then
          Node(left.insert(a)(ordered), value, right)
        else
          Node(left, value, right.insert(a)(ordered))
      case Empty => Node(Empty, a, Empty)

  }

  def find[B >: A](a: B)(ord: (B, B) => Boolean): Boolean = {
    this match
      case Node(left, value, right) =>
        if ord(a, value) then
          true
        else
          left.find(a)(ord) || right.find(a)(ord)
      case Empty => false
  }

  /*def fold[B](b: B)(f: (B, A, B) => B): B = {
    b match
      case Node(left, value, right) =>
        var b
      case Empty => _
  }*/
  def fold[B](b: B)(f: (B, A, B) => B): B = this match {
    case Empty => b
    case Node(left, value, right) =>
      f(left.fold(b)(f), value, right.fold(b)(f))
  }
}

object BST {

  def fromList[A](l: List[A])(lt: (A, A) => Boolean): BST[A] = {
    l match
      case head :: tail =>
        fromList(tail)(lt).insert(head)(lt)
      case head :: Nil => Node(Empty, head, Empty)
      case _ => Empty
  }

  def inorder[A](t: BST[A]): List[A] = {
    t match
      case Node(left, value, right) =>
        inorder(left).appended(value).appendedAll(inorder(right))
      case _ => List()
  }
}
