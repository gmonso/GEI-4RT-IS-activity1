package cat.udl.eps.is

import scala.List

enum BST[+A] {
  case Empty
  case Node(left: BST[A], value: A, right: BST[A])

  def insert[B >: A](a: B)(ordered: (B, B) => Boolean): BST[B] = {
    ???
  }

  def find[B >: A](a: B)(ord: (B, B) => Boolean): Boolean = {
    ???
  }

  def fold[B](b: B)(f: (B, A, B) => B): B = {
    ???
  }
}

object BST {

  def fromList[A](l: List[A])(lt: (A, A) => Boolean): BST[A] = {
    ???
  }

  def inorder[A](t: BST[A]): List[A] = {
    ???
  }
}
