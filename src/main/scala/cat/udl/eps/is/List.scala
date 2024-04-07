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

  /**
   * Donada una llista de A, retorna una tupla amb dues llistes, la primera les que compleixen la condició i la segona les que no implementat amb foldLeft
   * @param l la llista a la que volem partir
   * @param p la funció que ens diu si un element ha de ser a la llista de l'esquerra o a la de la dreta
   * @tparam A el tipus de la llista
   * @return una tupla amb dues llistes, la primera amb els elements que compleixen la condició i la segona amb els que no
   */
  def partitionFoldLeft[A](l: List[A])(p: A => Boolean): (List[A], List[A]) = {
    (foldLeft(reverse(l),  Nil: List[A]) { (acc, elem) => /** per la particio de l'esquerra, girem la llista ja per obtindre el resultat ordenat  */
      if p(elem) then Cons(elem, acc) /** si compleix la condició, l'afegim a la llista sino retornem l'acumulador */
      else acc
    },
      foldLeft(reverse(l), Nil: List[A]){ (acc, elem) => /** per la particio de la dreta, girem la llista ja per obtindre el resultat ordenat  */
        if !p(elem) then Cons(elem, acc) /** si no compleix la condició, l'afegim a la llista sino retornem l'acumulador */
        else acc
      })
  }

  /**
   * Donada una llista de A, retorna una tupla amb dues llistes, la primera les que compleixen la condició i la segona les que no implementat amb foldRight, l'unic que canvia de l'antrior implementació es que les llistes no els hem d'invertir, ja que ara començem per la dreta i pel primer element de la llista.
   * @param l la llista a la que volem partir
   * @param p la funció que ens diu si un element ha de ser a la llista de l'esquerra o a la de la dreta
   * @tparam A el tipus de la llista
   * @return una tupla amb dues llistes, la primera amb els elements que compleixen la condició i la segona amb els que no
   */
  def partitionFoldRight[A](l: List[A])(p: A => Boolean): (List[A], List[A]) = {
    (foldRight(l, Nil: List[A]) { (elem, acc) =>
      if p(elem) then Cons(elem, acc)
      else acc
    },
      foldRight(l, Nil: List[A]) { (elem, acc) =>
        if !p(elem) then Cons(elem, acc)
        else acc
      })
  }

  /**
   * Donada una llista de A, retorna una tupla amb dues llistes, la primera les que compleixen la condició i la segona les que no implementat amb una funció auxiliar i recursivitat
   * @param l la llista a la que volem partir
   * @param p la funció que ens diu si un element ha de ser a la llista de l'esquerra o a la de la dreta
   * @tparam A el tipus de la llista
   * @return una tupla amb dues llistes, la primera amb els elements que compleixen la condició i la segona amb els que no
   */
  def partitionRecursive[A](l: List[A])(p: A => Boolean): (List[A], List[A]) = {
    def loop(l: List[A], p: A => Boolean): List[A] = { /** una funcio que a partir de una llista i una condició ens retorna una llista amb els elements que compleixen la condició */
      l match
        case Nil => Nil
        case Cons(head, tail) =>
          if p(head) then Cons(head, loop(tail, p)) /** no ens cal passar els elements reversats perque ja els afegim al principi  */
          else loop(tail, p)

    }
    (loop(l, p), loop(l, (a: A) => !p(a))) /** creem la tupla, per la dreta la crida a la funcio loop normal, i per la esquerra, creem una funcio euxiliar nega la que ens passen per paràmetre. */
  }

  /**
   * Donada una llista de A, retorna una tupla amb dues llistes, la primera les que compleixen la condició i la segona les que no implementat amb una funció auxiliar i tail recursive.
   * @param l la llista a la que volem partir
   * @param p la funció que ens diu si un element ha de ser a la llista de l'esquerra o a la de la dreta
   * @tparam A el tipus de la llista
   * @return una tupla amb dues llistes, la primera amb els elements que compleixen la condició i la segona amb els que no
   */
  def partitionTailRecursive[A](l: List[A])(p: A => Boolean): (List[A], List[A]) = {
    @tailrec
    def loop(l: List[A], acc: List[A], p: A => Boolean): List[A] = { /** una funcio que a partir de una llista, una condició i un acumulador ens retorna una llista amb els elements que compleixen la condició */
      l match
        case Nil => acc /** si es la llista buida retornem la llista que em creat amb les crides recursives */
        case Cons(head, tail) =>
          if p(head) then loop(tail, Cons(head, acc), p) /** com que he de ser tailrec, necessitem un acumulador per ficar els elements del final al principi, */
          else loop(tail, acc, p) /** fem crida recursiva i no afegim res al acumulador */
    }
    (loop(reverse(l), Nil, p), loop(reverse(l), Nil, (a: A) => !p(a))) /** creem la tupla, amb la llista reversada, per despres obtindre els resultats ordenats, per la part de l'esquerra igual que abans pero reversat també.  */
  }


  /**
   * Donada una llista de digits, el valor del numero de forma recursiva amb una funcio auxiliar.
   * @param l la llista de digits
   * @return el valor del numero
   */
  def digitsToNumRecursive(l: List[Int]): Int = {
    def loop(acc: Int, l: List[Int], i: Int): Int = { /** una funcio que a partir d'un acumulador, una llista de digits i un index */
      l match
        case Nil => acc /** si som al final de la llista retornem l'enter. */
        case Cons(head, tail) => acc + (head * scala.math.pow(10, i)).toInt + loop(acc, tail, i + 1)
    } /** recorrem la llista, i sumem, l'enter acumulador, i 10 elevat al index actual mes la crida recursiva */
    loop(0, reverse(l), 0) /** cridem a la funcio auxiliar, amb la llista reversada perque començarem pel final, i amb l'index a 0, per obtindre 10^0 = 1 al primer digit. */
  }

  /**
   * Donada una llista de digits, el valor del numero de forma recursiva amb una funcio auxiliar tailrec.
   *
   * @param l la llista de digits
   * @return el valor del numero
   */
  def digitsToNumTailRecursive(l: List[Int]): Int = {
    @tailrec
    def loop(acc: Int, l: List[Int], i: Int): Int = { /** una funcio que a partir d'un acumulador, una llista de digits i un index */
      l match
        case Nil => acc /** si som al final de la llista retornem l'enter. */
        case Cons(head, tail) => loop(acc + (head * scala.math.pow(10, i)).toInt, tail, i + 1) /** a diferència, l'ultim es la crida recursiva, i les sumes les fam a dins de la crdia passant-li al valor al parametre acc */
    }
    loop(0, reverse(l), 0) /** mateixa crida que abans, no canvia. */
  }

  /**
   * Donada una llista de digits, el valor del numero amb un foldLeft.
   *
   * @param l la llista de digits
   * @return el valor del numero
   */
  def digitsToNumFoldLeft(l: List[Int]): Int = {
    foldLeft(reverse(l), (0, 0)) { (acc, elem) => /** passem la llista reversada per obtindre una sortida ordenada, i passem una tupla per contindre l'index i l'acumulador */
      (acc._1 + elem * scala.math.pow(10, acc._2).toInt, acc._2 + 1) /** afegim la potència de 10 amb l'index i incrementem l'index */
    }._1 /** retornem el acc */
  }

  /**
   * Donada una llista de digits, el valor del numero amb un foldRight.
   *
   * @param l la llista de digits
   * @return el valor del numero
   */
  def digitsToNumFoldRight(l: List[Int]): Int = {
    foldRight(l, (0, 0)) { (elem, acc) => /** l'unic que canvia, es que la llista no l'hem de reversar, perque ja començem pel principi */
      (acc._1 + elem * scala.math.pow(10, acc._2).toInt, acc._2 + 1)
    }._1
  }

  /**
   * Donada una llista de digits, el valor del numero amb un foldRight i toInt.
   *
   * @param l la llista de digits
   * @return el valor del numero
   */
  def digitsToNumNoValidFirstImplement(l: List[Int]): Int = {
    if l == Nil then return 0
    /** si la llista es buida retornem 0 */
    foldRight(l, "") { (elem, acc) =>
      /** si no, fem un foldRight per concatenar els elements de la llista a un string */
      elem + acc
      /** concatenem l'element amb l'acumulador */
    }.toInt
    /** retornem el valor del string convertit a enter */
  }

  /** Donada una llista de A's, i una funció que ens marca l'ordre entre dos A's, retorna la llista ordenada amb el mètode de mergesort i una funció auxiliar
   *
   * @param as la llista a ordenar
   * @param ord la funció que ens diu si un element està ordenat sobre un altre.
   * @tparam A el tipus de la llista
   * @return la llista ordenada
   */
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

  /** Donada una llista de A's, i una funció que ens marca l'ordre entre dos A's, retorna la llista ordenada amb el mètode de mergesort i un foldLeft
   *
   * @param as  la llista a ordenar
   * @param ord la funció que ens diu si un element està ordenat sobre un altre.
   * @tparam A el tipus de la llista
   * @return la llista ordenada
   */
  def sortFoldLeft[A](as: List[A])(ord: (A, A) => Boolean): List[A] = {
    foldLeft(as, Nil: List[A])((acc, elem) =>
      mergeSorted(Cons(elem, Nil), acc)(ord))
  }


  /** Donada una llista de A's, i una funció que ens marca l'ordre entre dos A's, retorna la llista ordenada amb el mètode de mergesort i un foldRight
   *
   * @param as  la llista a ordenar
   * @param ord la funció que ens diu si un element està ordenat sobre un altre.
   * @tparam A el tipus de la llista
   * @return la llista ordenada
   */
  def sortFoldRight[A](as: List[A])(ord: (A, A) => Boolean): List[A] = {
    foldRight(as, Nil: List[A])((elem, acc) =>
      mergeSorted(Cons(elem, Nil), acc)(ord))
  }

  /**
   * Donades dues llistes ordenades, les fusiona en una de ordenada amb una funció auxiliar.
   * @param l1 la primera llista
   * @param l2 la segona llista
   * @param ord la funció que ens diu si un element està ordenat sobre un altre.
   * @tparam A el tipus de la llista
   * @return la llista ordenada
   */
  def mergeSorted[A](l1: List[A], l2: List[A])(
    ord: (A, A) => Boolean
  ): List[A] = {
    @tailrec
    def loop(l1: List[A], l2: List[A], acc: List[A]): List[A] =
      (l1,l2) match /** una funcio que a partir de dues llistes i un acumulador ens retorna una llista ordenada */
        case (Cons(head1, tail1), Cons(head2, tail2)) => /** agafem dos elements de la llista */
          if ord(head1, head2) then /** si a través de la funció ord ens retorna cert, afegim a l'acumulaoor el primer element i crida revusriva. */
            loop(tail1, tail2, append(acc, Cons(head1, Cons(head2, Nil))) )
          else /** sino, afegim l'altre element i crida recursiva */
            loop(Cons(head1, tail1), tail2,append(acc, Cons(head2, Nil)) ) /**  */
        case (Nil, Cons(head2, tail2)) => loop(Nil, tail2, append(acc, Cons(head2, Nil))) /** si la llista de la dreta es buida, afegim l'element de la dreta i crida recursiva */
        case (Cons(head1, tail1), Nil) => loop(tail1, Nil, append(acc, Cons(head1, Nil))) /** si la llista de la dreta es buida, afegim l'element de l'esquerra i crida recursiva */
        case (Nil, Nil) => acc /** si les dues llistes son buides retornem l'acumulador */
    loop(l1, l2, List(): List[A])
  }

  /**
   * Donada una llista de A's, una funció que a partir de dos A's que ens diu si està ordenats o no, retorna si està ordenada o no.
   * @param as la llista a comprovar
   * @param ord la funció que ens diu si un element està ordenat sobre un altre.
   * @tparam A el tipus de la llista
   * @return
   */
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

  /**
   * Donada una llista de A's, una funció que retorna un boolean a partir d'un a, retorna el primer element que compleix la condició.
   * @param l la llista a la que volem buscar de A's
   * @param p la funció que ens retorna un booleà a partir d'un A
   * @tparam A el tipus de la llista
   * @return el primer element que compleix la condició en un Some(A) o si no None
   */
  @tailrec
  def find[A](l: List[A])(p: A => Boolean): Option[A] = {
    l match
      case Nil => None
      case Cons(head: A, tail: List[A]) =>
        if p(head) then
          Some(head)
        else find(tail)(p)
  }


  /**
   * Donada una llista de A's, una funció que retorna un Either, retorna una tupla amb dues llistes, la primera amb els elements que són Left i la segona amb els que són Right amb dos foldLeft.
   * @param l la llista a la que volem partir
   * @param p la funció que ens retorna un element de l'esquerra o a la de la dreta a partir d'un A.
   * @tparam A el tipus de la llista
   * @tparam B el tipus de la llista de l'esquerra
   * @tparam C el tipus de la llista de la dreta
   * @return una tupla amb dues llistes, la primera amb els elements que són Left i la segona amb els que són Right
   */
  def partitionMapFoldLeft[A, B, C](l: List[A])(p: A => Either[B, C]): (List[B], List[C]) = {
    (foldLeft(reverse(l), Nil: List[B]) { (acc, elem) => /** reversem la llista perque comencem pel final, i passem un acumulador de tipus List[B] */
      p(elem) match /** si l'element es Left l'afegim a la llista de l'esquerra, sino no fem res i retornem el acc */
        case Left(l) => Cons(l, acc)
        case Right(r) => acc
    },
      foldLeft(reverse(l), Nil: List[C]){ (acc, elem) => /** al reves que abans per discriminar l'altra llista */
        p(elem) match
          case Left(l) => acc
          case Right(r) => Cons(r, acc)
      })
  }

  /**
   * Donada una llista de A's, una funció que retorna un Either, retorna una tupla amb dues llistes, la primera amb els elements que són Left i la segona amb els que són Right amb dos foldRight.
   *
   * @param l la llista a la que volem partir
   * @param p la funció que ens retorna un element de l'esquerra o a la de la dreta a partir d'un A.
   * @tparam A el tipus de la llista
   * @tparam B el tipus de la llista de l'esquerra
   * @tparam C el tipus de la llista de la dreta
   * @return una tupla amb dues llistes, la primera amb els elements que són Left i la segona amb els que són Right
   */
  def partitionMapFoldRight[A, B, C](l: List[A])(p: A => Either[B, C]): (List[B], List[C]) = {
    (foldRight(l, Nil: List[B]) { (elem, acc) => /** el mateix que abans pero sense reversar les llistes inicials. */
      p(elem) match
        case Left(l) => Cons(l, acc)
        case Right(r) => acc
    },
      foldRight(l, Nil: List[C]) { (elem, acc) =>
        p(elem) match
          case Left(l) => acc
          case Right(r) => Cons(r, acc)
      })
  }

  /**
   * A partir d'una llista de digits, el convertirem a un enter amb foldLeft i string
   * @param l la llista de digits
   * @return el valor del numero
   */
  def digitsToNumOptionFoldLeft(l: List[Int]): Option[Int] = {
    foldLeft(l, Some(0)){(acc, elem) => /** passem un acumulador de tipus Option amb Some(0) per poder retornar None si hi ha algun error */
      if (acc.getOrElse(None) == None) then return None /** si l'acumulador es None, retornem None */
      if elem.toString.length > 1 then /** si sol es un digit agafem l'acumulador i el concatenem i el convertim a numero*/
        return None
      else
        Some( acc.getOrElse(None).toString.concat(elem.toString).toInt)
    }
  }

  /**
   * A partir d'una llista de digits, el convertirem a un enter amb foldRight i string.
   *
   * @param l la llista de digits
   * @return el valor del numero
   */
  def digitsToNumOptionFoldRight(l: List[Int]): Option[Int] = {
    foldRight(reverse(l), Some(0)) { (elem, acc) => /** passem la llista reversada perque comencem pel final, i un acumulador de tipus Option amb Some(0) per poder retornar None si hi ha algun error */
      if (acc.getOrElse(None) == None) then return None
      if elem.toString.length > 1 then
        return None
      else /** si sol es un digit agafem l'acumulador i el concatenem i el convertim a numero */
        Some(acc.getOrElse(None).toString.concat(elem.toString).toInt)
    }
  }

  /**
   * A partir d'una llista de digits, el convertirem a un enter amb foldLeft i Either.
   *
   * @param l la llista de digits
   * @return el valor del numero a la dreta, o a l'esquerra si hi ha algun error i la posició de l'error.
   */
  def digitsToNumEitherFoldLeft(l: List[Int]): Either[Int, Int] = {
    foldLeft(l, Right(0) :Either[Int, Int]) { (acc, elem) =>
      acc match
        case Left(v) => acc /** si l'acumulador es erroni (left) el retornem */
        case Right(value) =>
          if (elem.toString.length > 1) then /** si l'element no es un digit retornem error (left) amb valor de l'element */
            Left(elem)
          else /** si sol es un digit agafem l'acumulador i el concatenem i el convertim a numero */
            Right(acc.getOrElse(0).toString.concat(elem.toString).toInt)
    }
  }

  /**
   * A partir d'una llista de digits, el convertirem a un enter amb foldRight i Either.
   *
   * @param l la llista de digits
   * @return el valor del numero a la dreta, o a l'esquerra si hi ha algun error i la posició de l'error.
   */
  def digitsToNumEitherFoldRight(l: List[Int]): Either[Int, Int] = {
    foldRight(reverse(l), Right(0): Either[Int, Int]) { (elem, acc) =>
      acc match
        case Left(v) => acc /** si l'acumulador es erroni (left) el retornem */
        case Right(value) =>
          if (elem.toString.length > 1) then /** si l'element no es un digit retornem error (left) amb valor de l'element */
            Left(elem)
          else /** si no, afegim el digit al acumulador convertint amb int -> string -> int */
            Right(acc.getOrElse(0).toString.concat(elem.toString).toInt)
    }
  }
}
