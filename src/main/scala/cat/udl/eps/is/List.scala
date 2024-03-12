package cat.udl.eps.is

import scala.annotation.tailrec

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

  // amb fold left
  def digitsToNum(l: List[Int]): Int = {
    foldLeft(l,""){ (acc, elem) =>
      acc + elem
    }.toInt
  }

  // de forma numerica i sense fold
  def digitsToNum1(l: List[Int]): Int = {
    def loop(acc: Int, l: List[Int], i: Int): Int = {
      l match
        case Nil => acc
        case Cons(head, tail) => acc + (head * scala.math.pow(10, i)).toInt + loop(acc, tail, i+1)
    }
    loop(0, reverse(l), 0)
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
  def sort[A](as: List[A])(ord: (A, A) => Boolean): List[A] = {
    @tailrec
    def loop(l: List[A], acc: List[A]): List[A] = {
      l match
        case Cons(head1: A, Cons(head2: A, tail2)) =>
          if ord(head1, head2) then
            loop(Cons(head2, tail2), append(acc, Cons(head1, Nil)))
          else
            loop(Cons(head1, tail2), append(acc, Cons(head2, Nil)))
        case Cons(head: A, Nil) =>
          append(acc, Cons(head, Nil))
        case _ => acc
    }
    loop(as, Nil)
  }

  /*def sort[A](as: List[A])(ord: (A, A) => Boolean): List[A] = {
    def loop(l: List[A], acc: List [A], mainHead: A|Boolean): List[A] = {
      mainHead match
        case Nil => {
          l match
            case Cons(head1: A, Cons(head2: A, tail2)) =>
              if ord(head1, head2) then
                append(acc, Cons(head1, Nil))
              else
                Cons(head2, Cons(head1, tail2))
        }
        case Cons(head,tail) => ???
    }
    loop(as, (), false)
  }*/

  /*def sort1[A](as: List[A])(ord: (A, A) => Boolean): List[A] = {
    foldLeft(as, ()){ (acc, elem) =>
      elem match
        //case Cons(head, Nil) => Cons(head, Nil)
        case Cons(head1: A, Cons(head2: A, tail2)) =>
          if ord(head1, head2) then
            append(acc, Cons(head1, tail1))
          else
            append(acc, Cons(head1, tail1))

    }
  }*/

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
            loop(tail1, tail2, append(acc, Cons(head2, Cons(head1, Nil))) )
        case (Nil, Cons(head2, tail2)) => loop(Nil, tail2, append(acc, Cons(head2, Nil)))
        case (Cons(head1, tail1), Nil) => loop(tail1, Nil, append(acc, Cons(head1, Nil)))
        case (Nil, Nil) => acc
    loop(l1, l2, List(): List[A])
  }

  def checkSorted[A](as: List[A])(ord: (A, A) => Boolean): Boolean = {
    ???
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

  def partitionMap[A, B, C](
                             l: List[A]
                           )(p: A => Either[B, C]): (List[B], List[C]) = {
    ???
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
    ???
  }
}
