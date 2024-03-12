package cat.udl.eps.is

// Exercises in user defined lists

enum List[+A] {
  case Nil
  case Cons(head: A, tail: List[A])
}

object List {

  def apply[A](as: A*): List[A] = {
    // This foldRight is the predefined one on Seq
    // This code does the same as the one in the fpinscala repo
    as.foldRight(Nil: List[A])(Cons.apply)
  }

  @annotation.tailrec
  def foldLeft[A, B](l: List[A], acc: B)(f: (B, A) => B): B = {
    l match
      case Nil         => acc
      case Cons(x, xs) => foldLeft(xs, f(acc, x))(f)
  }

  // If we put a function in a single parameter list we can define the function
  // with {} instead of ()
  def reverse[A](l: List[A]): List[A] = {
    foldLeft(l, Nil: List[A]) { (acc, x) =>
      Cons(x, acc)
    }
  }

  // This version of foldRight is stack save
  def foldRight[A, B](l: List[A], acc: B)(f: (A, B) => B): B = {
    foldLeft(reverse(l), acc) { (acc, x) =>
      f(x, acc)
    }
  }

  def append[A](l: List[A], r: List[A]): List[A] = {
    foldRight(l, r)(Cons.apply)
  }

  def concat[A](l: List[List[A]]): List[A] = {
    foldRight(l, Nil: List[A])(append)
  }

  // ------------------------------------------------------------------

  def partition[A](l: List[A])(p: A => Boolean): (List[A], List[A]) = {
    ???
  }
  
  def digitsToNum(l: List[Int]): Int = {
    ???
  }
  
  def sort[A](as: List[A])(ord: (A, A) => Boolean): List[A] = {
    ???
  }
  
  def mergeSorted[A](l1: List[A], l2: List[A])(
      ord: (A, A) => Boolean
  ): List[A] = {
    ???
  }

  def checkSorted[A](as: List[A])(ord: (A, A) => Boolean): Boolean = {
    ???
  }

  def find[A](l: List[A])(p: A => Boolean): Option[A] = {
    ???
  }

  def partitionMap[A, B, C](
      l: List[A]
  )(p: A => Either[B, C]): (List[B], List[C]) = {
    ???
  }

  def digitsToNumOption(l: List[Int]): Option[Int] = {
    ???
  }

  def digitsToNumEither(l: List[Int]): Either[Int, Int] = {
    ???
  }
}
