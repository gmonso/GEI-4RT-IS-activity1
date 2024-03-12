package cat.udl.eps.is

import munit.FunSuite

class HondtSuite extends FunSuite {

  test("lleida") {
    val n: Int = 15
    val votes: Map[String, Int] =
      Map("JxCat" -> 45_029,
        "ERC" -> 42_670,
        "PSC" -> 24_115,
        "CUP-G" -> 11_871,
        "VOX" -> 8_876,
        "PDeCAT" -> 7_365,
        "PP" -> 5_189,
        "Cs" -> 5_175)
    val result =
      Map("CUP-G" -> 1,
        "JxCat" -> 5,
        "VOX" -> 1,
        "ERC" -> 5,
        "PSC" -> 3)
      
    assertEquals(Hondt.hondt(votes, n), result)
  }
}
