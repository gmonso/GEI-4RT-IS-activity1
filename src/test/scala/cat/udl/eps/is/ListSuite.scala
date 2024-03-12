package cat.udl.eps.is

import munit.FunSuite
import List.*

class ListSuite extends FunSuite {

  test("sort") {
    val l = Cons(3, Cons(1, Cons(2, Nil)))
    val result = Cons(3, Cons(2, Cons(1, Nil)))
    assertEquals(sort(l)(_ >= _), result)
  }
  
  test("partition") {
    val l = List(11, 22, 33, 44, 55)
    val (odds, evens) = partition(l)(_ % 2 == 1)
    assertEquals(odds, List(11, 33, 55))
    assertEquals(evens, List(22, 44))
  }
  
  test("digitsToNum") {
    val l = List(1,2,3,4)
    assertEquals(digitsToNum(l), 1234)
  }
  
  test("sort") {
    val l = List(10, 7, 2, 3, 1, 4, 6, 5, 3, 2)
    assertEquals(sort(l)(_ <= _), List(1, 2, 2, 3, 3, 4, 5, 6, 7, 10))
    assertEquals(sort(l)(_ >= _), List(10, 7, 6, 5, 4, 3, 3, 2, 2, 1))
  }
  
  test("checkSorted") {
    val sorted = List(1, 2, 3, 4, 5)
    val unsorted = List(1, 2, 4, 3, 5)
    assert(checkSorted(sorted)(_ <= _))
    assert(!checkSorted(unsorted)(_ <= _))
  }
  
  test("digitsToNumOption") {
    val num = List(1, 2, 3, 4)
    val noNum = List(1, 20, 3, 4)
    assertEquals(digitsToNumOption(num), Some(1234))
    assertEquals(digitsToNumOption(noNum), None)
  }

  test("digitsToNumEither") {
    val num = List(1, 2, 3, 4)
    val noNum = List(1, 20, 3, 4)
    assertEquals(digitsToNumEither(num), Right(1234))
    assertEquals(digitsToNumEither(noNum), Left(20))
  }
}
