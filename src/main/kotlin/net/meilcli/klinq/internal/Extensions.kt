package net.meilcli.klinq.internal

import net.meilcli.klinq.*
import java.util.*

internal fun <TSource> IEnumerable<TSource>._elementAt(index: Int): TSource {
    var result: TSource? = elementAtOrDefault(index)
    if (result == null) throw IllegalArgumentException("index is out of range")
    return result
}

internal fun <TSource> IEnumerable<TSource>._elementAtOrDefault(index: Int): TSource? {
    if (index < 0) return null
    var enumerator: IEnumerator<TSource> = getEnumerator()

    for (i in 0..index) {
        if (enumerator.moveNext() == false) {
            enumerator.reset()
            return null
        }
    }
    enumerator.reset()
    return enumerator.current
}

internal fun <TSource> IEnumerable<TSource>._first(predicate: ((TSource) -> Boolean)?): TSource {
    var result: TSource? = firstOrDefault(predicate)
    if (result == null) throw IllegalArgumentException("not found item")
    return result
}

internal fun <TSource> IEnumerable<TSource>._firstOrDefault(predicate: ((TSource) -> Boolean)?): TSource? {
    var enumerator: IEnumerator<TSource> = getEnumerator()

    while (enumerator.moveNext()) {
        if (predicate == null || predicate(enumerator.current)) {
            enumerator.reset()
            return enumerator.current
        }
    }
    enumerator.reset()
    return null
}

internal fun <TSource> IEnumerable<TSource>._last(predicate: ((TSource) -> Boolean)?): TSource {
    var result: TSource? = lastOrDefault(predicate)
    if (result == null) throw IllegalArgumentException("not found item")
    return result
}

internal fun <TSource> IEnumerable<TSource>._lastOrDefault(predicate: ((TSource) -> Boolean)?): TSource? {
    var enumerator: IEnumerator<TSource> = getEnumerator()

    var item: TSource? = null
    while (enumerator.moveNext()) {
        if (predicate == null || predicate(enumerator.current)) {
            item = enumerator.current
        }
    }
    enumerator.reset()
    return item
}

internal fun <TSource> IEnumerable<TSource>._single(predicate: ((TSource) -> Boolean)?): TSource {
    var result: TSource? = singleOrDefault(predicate)
    if (result == null) throw IllegalArgumentException("not found item")
    return result
}

internal fun <TSource> IEnumerable<TSource>._singleOrDefault(predicate: ((TSource) -> Boolean)?): TSource? {
    var enumerator: IEnumerator<TSource> = getEnumerator()

    var item: TSource? = null
    while (enumerator.moveNext()) {
        if (predicate == null || predicate(enumerator.current)) {
            if (item != null) {
                enumerator.reset()
                return null
            }
            item = enumerator.current
        }
    }
    enumerator.reset()
    return item
}

internal fun <TSource, TResult : Comparable<TResult>> IEnumerable<TSource>._max(selector: (TSource) -> TResult): TResult {
    var enumerator: IEnumerator<TSource> = getEnumerator()
    if (enumerator.moveNext() == false) {
        enumerator.reset()
        throw IllegalArgumentException("size is 0")
    }
    var seed: TSource = enumerator.current
    enumerator.reset()
    return aggregate(selector(seed), { x, y ->
        var second: TResult = selector(y)
        if (second.compareTo(x) > 0) second else x
    })
}

internal fun <TSource, TResult : Comparable<TResult>> IEnumerable<TSource>._min(selector: (TSource) -> TResult): TResult {
    var enumerator: IEnumerator<TSource> = getEnumerator()
    if (enumerator.moveNext() == false) {
        enumerator.reset()
        throw IllegalArgumentException("size is 0")
    }
    var seed: TSource = enumerator.current
    enumerator.reset()
    return aggregate(selector(seed), { x, y ->
        var second: TResult = selector(y)
        if (second.compareTo(x) < 0) second else x
    })
}

internal fun <TSource> IEnumerable<TSource>._average(selector: (TSource) -> Number): Double {
    var enumerator: IEnumerator<TSource> = getEnumerator()
    if (enumerator.moveNext() == false) throw IllegalArgumentException("size is 0")
    var sum: Double = selector(enumerator.current).toDouble()
    var count: Int = 1
    while (enumerator.moveNext()) {
        sum += (selector(enumerator.current).toDouble())
        count++
    }
    enumerator.reset()
    return sum / count
}

internal fun <TSource> IEnumerable<TSource>._sum(selector: (TSource) -> Number): Double {
    var enumerator: IEnumerator<TSource> = getEnumerator()
    if (enumerator.moveNext() == false) throw IllegalArgumentException("size is 0")
    var sum: Double = selector(enumerator.current).toDouble()
    while (enumerator.moveNext()) {
        sum += (selector(enumerator.current).toDouble())
    }
    enumerator.reset()
    return sum
}

