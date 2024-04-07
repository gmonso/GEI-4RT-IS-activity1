package cat.udl.eps.is

import scala.List

/*
  - (1 to 5) = 1, 2, 3, 4, 5
  - (1 until 5) = 1, 2, 3, 4
  - Iterator
    def iterate[A](a: A)(f: A -> A): Iterator[A]
  - Iterable[A]
    def toList: List[A]
  - List[Int]
    def sum: List[Int]
    def prod: List[Int]
    def sortBy[B](f: A => B)(given ord: Ordering[B]): List[A]
  - List[List[A]]
    def flatten: List[A]
  - List[A]
    def head: A
    def tail: List[A]
    def tails: Iterator[List[A]]
    def map[B](f: A => B): List[B]
    def flatMap[B](f: A => List[B]): List[B]
    def filter(p: A => Boolean): List[A]
    def forall(p: A => Boolean): Boolean
    def exist(p: A => Boolean): Boolean
    def take(n: Int): List[A]
    def drop(n: Int): List[A]
    def takeWhile(p: A => Boolean): List[A]
    def dropWhile(p: A => Boolean): List[A]
    def groupBy[K](f: A => K): Map[K, List[A]]
    def groupMapReduce[K, B](key: A => K)(f: A => B)(reduce: (B, B) => B): Map[K, B]
  - Map[K, V]
    def map[K2, V2](f: ((K, V)) => (K2, V2)): Map[K2, V2]
    def values: Iterable[V]
  - (A, B) // Tuple
     t._1 = t(0) = A
     t._2 = t(1) = B
 */

object StdLib {

  /**
   * A partir d'una llista de paraules, retorna un mapa on la clau es le numero de lletres i el valor es la quantitat de paraules que tenen aquesta longitud.
   * @param words Llista de paraules
   * @return Mapa amb la longitud de les paraules com a clau i la quantitat de paraules com a valor
   */
  def countLengths(words: List[String]): Map[Int, Int] = { /** a partir de la llista, fem un groupMapReduce, la clau serà la llargada de cada paraula, per cada paraula nomes incrementem 1, i a partir de dos elements del mapa, els sumem. */
    words.groupMapReduce(_.length)((key) => 1)((count1, count2) => count1 + count2)
  }

  /**
   * A partir d'un enter b, fem la seva potència n vegades amb una funcio auxiliar.
   * @param b enter base
   * @param n enter exponent
   * @return sumatori de les potències de b des de 0 fins a n-1
   */
  def sumFirstPowersOf(b: Int)(n: Int): Int = {
    def pow(b:Int, n: Int): Int = { /** Aquesta funcio retorna el resultat d'elevar b elevat a n */
      if (n-1 >= 0) {
        pow(b, n - 1) * b
      } else {
        1
      }
    }
    if n-1 >= 0 then
      sumFirstPowersOf(b)(n-1) + pow(b, n-1) /** Cridem a la funció ppal amb n-1 per fer totes les potències i sumem el valor de la n actual. */
    else
      0 /** Si ja em fet tot el recorregut, retornem 0 */
  }
}
