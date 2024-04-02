package cat.udl.eps.is

import scala.annotation.tailrec
import scala.language.postfixOps

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
    (foldLeft(reverse(l),  Nil: List[A]) { (acc, elem) =>
      if p(elem) then Cons(elem, acc)
      else acc
    },
      foldLeft(reverse(l), Nil: List[A]){ (acc, elem) =>
        if !p(elem) then Cons(elem, acc)
        else acc
      })
  }


  // de forma numerica i sense fold
  def digitsToNum(l: List[Int]): Int = {
    def loop(acc: Int, l: List[Int], i: Int): Int = {
      l match
        case Nil => acc
        case Cons(head, tail) => acc + (head * scala.math.pow(10, i)).toInt + loop(acc, tail, i+1)
    }
    loop(0, reverse(l), 0)
  }

  def digitsToNumFold(l: List[Int]): Int = {
    foldLeft(reverse(l), (0, 0)) { (acc, elem) =>
      (acc._1 + elem * scala.math.pow(10, acc._2).toInt, acc._2 + 1)
    }._1
  }

  // amb fold left
  def digitsToNumNoValid(l: List[Int]): Int = {
    foldLeft(l,""){ (acc, elem) =>
      acc + elem
    }.toInt
  }

  // sort una mica liat
  def sort1[A](as: List[A])(ord: (A, A) => Boolean): List[A] = {
    @tailrec
    def loop(l: List[A], acc: List [A], mainHead: A|Boolean): List[A] = {
      mainHead match
        case false =>
          l match
            case Cons(head1: A, Cons(head2: A, tail2)) =>
              if ord(head1, head2) then
                loop(Cons(head2, tail2), append(acc, Cons(head1, Nil)), false)
              else
                loop(tail2, append(acc, Cons(head2, Nil)), head1)
            case Cons(head: A, tail) =>
              loop(tail, append(acc, Cons(head, Nil)), false)
            case _ => acc
        case mainHead:A =>
          l match
            case Cons(head: A, tail) =>
              if ord( mainHead, head) then
                loop(tail, append(acc, Cons(mainHead, Nil)), head)
              else
                loop(tail, append(acc, Cons(head, Nil)), mainHead)
            case Nil => append(acc, Cons(mainHead, Nil))
        case _ => acc
    }
    loop(as, Nil, false)
  }

  // el sort definitiu
  def sort11[A](as: List[A])(ord: (A, A) => Boolean): List[A] = {
    @tailrec
    def loop(l: List[A], acc: List[A]): List[A] = {
      l match
        case Cons(head1: A, Cons(head2: A, tail)) =>
          if ord(head1, head2) then
            loop(Cons(head2, tail), mergeSorted(acc, Cons(head1, Nil))(ord))
          else
            loop(Cons(head1, tail), mergeSorted(acc, Cons(head2, Nil))(ord))
        case Cons(head: A, Nil) =>
          mergeSorted(acc, Cons(head, Nil))(ord)
        case _ => acc
    }
    loop(as, Nil)
  }

  def sort[A](as: List[A])(ord: (A, A) => Boolean): List[A] = {
    @tailrec
    def loop(l: List[A], acc: List[A]): List[A] = {
      l match
        case Cons(head: A, tail) =>
            loop(tail, mergeSorted(Cons(head, Nil), acc)(ord))
        case Cons(head: A, Nil) =>
            mergeSorted(Cons(head, Nil), acc)(ord)
        case _ => acc
    }
    loop(as, Nil)
  }

  def mergeSorted[A](l1: List[A], l2: List[A])(
    ord: (A, A) => Boolean
  ): List[A] = {
    @tailrec
    def loop(l1: List[A], l2: List[A], acc: List[A]): List[A] =
      (l1,l2) match
        case (Cons(head1, tail1), Cons(head2, tail2)) =>
          if ord(head1, head2) then
            loop(tail1, tail2, append(acc, Cons(head1, Cons(head2, Nil))) )
          else
            loop(Cons(head1, tail1), tail2,append(acc, Cons(head2, Nil)) )
        case (Nil, Cons(head2, tail2)) => loop(Nil, tail2, append(acc, Cons(head2, Nil)))
        case (Cons(head1, tail1), Nil) => loop(tail1, Nil, append(acc, Cons(head1, Nil)))
        case (Nil, Nil) => acc
    loop(l1, l2, List(): List[A])
  }

  def checkSorted[A](as: List[A])(ord: (A, A) => Boolean): Boolean = {
    @tailrec
    def loop(as: List[A], isSorted: Boolean): Boolean =
      if (!isSorted) return false
      as match
        case Cons(head1, Cons(head2, tail2)) =>
          loop(Cons(head2, tail2), ord(head1, head2))
        case Cons(head, tail) =>
          true
    loop(as, true)
  }

  @tailrec
  def find[A](l: List[A])(p: A => Boolean): Option[A] = {
    l match
      case Nil => None
      case Cons(head: A, tail: List[A]) =>
        if p(head) then
          Some(head)
        else find(tail)(p)
  }



  def partitionMap[A, B, C](l: List[A])(p: A => Either[B, C]): (List[B], List[C]) = {
    (foldLeft(reverse(l), Nil: List[B]) { (acc, elem) =>
      p(elem) match
        case Left(l) => Cons(l, acc)
        case Right(r) => acc
    },
      foldLeft(reverse(l), Nil: List[C]){ (acc, elem) =>
        p(elem) match
          case Left(l) => acc
          case Right(r) => Cons(r, acc)
      })
  }

  def digitsToNumOption(l: List[Int]): Option[Int] = {
    foldLeft(l, Some(0)){(acc, elem) =>
      if (acc.getOrElse(None) == None) then return None
      if elem.toString.length > 1 then
        return None
      else
        Some( acc.getOrElse(None).toString.concat(elem.toString).toInt)
    }
  }

  def digitsToNumEither(l: List[Int]): Either[Int, Int] = {
    foldLeft(l, Right(0) :Either[Int, Int]) { (acc, elem) =>
      acc match
        case Left(v) => acc
        case Right(value) =>
          if (elem.toString.length > 1) then
            Left(elem)
          else
            Right(acc.getOrElse(0).toString.concat(elem.toString).toInt)
    }
  }
}