internal fun <TSource> IEnumerable<TSource>._count(predicate: ((TSource) -> Boolean)?): Int {
    var enumerator: IEnumerator<TSource> = getEnumerator()
    var count: Int = 0
    while (enumerator.moveNext()) {
        if (predicate == null || predicate(enumerator.current)) {
            count++
        }
    }
    enumerator.reset()
    return count
}

internal fun <TSource> IEnumerable<TSource>._aggregate(func: (TSource, TSource) -> TSource): TSource {
    var enumerator: IEnumerator<TSource> = getEnumerator()
    if (enumerator.moveNext() == false) {
        enumerator.reset()
        throw IllegalArgumentException("size is 0")
    }
    var seed: TSource = enumerator.current
    enumerator.reset()
    return aggregate(seed, func, { x -> x })
}

internal fun <TSource, TAccumulate, TResult> IEnumerable<TSource>._aggregate(seed: TAccumulate, func: (TAccumulate, TSource) -> TAccumulate, resultSelector: (TAccumulate) -> TResult): TResult {
    var enumerator: IEnumerator<TSource> = getEnumerator()
    var select: TAccumulate = seed
    while (enumerator.moveNext()) {
        select = func(select, enumerator.current)
    }
    enumerator.reset()
    return resultSelector(select)
}

internal fun <TSource> IEnumerable<TSource>._all(predicate: (TSource) -> Boolean): Boolean {
    var enumerator: IEnumerator<TSource> = getEnumerator()

    while (enumerator.moveNext()) {
        if (predicate(enumerator.current) == false) {
            enumerator.reset()
            return false
        }
    }
    enumerator.reset()
    return true
}

internal fun <TSource> IEnumerable<TSource>._any(predicate: ((TSource) -> Boolean)?): Boolean {
    var enumerator: IEnumerator<TSource> = getEnumerator()

    while (enumerator.moveNext()) {
        if (predicate == null || predicate(enumerator.current)) {
            enumerator.reset()
            return true
        }
    }
    enumerator.reset()
    return false
}

internal fun <TSource> IEnumerable<TSource>._sequenceEqual(second: IEnumerable<TSource>, comparer: IEqualityComparer<TSource>): Boolean {
    var firstEnumerator: IEnumerator<TSource> = getEnumerator()
    var secondEnumerator: IEnumerator<TSource> = second.getEnumerator()
    while (firstEnumerator.moveNext()) {
        if (secondEnumerator.moveNext() == false) {
            firstEnumerator.reset()
            secondEnumerator.reset()
            return false
        }
        if ((comparer.equals(firstEnumerator.current, secondEnumerator.current)) == false) {
            firstEnumerator.reset()
            secondEnumerator.reset()
            return false
        }
    }
    if (secondEnumerator.moveNext()) {
        firstEnumerator.reset()
        secondEnumerator.reset()
        return false
    }
    firstEnumerator.reset()
    secondEnumerator.reset()
    return true
}

inline fun <reified TSource> IEnumerable<TSource>._defaultIfEmpty(): IEnumerable<TSource?> {
    if (getEnumerator().moveNext()) {
        getEnumerator().reset()
        // TSource→TSource?
        return this.select { x -> x }
    } else {
        getEnumerator().reset()
        return arrayOfNulls<TSource>(1).toEnumerable()
    }
}

inline fun <reified TSource> IEnumerable<TSource>._defaultIfEmpty(defaultValue: TSource): IEnumerable<TSource> {
    if (getEnumerator().moveNext()) {
        getEnumerator().reset()
        // TSource→TSource?
        return this.select { x -> x }
    } else {
        getEnumerator().reset()
        return arrayOf(defaultValue).toEnumerable()
    }
}

internal fun <TSource> IEnumerable<TSource>._toList(): List<TSource> {
    var list = ArrayList<TSource>()
    var enumerator: IEnumerator<TSource> = getEnumerator()
    while (enumerator.moveNext()) {
        list.add(enumerator.current)
    }
    enumerator.reset()
    return list
}

internal fun <TSource, TKey, TElement> IEnumerable<TSource>._toDictionary(keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement, comparer: IEqualityComparer<TKey>)
        : Map<TKey, TElement> {
    var map = HashMap<TKey, TElement>()
    var enumerator: IEnumerator<TSource> = getEnumerator()
    while (enumerator.moveNext()) {
        var key: TKey = keySelector(enumerator.current)
        if (map.keys.toEnumerable().contains(key, comparer) == false) {
            map.put(key, elementSelector(enumerator.current))
        } else {
            throw IllegalArgumentException("duplicate keys")
        }
    }
    enumerator.reset()
    return map
}

inline fun<reified TSource> IEnumerable<TSource>._toArray(): Array<TSource> {
    var list: List<TSource> = toList()
    return Array(list.size, { i -> list[i] })
}

internal fun <TSource> IEnumerable<TSource>._forEach(action: (TSource, Int) -> Unit) {
    var enumerator: IEnumerator<TSource> = getEnumerator()
    var index = 0
    while (enumerator.moveNext()) {
        action(enumerator.current, index)
        index++
    }
    enumerator.reset()
}