package cat.udl.eps.is

import scala.List
import scala.annotation.tailrec

enum BST[+A] {
  case Empty
  case Node(left: BST[A], value: A, right: BST[A])


  /**
   * Insereix un element a l'arbre binari de cerca (BST), mantenint l'ordre especificat pel paràmetre 'ordered'.
   *
   *
   * @param a l'element a inserir a l'arbre.
   * @param ordered una funció que determina l'ordre dels elements. Ha de rebre dos elements de tipus B i retornar un booleà indicant si estan en ordre.
   * @tparam B el tipus dels elements de l'arbre binari de cerca.
   * @return un nou BST amb l'element inserit.
   */
  def insert[B >: A](a: B)(ordered: (B, B) => Boolean): BST[B] = {
    this match
      case Node(left, value, right) => /** Agafem el primer node del BST */
        if ordered(a, value) then /** si està ordenat, a s'ha d'inserir per l'esquerra  */
          Node(left.insert(a)(ordered), value, right)
        else /** si no està ordenat, a s'ha d'inserir per la dreta  */
          Node(left, value, right.insert(a)(ordered))
      case Empty => Node(Empty, a, Empty) /** Si ens trobem en una fulla, creem el nou node amb l'element */

  }


  /**
   * Busca un element a l'arbre binari de cerca (BST), mantenint l'ordre especificat pel paràmetre 'ord'.
   *
   * @param a l'element a buscar a l'arbre.
   * @param ord una funció que determina l'ordre dels elements. Ha de rebre dos elements de tipus B i retornar un booleà indicant si estan en ordre.
   * @tparam B el tipus dels elements de l'arbre binari de cerca.
   * @return un booleà indicant si l'element a s'ha trobat a l'arbre.
   */
  def find[B >: A](a: B)(ord: (B, B) => Boolean): Boolean = {
    this match
      case Node(left, value, right) => /** Agafem el primer node del BST */
        if a == value then  /** si l'element a és igual al node actual, retornem true */
          true
        else if ord(a, value) then /** si l'element està ordenat, busquem a l'esquerra */
          left.find(a)(ord)
        else /** si l'element no està ordenat, busquem a la dreta */
          right.find(a)(ord)
      case Empty => false /** Si arribem a una fulla, retornem false */
  }

  /**
   * Recorrem l'arbre binari de cerca (BST) en preordre i apliquem una funció a cada Node
   *
   * @param b l'arbre binari de cerca (BST) a recórrer.
   * @param f la funció a aplicar a cada Node.
   * @tparam B el tipus dels elements de l'arbre binari de cerca.
   * @return un nou BST amb els elements modificats per la funció f.
   */
  def fold[B](b: B)(f: (B, A, B) => B): B = this match {
    case Empty => b /** Si arribem a una fulla, retornem el valor b */
    case Node(left, value, right) => /** Agafem el primer node del BST */
      f(left.fold(b)(f), value, right.fold(b)(f)) /** Apliquem la funció f a l'esquerra, el valor i la dreta */
  }
}

object BST {

  /**
   * Crea un BST a partir d'una llista d'elements, mantenint l'ordre especificat pel paràmetre 'lt'.
   *
   * @param l la llista d'elements a inserir a l'arbre.
   * @param lt una funció que determina l'ordre dels elements. Ha de rebre dos elements de tipus A i retornar un booleà indicant si estan en ordre.
   * @tparam A el tipus dels elements de l'arbre binari de cerca.
   * @return un nou BST amb els elements de la llista.
   */
  def fromList[A](l: List[A])(lt: (A, A) => Boolean): BST[A] = {
    l match
      case head :: tail =>
        fromList(tail)(lt).insert(head)(lt) /** Inserim el primer element de la llista a l'arbre */
      case head :: Nil => Node(Empty, head, Empty) /** Si només queda un element, creem el node amb l'element */
      case _ => Empty /** Si no hi ha elements, retornem un arbre buit */
  }

  /**
   * Retorna una llista amb els elements de l'arbre binari de cerca (BST) en ordre. (primer esquerra, després arrel, finalment dreta)
   *
   * @param t l'arbre binari de cerca (BST) a transformar.
   * @tparam A el tipus dels elements de l'arbre binari de cerca.
   * @return una llista amb els elements de l'arbre en ordre.
   */
  def inorder[A](t: BST[A]): List[A] = {
    t match
      case Node(left, value, right) =>
        inorder(left).appended(value).appendedAll(inorder(right)) /** Recorrem l'arbre en ordre d'esquerra, arrel i dreta retornant la llista de toes les sortides. */
      case _ => List() /** Si arribem a una fulla, retornem una llista buida */
  }
}
