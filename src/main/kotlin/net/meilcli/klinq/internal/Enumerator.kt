package net.meilcli.klinq.internal

import net.meilcli.klinq.*
import java.util.*

internal class WhereEnumerator<TSource>(val enumerator: IEnumerator<TSource>, val predicate: (TSource, Int) -> Boolean) : IEnumerator<TSource> {
    private var index: Int = -1
    private var _current: TSource? = null
    override var current: TSource
        get() = _current!!
        set(value) {
            _current = value
        }

    override fun moveNext(): Boolean {
        while (enumerator.moveNext()) {
            index++
            if (predicate(enumerator.current, index)) {
                _current = enumerator.current
                return true
            }
        }
        return false
    }

    override fun reset() {
        index = -1
        enumerator.reset()
    }
}

internal class DistinctEnumerator<TSource>(val enumerator: IEnumerator<TSource>, val comparer: IEqualityComparer<TSource>) : IEnumerator<TSource> {
    private var list = ArrayList<TSource>()
    private var _current: TSource? = null
    override var current: TSource
        get() = _current!!
        set(value) {
            _current = value
        }

    override fun moveNext(): Boolean {
        while (enumerator.moveNext()) {
            var item: TSource = enumerator.current
            if (list.toEnumerable().contains(item, comparer)) {
                continue
            }
            _current = item
            list.add(item)
            return true
        }
        return false
    }

    override fun reset() {
        list.clear()
        enumerator.reset()
    }
}

internal class SkipWhileEnumerator<TSource>(val enumerator: IEnumerator<TSource>, val predicate: (TSource, Int) -> Boolean) : IEnumerator<TSource> {
    private var index: Int = 0
    override var current: TSource
        get() = enumerator.current
        set(value) {
            enumerator.current = value
        }

    override fun moveNext(): Boolean {
        var result: Boolean = enumerator.moveNext()
        while (result && predicate(enumerator.current, index)) {
            index++
            result = enumerator.moveNext()
        }
        return result
    }

    override fun reset() {
        index = 0
        enumerator.reset()
    }
}

internal class TakeWhileEnumerator<TSource>(val enumerator: IEnumerator<TSource>, val predicate: (TSource, Int) -> Boolean) : IEnumerator<TSource> {
    private var index: Int = 0
    override var current: TSource
        get() = enumerator.current
        set(value) {
            enumerator.current = value
        }

    override fun moveNext(): Boolean {
        var result: Boolean = enumerator.moveNext()
        if (result && predicate(enumerator.current, index)) {
            index++
        } else {
            result = false
        }
        return result
    }

    override fun reset() {
        index = 0
        enumerator.reset()
    }
}

internal class UnionEnumerator<TSource>(val enumerator: IEnumerator<TSource>, val secondEnumerator: IEnumerator<TSource>, val comparer: IEqualityComparer<TSource>) : IEnumerator<TSource> {
    private var list = ArrayList<TSource>()
    private var _current: TSource? = null
    override var current: TSource
        get() = _current!!
        set(value) {
            _current = value
        }

    override fun moveNext(): Boolean {
        while (enumerator.moveNext()) {
            var item: TSource = enumerator.current
            if (list.toEnumerable().contains(item, comparer)) {
                continue
            }
            _current = item
            list.add(item)
            return true
        }
        while (secondEnumerator.moveNext()) {
            var item: TSource = secondEnumerator.current
            if (list.toEnumerable().contains(item, comparer)) {
                continue
            }
            _current = item
            list.add(item)
            return true
        }
        return false
    }

    override fun reset() {
        list.clear()
        enumerator.reset()
        secondEnumerator.reset()
    }
}

internal class ExceptEnumerator<TSource>(val enumerator: IEnumerator<TSource>, val second: IEnumerable<TSource>, val comparer: IEqualityComparer<TSource>) : IEnumerator<TSource> {
    private var list = ArrayList<TSource>()
    private var _current: TSource? = null
    override var current: TSource
        get() = _current!!
        set(value) {
            _current = value
        }

