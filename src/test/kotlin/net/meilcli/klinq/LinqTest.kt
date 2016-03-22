package net.meilcli.klinq

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class LinqTest {

    //lazyというよりはdeferredでしたね
    @Test fun lazy() {
        var ar1: Array<Int> = arrayOf(0, 0, 0, 0, 0)
        var ar2: Array<Int> = arrayOf(0, 0, 1, 2, 2)// if not lazy 0,1,2,0,2
        var i: Int = 0
        var list: List<Int> = (0..2).toEnumerable().where { x -> ar1[i++] = x;x % 2 == 0 }.where { x -> ar1[i++] = x;x != 0 }.toList()
        assertArrayEquals(ar1, ar2)
        assertEquals(list.size, 1)
        assertEquals(list[0], 2)
    }

    @Test fun reset() {
        var linq = (0..10).toEnumerable().where { x -> x % 2 == 0 }.where { x -> x % 4 == 0 }
        assertArrayEquals(linq.toArray(), arrayOf(0, 4, 8))
        assertEquals(linq.max(), 8)
    }

    @Test fun array() {
        var ar: Array<Int> = arrayOf(0, 1, 2);
        assertArrayEquals((0..2).toEnumerable().toArray(), ar)
    }

    @Test fun range() {
        assertArrayEquals(Enumerable.range(0, 10).toArray(), (0..9).toEnumerable().toArray())
    }

    @Test fun repeat() {
        assertArrayEquals(Enumerable.repeat(0, 3).toArray(), arrayOf(0, 0, 0))
    }

    @Test fun empty() {
        assertEquals(Enumerable.empty<Int>().count(), 0)
    }

    @Test fun elementAt() {
        assertEquals((0..10).toEnumerable().elementAt(9), 9)
        assertEquals((0..10).toEnumerable().elementAt(0), 0)
    }

    @Test fun first() {
        assertEquals((0..10).toEnumerable().first(), 0)
        assertEquals((0..10).toEnumerable().first { x -> x > 4 }, 5)
    }

    @Test fun last() {
        assertEquals((0..10).toEnumerable().last(), 10)
        assertEquals((0..10).toEnumerable().last { x -> x < 6 }, 5)
    }

    @Test fun single() {
        assertEquals((0..10).toEnumerable().singleOrDefault(), null)
        assertEquals((0..10).toEnumerable().single { x -> x % 12 == 0 }, 0)
    }

    @Test fun where() {
        assertArrayEquals((0..10).toEnumerable().where { x -> x % 2 == 0 }.toArray(), arrayOf(0, 2, 4, 6, 8, 10))
    }

    @Test fun distinct() {
        assertArrayEquals(arrayOf(0, 0, 2, 0, 3, 2, 0, 5).toEnumerable().distinct().toArray(), arrayOf(0, 2, 3, 5))
    }

    @Test fun skip() {
        assertArrayEquals(arrayOf(0, 3, 5, 7, 9).toEnumerable().skip(3).toArray(), arrayOf(7, 9))
    }

    @Test fun skipWhile() {
        assertArrayEquals(arrayOf(0, 3, 5, 7, 9).toEnumerable().skipWhile { x -> x < 7 }.toArray(), arrayOf(7, 9))
    }

    @Test fun take() {
        assertArrayEquals(arrayOf(0, 3, 5, 7, 9).toEnumerable().take(3).toArray(), arrayOf(0, 3, 5))
    }

    @Test fun takeWhile() {
        assertArrayEquals(arrayOf(0, 3, 5, 7, 9).toEnumerable().takeWhile { x -> x < 7 }.toArray(), arrayOf(0, 3, 5))
    }

    @Test fun max() {
        assertEquals(arrayOf(0, 2, 1, 10, 5, 7).toEnumerable().max(), 10)
    }

    @Test fun min() {
        assertEquals(arrayOf(3, 4, 0, 5, 2, 7).toEnumerable().min(), 0)
    }

    @Test fun average() {
        assertEquals(arrayOf(1, 2, 3).toEnumerable().average(), 2.0, 0.0001)
        assertEquals(arrayOf(1.0, 2.0).toEnumerable().average(), 1.5, 0.0001)
    }

    @Test fun sum() {
        assertEquals(arrayOf(1, 2, 3).toEnumerable().sum(), 6.0, 0.0001)
        assertEquals(arrayOf(1.0, 2.0).toEnumerable().sum(), 3.0, 0.0001)
    }

    @Test fun count() {
        assertEquals(arrayOf(1, 2, 3, 4, 0, 6, 3).toEnumerable().count(), 7)
        assertEquals(arrayOf(1, 2, 3, 4, 0, 6, 3).toEnumerable().count { x -> x % 2 == 1 }, 3)
    }

    // max,min test makes up for aggregate test

    @Test fun all() {
        assertEquals(arrayOf(1, 2, 3).toEnumerable().all { x -> x > 0 }, true)
        assertEquals(arrayOf(1, 0, 3).toEnumerable().all { x -> x > 0 }, false)
    }

    @Test fun any() {
        assertEquals(arrayOf(1, 2, 3).toEnumerable().any { x -> x > 0 }, true)
        assertEquals(arrayOf(1, 0, 3).toEnumerable().any { x -> x > 0 }, true)
    }

    @Test fun contains() {
        assertEquals(arrayOf(1, 2, 3).toEnumerable().contains(2), true)
    }

    @Test fun sequenceEqual() {
        assertEquals(arrayOf(1, 2, 3, 4, 5).toEnumerable().sequenceEqual(arrayOf(1, 2, 3, 4, 5).toEnumerable()), true)
        assertEquals(arrayOf(1, 2, 3, 4, 5).toEnumerable().sequenceEqual(arrayOf(1, 2, 3, 4, 5, 6).toEnumerable()), false)
        assertEquals(arrayOf(1, 2, 3, 4, 5, 6).toEnumerable().sequenceEqual(arrayOf(1, 2, 3, 4, 5).toEnumerable()), false)
        assertEquals(arrayOf(1, 2, 0, 4, 5).toEnumerable().sequenceEqual(arrayOf(1, 2, 3, 4, 5).toEnumerable()), false)
    }

    @Test fun union() {
        assertArrayEquals(arrayOf(1, 3, 5, 3).toEnumerable().union(arrayOf(3, 6, 7).toEnumerable()).toArray(), arrayOf(1, 3, 5, 6, 7))
    }

    @Test fun except() {
        assertArrayEquals(arrayOf(1, 3, 5, 3).toEnumerable().except(arrayOf(3, 6, 7).toEnumerable()).toArray(), arrayOf(1, 5))
    }

    @Test fun intersect() {
        assertArrayEquals(arrayOf(1, 3, 5, 3).toEnumerable().intersect(arrayOf(3, 6, 7).toEnumerable()).toArray(), arrayOf(3))
    }

    @Test fun orderBy() {
        assertArrayEquals(arrayOf(2, 4, 2, 1, 3, 5).toEnumerable().orderBy { x -> x }.toArray(), arrayOf(1, 2, 2, 3, 4, 5))
        //reset test
        var linq = arrayOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0).toEnumerable().orderBy { x -> x }
        assertArrayEquals(linq.toArray(), (0..10).toEnumerable().toArray())
        assertArrayEquals(linq.toArray(), (0..10).toEnumerable().toArray())
    }

    @Test fun orderByDescending() {
        assertArrayEquals(arrayOf(2, 4, 2, 1, 3, 5).toEnumerable().orderByDescending { x -> x }.toArray(), arrayOf(5, 4, 3, 2, 2, 1))
    }

    @Test fun thenBy() {
        assertArrayEquals(arrayOf("a", "b", "aa", "c", "aaa").toEnumerable()
                .orderBy { x -> x.length }.thenBy { x -> x }.toArray()
                , arrayOf("a", "b", "c", "aa", "aaa"))
    }

    @Test fun thenByDescending() {
        assertArrayEquals(arrayOf("a", "b", "aa", "c", "aaa").toEnumerable()
                .orderBy { x -> x.length }.thenByDescending { x -> x }.toArray()
                , arrayOf("c", "b", "a", "aa", "aaa"))
    }

    @Test fun reverse() {
        assertArrayEquals(arrayOf(3, 2, 1, 0).toEnumerable().reverse().toArray(), (0..3).toEnumerable().toArray())
    }

    @Test fun select() {
        assertArrayEquals(arrayOf(1, 2).toEnumerable().select { x -> x * x }.toArray(), arrayOf(1, 4).toEnumerable().toArray())
    }

    @Test fun selectMany() {
        // 0 -> []
        // 1 -> [0]
        // 2 -> [0,1]
        assertArrayEquals(arrayOf(0, 1, 2).toEnumerable()
                .selectMany { x -> Array(x, { x -> x }).toEnumerable() }.toArray(), arrayOf(0, 0, 1))
    }

    @Test fun groupBy() {
        assertArrayEquals(arrayOf("aaa", "aa", "bbb", "cc", "bb").toEnumerable()
                .groupBy({ x -> x.length })
                .orderByDescending { x -> x.key }
                .selectMany { x -> Enumerable(x.getEnumerator()).orderBy { x -> x } }.toArray()
                , arrayOf("aaa", "bbb", "aa", "bb", "cc"))
        assertArrayEquals(arrayOf("aaa", "aa", "bbb", "cc", "bb").toEnumerable()
                .groupBy({ x -> x.length }, { x, y -> x.toString() + y })
                .orderByDescending { x -> x.length }
                .thenBy { x -> x }
                .toArray()
                , arrayOf("3aaa", "3bbb", "2aa", "2bb", "2cc"))
    }

    @Test fun join() {
        assertArrayEquals(arrayOf("aa", "bbb", "bb").toEnumerable()
                .join(arrayOf(2, 1, 2).toEnumerable(), { x -> x.length }, { x -> x }, { x, y -> y.toString() + x }).toArray()
                , arrayOf("2aa", "2aa", "2bb", "2bb"))
    }

    @Test fun groupJoin() {
        assertArrayEquals(arrayOf("aa", "bbb", "bb").toEnumerable()
                .groupJoin(arrayOf(2, 1, 2).toEnumerable(), { x -> x.length }, { x -> x }, { x, y -> y.count().toString() + x }).toArray()
                , arrayOf("2aa", "0bbb", "2bb"))
    }

    @Test fun concat() {
        assertArrayEquals((0..2).toEnumerable().concat((3..5).toEnumerable()).toArray(), (0..5).toEnumerable().toArray())
    }

    @Test fun defaultIfEmpty() {
        assertArrayEquals(arrayOf(0, 1).toEnumerable().where { x -> x > -1 }.defaultIfEmpty(1).toArray(), arrayOf(0, 1))
        assertArrayEquals(arrayOf(0, 1).toEnumerable().where { x -> x < -1 }.defaultIfEmpty(1).toArray(), arrayOf(1))
    }

    @Test fun zip() {
        assertArrayEquals(arrayOf("a", "b", "c").toEnumerable()
                .zip(arrayOf(1, 2).toEnumerable(), { x, y -> x + y.toString() }).toArray(), arrayOf("a1", "b2"))
        assertArrayEquals(arrayOf("a", "b").toEnumerable()
                .zip(arrayOf(1, 2, 3).toEnumerable(), { x, y -> x + y.toString() }).toArray(), arrayOf("a1", "b2"))
    }

    open class a(var name: String) {
        override fun equals(other: Any?): Boolean {
            if (other == null) {
                return false
            }
            if (other is a) {
                return name.equals(other.name)
            }
            return super.equals(other)
        }
    }

    class b(name: String) : a(name) {
        fun a(): String {
            return name
        }
    }

    @Test fun ofType() {
        assertArrayEquals(arrayOf(b("a") as a, a("b")).toEnumerable().ofType<a, b>().toArray(), arrayOf(b("a")))
    }

    @Test fun cast() {
        assertArrayEquals(arrayOf(b("a") as a).toEnumerable().cast<a, b>().toArray(), arrayOf(b("a")))
    }

    @Test fun toLookUp() {
        assertArrayEquals(arrayOf("aaa", "aa", "bbb", "cc", "bb").toEnumerable()
                .toLookup({ x -> x.length })
                .orderByDescending { x -> x.key }
                .selectMany { x -> Enumerable(x.getEnumerator()).orderBy { x -> x } }.toArray()
                , arrayOf("aaa", "bbb", "aa", "bb", "cc"))
    }

    @Test fun toLookUp_VS_groupBy() {
        //groupBy is lazy
        var ar = arrayOf(-1, -1, -1, -1, -1, -1)
        var count = 0
        var linq1 = arrayOf("aaa", "aa", "bbb", "cc", "bb").toEnumerable()
                .select { x -> if (ar[0] == -1) ar[0] = count++;x }
                .groupBy({ x -> if (ar[1] == -1) ar[1] = count++;x.length })
                .orderByDescending { x -> x.key }
                .where { x -> true }
                .selectMany { x -> Enumerable(x.getEnumerator()).orderBy { x -> x } }
        ar[2] = count++
        linq1.toArray()

        var linq2 = arrayOf("aaa", "aa", "bbb", "cc", "bb").toEnumerable()
                .select { x -> if (ar[3] == -1) ar[3] = count++;x }
                .toLookup({ x -> if (ar[4] == -1) ar[4] = count++;x.length })
                .orderByDescending { x -> x.key }
                .where { x -> true }
                .selectMany { x -> Enumerable(x.getEnumerator()).orderBy { x -> x } }
        ar[5] = count++
        linq2.toArray()

        // if groupBy is lazy, ar will be [1,2,0,3,4,5]
        // else ar will be [0,1,2,3,4,5]
        assertArrayEquals(ar, arrayOf(1, 2, 0, 3, 4, 5))
    }

    @Test fun sequence() {
        assertArrayEquals(arrayOf(1, 2, 3, 4).asSequence().filter { it % 2 == 0 }.toEnumerable().toArray(), arrayOf(2, 4))
        assertArrayEquals(arrayOf(1, 2, 3, 4).toEnumerable().where { x -> x % 2 == 0 }.asSequence().toList().toTypedArray(), arrayOf(2, 4))
        var ar1 = arrayOf(0, 0, 0, 0, 0, 0, 0)
        var ar2 = arrayOf(0, 0, 0, 1, 2, 2, 2)
        var i = 0
        (0..2).toEnumerable().where { x -> ar1[i++] = x;x % 2 == 0 }
                .asSequence().filter { x -> ar1[i++] = x;x % 2 == 0 }
                .toEnumerable().select { x -> ar1[i++] = x;x }.toList()
        assertArrayEquals(ar1, ar2)
    }

}