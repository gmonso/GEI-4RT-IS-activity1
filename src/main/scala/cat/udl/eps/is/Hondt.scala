package cat.udl.eps.is

import scala.List

// Eleccions Parlament Catalunya 2021 (Lleida)
// https://www.parlament.cat/pcat/parlament/que-es-el-parlament/procediment-electoral/
// https://gencat.cat/eleccions/resultatsparlament2021/resultados/3/catalunya/lleida

// Consulteu la llista de mètodes a StdLib

object Hondt {

  // El paràmetres d'entrada són
  // - els vots que ha rebut cada partit (només els que han passat el tall del 5%)
  // - el nombre d'escons a repartir
  // La sortida ha de ser, per cada partit, el nombre d'escons que li pertoquen
  // Per simplificar, representem les candidatures per Strings.

  def hondt(votes: Map[String, Int], seats: Int): Map[String, Int] = {

    // Primer de tot, si mirem la informació sobre el procediment electoral (concretament
    // a la secció el resultat electroral) veurem que haurem de construïr una matriu on cada fila
    // representa els vots d'un partir, dividits per diferents divisors.
    // Fixeuvos que al ser una funció interna, puc accedir al valor de n
    // La primera funció ens retorna, donat un número de vots, els valors que hi ha a la fila:// donats els vots d'una candidatura, obtenik la llista de tots els quocients
    // nvotes/1, nvotes/2, nvotes/3, ..., nvots/seats

    // Per evitar problemes d'arrodoniments, hem de realitzar la divisió amb doubles.
    def quotients(nvotes: Int): List[Double] = {
      (1 to seats).foldLeft(List())((acc: List[Double], elem) => {  // recorrem els seients del 1 al seats i creem una llista de Double de la divisió del element del loop amb el num de vots passat per paràmetre.
        acc.appended( nvotes.toDouble / elem.toDouble)
      })
    }

    // Aquesta la podem fer genèrica: tenim una parella formada per un A i una llista de Bs
    // L'objectiu final serà marcar cadascun dels elements de la taula que he comentat abans amb
    // el partit que representen ja que així, si ordeno la llista resultant, podré trobar els quocients
    // més grans i saber el partit que representen

    def distribute[A, B](pr: (A, List[B])): List[(A, B)] = {
      pr._2.map((pr2) => (pr._1, pr2))
    }

    // Transformarem el map inicial dels vots en un map que, per cada partit
    // contindrà la llista dels quocients.
    val quotientsByParty: Map[String, List[Double]] = {
      votes.map((partit, vots) => {
        (partit, quotients(vots))
      })
    }

    // Ara convertirem el Map anterior en una llista de parelles que contindrà
    // tots els valors de la taula (els quocients) amb el partit a que es corresponen.
    val allQuotients: List[(String, Double)] = {
      /*quotientsByParty.foldLeft(List())((acc, elem) => {
        elem._2.foldLeft(List())((acc2, elem2)=>{
          acc.appended((elem._1, elem2))
        })
      })*/

      quotientsByParty.foldLeft(List(): List[(String, Double)])((acc, elem) => {
        acc.appendedAll(distribute(elem))
      })
    }

    // Una vegada tenim tots els quocients, els ordenem en ordre decreixent
    // Pista: use sortBy i passeu una funció que, donada una parella retorna el
    // valor a considerar (com voleu ordenació decreixent. canvieu-li el signe)
    val sortedQuotients: List[(String, Double)] = {
      allQuotients.sortBy((s) => (s._2)).reverse
    }

    // Agafem tants elements de la llista anterior com escons a repartir
    val selected: List[(String, Double)] = {
      sortedQuotients.take(seats)
    }

    // I, finalment, comptem els partits a que corresponen aquests escons
    selected.groupMapReduce((partit, coeficient) => partit)(_ => 1)(_ + _)
  }
}