    override fun moveNext(): Boolean {
        while (enumerator.moveNext()) {
            var item: TSource = enumerator.current
            if (list.toEnumerable().contains(item, comparer) || second.contains(item, comparer)) {
                continue
            }
            _current = item
            list.add(item)
            return true
        }
        return false
    }

    override fun reset() {
        list.clear()
        enumerator.reset()
    }
}

internal class IntersectEnumerator<TSource>(val enumerator: IEnumerator<TSource>, val second: IEnumerable<TSource>, val comparer: IEqualityComparer<TSource>) : IEnumerator<TSource> {
    private var list = ArrayList<TSource>()
    private var _current: TSource? = null
    override var current: TSource
        get() = _current!!
        set(value) {
            _current = value
        }

    override fun moveNext(): Boolean {
        while (enumerator.moveNext()) {
            var item: TSource = enumerator.current
            if (list.toEnumerable().contains(item, comparer) || second.contains(item, comparer) == false) {
                continue
            }
            _current = item
            list.add(item)
            return true
        }
        return false
    }

    override fun reset() {
        list.clear()
        enumerator.reset()
    }
}

internal class SelectEnumerator<TSource, TResult>(val enumerator: IEnumerator<TSource>, val selector: (TSource, Int) -> TResult) : IEnumerator<TResult> {
    private var index: Int = 0
    private var _current: TResult? = null
    override var current: TResult
        get() = _current!!
        set(value) {
            _current = value
        }

    override fun moveNext(): Boolean {
        var result: Boolean = enumerator.moveNext()
        if (result == true) {
            _current = selector(enumerator.current, index)
            index++
        }
        return result;
    }

    override fun reset() {
        index = 0
        enumerator.reset()
    }
}

internal class SelectManyEnumerator<TSource, TCollection, TResult>(
        val enumerator: IEnumerator<TSource>, val collectionSelector: (TSource, Int) -> IEnumerable<TCollection>, val resultSelector: (TSource, TCollection) -> TResult) : IEnumerator<TResult> {
    private var index: Int = 0
    private var select: TSource? = null
    private var collection: IEnumerable<TCollection>? = null
    private var _current: TResult? = null
    override var current: TResult
        get() = _current!!
        set(value) {
            _current = value
        }

    override fun moveNext(): Boolean {
        if (collection != null) {
            if (collection!!.getEnumerator().moveNext()) {
                _current = resultSelector(select!!, collection!!.getEnumerator().current)
                return true
            } else {
                collection = null
            }
        }
        while (enumerator.moveNext()) {
            collection = collectionSelector(enumerator.current, index)
            select = enumerator.current
            index++
            if (collection!!.getEnumerator().moveNext()) {
                _current = resultSelector(select!!, collection!!.getEnumerator().current)
                return true
            }
        }
        return false
    }

    override fun reset() {
        index = 0
        collection = null
        enumerator.reset()
    }
}

internal class JoinEnumerator<TOuter, TInner, TKey, TResult>(
        val enumerator: IEnumerator<TOuter>, val innnerEnumerator: IEnumerator<TInner>, val outerKeySelector: (TOuter) -> TKey,
        val innerKeySelector: (TInner) -> TKey, val resultSelector: (TOuter, TInner) -> TResult, val comparer: IEqualityComparer<TKey>) : IEnumerator<TResult> {
    private var isContinue: Boolean = false
    private var _current: TResult? = null
    override var current: TResult
        get() = _current!!
        set(value) {
            _current = value
        }

    override fun moveNext(): Boolean {
        if (isContinue) {
            var key: TKey = outerKeySelector(enumerator.current)
            while (innnerEnumerator.moveNext()) {
                if (comparer.equals(key, innerKeySelector(innnerEnumerator.current))) {
                    _current = resultSelector(enumerator.current, innnerEnumerator.current)
                    return true
                }
            }
            innnerEnumerator.reset()
            isContinue = false
        }
        while (enumerator.moveNext()) {
            var key: TKey = outerKeySelector(enumerator.current)
            while (innnerEnumerator.moveNext()) {
                if (comparer.equals(key, innerKeySelector(innnerEnumerator.current))) {
                    _current = resultSelector(enumerator.current, innnerEnumerator.current)
                    isContinue = true
                    return true
                }
            }
            innnerEnumerator.reset()
        }
        return false
    }

    override fun reset() {
        isContinue = false
        enumerator.reset()
    }
}

