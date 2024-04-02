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

  test("test russia") {
    val n: Int = 15
    val votes: Map[String, Int] =
      Map("putin" -> 1)
    val result =
      Map("putin" -> 15)

    assertEquals(Hondt.hondt(votes, n), result)
  }

  test("test ager") {
    val n: Int = 7
    val votes: Map[String, Int] =
      Map("CM-Junts" -> 219,
        "IXA-Erc" -> 119,
        "AXA-Nitupa" -> 43)
    val result =
      Map("CM-Junts" -> 5,
        "IXA-Erc" -> 2)

    assertEquals(Hondt.hondt(votes, n), result)
  }
}
