package net.meilcli.klinq.internal

import net.meilcli.klinq.IEnumerable
import net.meilcli.klinq.IEnumerator
import net.meilcli.klinq.IOrderedEnumerable
import net.meilcli.klinq.toList
import java.util.*

internal class OrderedEnumerable<T, TKey> : IOrderedEnumerable<T> {

    private var enumComparator: Comparator<T>
    private val enumerator: IEnumerator<T>

    constructor(enumerable: IEnumerable<T>, keySelector: (T) -> TKey, comparator: Comparator<TKey>, descending: Boolean) {
        enumComparator = object : Comparator<T> {
            override fun compare(o1: T, o2: T): Int {
                var t1: TKey = keySelector(o1)
                var t2: TKey = keySelector(o2)
                var result: Int = comparator.compare(t1, t2)
                return if (descending) -1 * result else result
            }
        }
        enumerator = object : IEnumerator<T> {
            private var list: List<T>? = null
            private var index: Int = -1
            private var _current: T? = null
            override var current: T
                get() = _current!!
                set(value) {
                    _current = value
                }

            override fun moveNext(): Boolean {
                if (list == null) {
                    list = enumerable.toList()
                    Collections.sort(list, enumComparator)
                }
                index++
                if (index >= list!!.size) {
                    return false
                }
                _current = list!![index]
                return true
            }

            override fun reset() {
                // do cache
                //list=null
                index = -1
            }
        }
    }

    override fun <TKey> createOrderedEnumerable(keySelector: (T) -> TKey, comparator: Comparator<TKey>, descending: Boolean): IOrderedEnumerable<T> {
        var oldComparator: Comparator<T> = enumComparator
        enumComparator = object : Comparator<T> {
            override fun compare(o1: T, o2: T): Int {
                var r: Int = oldComparator.compare(o1, o2)
                if (r != 0) return r
                var t1: TKey = keySelector(o1)
                var t2: TKey = keySelector(o2)
                var result: Int = comparator.compare(t1, t2)
                return if (descending) -1 * result else result
            }
        }
        return this
    }

    override fun getEnumerator(): IEnumerator<T> {
        return enumerator
    }
}