internal class GroupJoinEnumerator<TOuter, TInner, TKey, TResult>(
        val enumerator: IEnumerator<TOuter>, val inner: IEnumerable<TInner>, val outerKeySelector: (TOuter) -> TKey, val innerKeySelector: (TInner) -> TKey,
        val resultSelector: (TOuter, IEnumerable<TInner>) -> TResult, val comparer: IEqualityComparer<TKey>) : IEnumerator<TResult> {
    private var _current: TResult? = null
    override var current: TResult
        get() = _current!!
        set(value) {
            _current = value
        }

    override fun moveNext(): Boolean {
        var result: Boolean = enumerator.moveNext()
        if (result) {
            var key: TKey = outerKeySelector(enumerator.current)
            _current = resultSelector(enumerator.current, inner.where { x -> comparer.equals(innerKeySelector(x), key) })
        }
        return result
    }

    override fun reset() {
        enumerator.reset()
    }
}

internal class ConcatEnumerator<TSource>(val enumerator: IEnumerator<TSource>, val secondEnumerator: IEnumerator<TSource>) : IEnumerator<TSource> {
    private var _current: TSource? = null
    override var current: TSource
        get() = _current!!
        set(value) {
            _current = value
        }

    override fun moveNext(): Boolean {
        while (enumerator.moveNext()) {
            _current = enumerator.current
            return true
        }
        while (secondEnumerator.moveNext()) {
            _current = secondEnumerator.current
            return true
        }
        return false
    }

    override fun reset() {
        enumerator.reset()
        secondEnumerator.reset()
    }
}

internal class ZipEnumerator<TFirst, TSecond, TResult>(
        val firstEnumerator: IEnumerator<TFirst>, val secondEnumerator: IEnumerator<TSecond>, val resultSelector: (TFirst, TSecond) -> TResult) : IEnumerator<TResult> {
    private var _current: TResult? = null
    override var current: TResult
        get() = _current!!
        set(value) {
            _current = value
        }

    override fun moveNext(): Boolean {
        while (firstEnumerator.moveNext() && secondEnumerator.moveNext()) {
            _current = resultSelector(firstEnumerator.current, secondEnumerator.current)
            return true
        }
        return false
    }

    override fun reset() {
        firstEnumerator.reset()
        secondEnumerator.reset()
    }
}

inline fun <TSource, reified TResult> IEnumerable<TSource>._ofType(): IEnumerable<TResult> {
    var enumerator: IEnumerator<TSource> = getEnumerator()
    var newEnumerator: IEnumerator<TResult> = object : IEnumerator<TResult> {
        private var _current: TResult? = null
        override var current: TResult
            get() = _current!!
            set(value) {
                _current = value
            }

        override fun moveNext(): Boolean {
            while (enumerator.moveNext()) {
                var item: TSource = enumerator.current
                if (item is TResult) {
                    _current = item
                    return true
                } else {
                    continue
                }
            }
            return false
        }

        override fun reset() {
            enumerator.reset()
        }
    }
    return Enumerable<TResult>(newEnumerator)
}

inline fun <TSource, reified TResult> IEnumerable<TSource>._cast(): IEnumerable<TResult> {
    var enumerator: IEnumerator<TSource> = getEnumerator()
    var newEnumerator: IEnumerator<TResult> = object : IEnumerator<TResult> {
        private var _current: TResult? = null
        override var current: TResult
            get() = _current!!
            set(value) {
                _current = value
            }

        override fun moveNext(): Boolean {
            if (enumerator.moveNext()) {
                var item: TSource = enumerator.current
                if (item is TResult) {
                    _current = item
                    return true
                } else {
                    throw ClassCastException("cannnot cast")
                }
            }
            return false
        }

        override fun reset() {
            enumerator.reset()
        }
    }
    return Enumerable<TResult>(newEnumerator)
}