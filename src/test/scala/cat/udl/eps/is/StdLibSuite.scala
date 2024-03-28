package cat.udl.eps.is

import munit.FunSuite

import scala.List
import StdLib.*

import scala.collection.immutable.List

class StdLibSuite extends FunSuite {

  test("sumFirstPowersOf") {
    assert(sumFirstPowersOf(2)(3) == 7) // 2^0 + 2^1 + 2^2 = 1 + 2 + 4 = 7
    assert(sumFirstPowersOf(3)(4) == 40) // 3^0 + 3^1 + 3^2 + 3^3 = 1 + 3 + 9 + 27 = 40
    assert(sumFirstPowersOf(5)(1) == 1) // 5^0 = 1
  }


  test("countLengths") {
    assert(countLengths(List("12345", "123456", "1234", "1234")) == Map(4 -> 2, 5 -> 1, 6 -> 1))
    assert(countLengths(List("1", "12", "123", "1234")) == Map(1 -> 1, 2 -> 1, 3 -> 1, 4 -> 1))
    assert(countLengths(List()) == Map())
  }

}
