package cat.udl.eps.is

import munit.FunSuite

import List.*
class ListSuite extends FunSuite {

  test("sort") {
    val l = Cons(3, Cons(1, Cons(2, Nil)))
    val result = Cons(3, Cons(2, Cons(1, Nil)))
    assertEquals(sort(l)(_ >= _), result)
  }

  test("sort-1") {
    val l = List(10, 7, 2, 3, 1, 4, 6, 5, 3, 2)
    assertEquals(sort(l)(_ <= _), List(1, 2, 2, 3, 3, 4, 5, 6, 7, 10))
    assertEquals(sort(l)(_ >= _), List(10, 7, 6, 5, 4, 3, 3, 2, 2, 1))
  }

  test("checkSorted") {
    val sorted = List(1, 2, 3, 4, 5)
    val unsorted = List(1, 2, 4, 3, 5)
    assert(checkSorted(sorted)(_ <= _))
    assert((!checkSorted(unsorted)(_ <= _)))
  }

  test("checkSorted-1") {
    val sorted = List(1, 2, 3, 4, 5)
    val unsorted = List(1, 2, 4, 3, 5)
    assert(checkSorted(sorted)(_ <= _))
    assert((!checkSorted(unsorted)(_ <= _)))
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

  test("digitsToNumFold empty") {
    val l = List()
    assertEquals(digitsToNum(l), 0)
  }

  test("digitsToNumFold") {
    val l = List(1, 2, 3, 4)
    assertEquals(digitsToNumFold(l), 1234)
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

  test("find") {
    val l = List(1, 2, 3, 4, 5)
    assertEquals(find(l)(_ % 2 == 0), Some(2)) // 2 is the first even number in the list
    assertEquals(find(l)(_ > 5), None) // there is no element greater than 5 in the list
  }


  test("mergeSorted") {
    val l1 = List(1, 3, 5)
    val l2 = List(2, 4, 6)
    val l2_extended = List(2, 4, 6, 7, 8,9,10,11)
    val l3 = List(2)
    val l4 = List(1,2,2,3)
    val l5 = List(3)
    val l6 = List(1, 2, 5, 7)
    assertEquals(mergeSorted(l1, Nil)(_ <= _), List(1,3,5))
    assertEquals(mergeSorted(Nil, l2)(_ <= _), List(2,4,6))
    assertEquals(mergeSorted(Nil, l2)(_ <= _), List(2,4,6))
    assertEquals(mergeSorted(l1, l2)(_ <= _), List(1, 2, 3, 4, 5, 6))
    assertEquals(mergeSorted(l3, l4)(_ <= _), List(1,2,2,2,3))
    assertEquals(mergeSorted(l5, l6)(_ <= _), List(1,2,3,5,7))
  }

}
