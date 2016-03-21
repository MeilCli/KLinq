package net.meilcli.klinq

import net.meilcli.klinq.internal.*
import java.util.*

// function orderBy http://d.hatena.ne.jp/chiheisen/20111031/1320068429
// function referenced https://msdn.microsoft.com/ja-jp/library/9eekhta0(v=vs.110).aspx method
// The difference between this and C# IEnumerable<T> is ...
// - this must use [List,Array,Iterable...].toEnumeable()
//   because some function's name conflict stdlib's function
// - **OrDefault function, default is only null
// - average function, generics is extended Number
//   because JVM generics must not allow overload
// - sum function, generics is extended Number and return Double
//   because JVM generics must not allow overload
// - defaultIfEmpty function, empty is only null
// - ofType,cast function
//   because extension function
// - add forEach function


fun <TSource> Iterable<TSource>.toEnumerable(): IEnumerable<TSource> {
    return Enumerable<TSource>(this)
}

fun <TSource> Array<TSource>.toEnumerable(): IEnumerable<TSource> {
    return Enumerable<TSource>({ iterator() })
}

fun BooleanArray.toEnumerable(): IEnumerable<Boolean> {
    return Enumerable({ iterator() })
}

fun CharArray.toEnumerable(): IEnumerable<Char> {
    return Enumerable({ iterator() })
}

fun ByteArray.toEnumerable(): IEnumerable<Byte> {
    return Enumerable({ iterator() })
}

fun ShortArray.toEnumerable(): IEnumerable<Short> {
    return Enumerable({ iterator() })
}

fun IntArray.toEnumerable(): IEnumerable<Int> {
    return Enumerable({ iterator() })
}

fun LongArray.toEnumerable(): IEnumerable<Long> {
    return Enumerable({ iterator() })
}

fun FloatArray.toEnumerable(): IEnumerable<Float> {
    return Enumerable({ iterator() })
}

fun DoubleArray.toEnumerable(): IEnumerable<Double> {
    return Enumerable({ iterator() })
}

fun <TSource> IEnumerable<TSource>.elementAt(index: Int): TSource {
    var result: TSource? = elementAtOrDefault(index)
    if (result == null) throw IllegalArgumentException("index is out of range")
    return result
}

fun <TSource> IEnumerable<TSource>.elementAtOrDefault(index: Int): TSource? {
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

fun <TSource> IEnumerable<TSource>.first() = first(null)

fun <TSource> IEnumerable<TSource>.first(predicate: ((TSource) -> Boolean)?): TSource {
    var result: TSource? = firstOrDefault(predicate)
    if (result == null) throw IllegalArgumentException("not found item")
    return result
}

fun <TSource> IEnumerable<TSource>.firstOrDefault() = firstOrDefault(null)

fun <TSource> IEnumerable<TSource>.firstOrDefault(predicate: ((TSource) -> Boolean)?): TSource? {
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

fun <TSource> IEnumerable<TSource>.last() = last(null)

fun <TSource> IEnumerable<TSource>.last(predicate: ((TSource) -> Boolean)?): TSource {
    var result: TSource? = lastOrDefault(predicate)
    if (result == null) throw IllegalArgumentException("not found item")
    return result
}

fun <TSource> IEnumerable<TSource>.lastOrDefault() = lastOrDefault(null)

fun <TSource> IEnumerable<TSource>.lastOrDefault(predicate: ((TSource) -> Boolean)?): TSource? {
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

fun <TSource> IEnumerable<TSource>.single() = single(null)

fun <TSource> IEnumerable<TSource>.single(predicate: ((TSource) -> Boolean)?): TSource {
    var result: TSource? = singleOrDefault(predicate)
    if (result == null) throw IllegalArgumentException("not found item")
    return result
}

fun <TSource> IEnumerable<TSource>.singleOrDefault() = singleOrDefault(null)

fun <TSource> IEnumerable<TSource>.singleOrDefault(predicate: ((TSource) -> Boolean)?): TSource? {
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

fun <TSource> IEnumerable<TSource>.where(predicate: (TSource) -> Boolean): IEnumerable<TSource> {
    return where { t, i -> predicate(t) }
}

fun <TSource> IEnumerable<TSource>.where(predicate: (TSource, Int) -> Boolean): IEnumerable<TSource>
        = Enumerable(WhereEnumerator(getEnumerator(), predicate))

fun <TSource> IEnumerable<TSource>.distinct() = distinct(EqualityComparer<TSource>())

fun <TSource> IEnumerable<TSource>.distinct(comparer: IEqualityComparer<TSource>): IEnumerable<TSource>
        = Enumerable(DistinctEnumerator(getEnumerator(), comparer))

fun <TSource> IEnumerable<TSource>.skip(count: Int): IEnumerable<TSource> {
    return skipWhile { t, i -> i < count }
}

fun <TSource> IEnumerable<TSource>.skipWhile(predicate: (TSource) -> Boolean): IEnumerable<TSource> {
    return skipWhile { t, i -> predicate(t) }
}

fun <TSource> IEnumerable<TSource>.skipWhile(predicate: (TSource, Int) -> Boolean): IEnumerable<TSource>
        = Enumerable(SkipWhileEnumerator(getEnumerator(), predicate))

fun <TSource> IEnumerable<TSource>.take(count: Int): IEnumerable<TSource> {
    return takeWhile { t, i -> i < count }
}

fun <TSource> IEnumerable<TSource>.takeWhile(predicate: (TSource) -> Boolean): IEnumerable<TSource> {
    return takeWhile { t, i -> predicate(t) }
}

fun <TSource> IEnumerable<TSource>.takeWhile(predicate: (TSource, Int) -> Boolean): IEnumerable<TSource>
        = Enumerable(TakeWhileEnumerator(getEnumerator(), predicate))

fun <TSource : Comparable<TSource>> IEnumerable<TSource>.max(): TSource {
    return max { x -> x }
}

fun <TSource, TResult : Comparable<TResult>> IEnumerable<TSource>.max(selector: (TSource) -> TResult): TResult {
    var enumerator: IEnumerator<TSource> = getEnumerator()
    if (enumerator.moveNext() == false) {
        enumerator.reset()
        throw IllegalArgumentException("size is 0")
    }
    var seed: TSource = enumerator.current
    enumerator.reset()
    return aggregate(selector(seed), { x, y ->
        var second: TResult = selector(y);
        if (second.compareTo(x) > 0) second else x
    })
}

fun <TSource : Comparable<TSource>> IEnumerable<TSource>.min(): TSource {
    return min { x -> x }
}

fun <TSource, TResult : Comparable<TResult>> IEnumerable<TSource>.min(selector: (TSource) -> TResult): TResult {
    var enumerator: IEnumerator<TSource> = getEnumerator()
    if (enumerator.moveNext() == false) {
        enumerator.reset()
        throw IllegalArgumentException("size is 0")
    }
    var seed: TSource = enumerator.current
    enumerator.reset()
    return aggregate(selector(seed), { x, y ->
        var second: TResult = selector(y);
        if (second.compareTo(x) < 0) second else x
    })
}

fun <TSource : Number> IEnumerable<TSource>.average(): Double {
    return average { x -> x }
}

fun <TSource> IEnumerable<TSource>.average(selector: (TSource) -> Number): Double {
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

fun <TSource : Number> IEnumerable<TSource>.sum(): Double {
    return sum { x -> x }
}

fun <TSource> IEnumerable<TSource>.sum(selector: (TSource) -> Number): Double {
    var enumerator: IEnumerator<TSource> = getEnumerator()
    if (enumerator.moveNext() == false) throw IllegalArgumentException("size is 0")
    var sum: Double = selector(enumerator.current).toDouble()
    while (enumerator.moveNext()) {
        sum += (selector(enumerator.current).toDouble())
    }
    enumerator.reset()
    return sum
}

fun <TSource> IEnumerable<TSource>.count() = count(null)

fun <TSource> IEnumerable<TSource>.count(predicate: ((TSource) -> Boolean)?): Int {
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

fun <TSource> IEnumerable<TSource>.aggregate(func: (TSource, TSource) -> TSource): TSource {
    var enumerator: IEnumerator<TSource> = getEnumerator()
    if (enumerator.moveNext() == false) {
        enumerator.reset()
        throw IllegalArgumentException("size is 0")
    }
    var seed: TSource = enumerator.current
    enumerator.reset()
    return aggregate(seed, func, { x -> x })
}

fun <TSource, TAccumulate> IEnumerable<TSource>.aggregate(
        seed: TAccumulate,
        func: (TAccumulate, TSource) -> TAccumulate): TAccumulate {
    return aggregate(seed, func, { x -> x })
}

fun <TSource, TAccumulate, TResult> IEnumerable<TSource>.aggregate(
        seed: TAccumulate,
        func: (TAccumulate, TSource) -> TAccumulate,
        resultSelector: (TAccumulate) -> TResult): TResult {
    var enumerator: IEnumerator<TSource> = getEnumerator()
    var select: TAccumulate = seed
    while (enumerator.moveNext()) {
        select = func(select, enumerator.current)
    }
    enumerator.reset()
    return resultSelector(select)
}

fun <TSource> IEnumerable<TSource>.all(predicate: (TSource) -> Boolean): Boolean {
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

fun <TSource> IEnumerable<TSource>.any() = any(null)

fun <TSource> IEnumerable<TSource>.any(predicate: ((TSource) -> Boolean)?): Boolean {
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

fun <TSource> IEnumerable<TSource>.contains(value: TSource) = contains(value, EqualityComparer<TSource>())

fun <TSource> IEnumerable<TSource>.contains(value: TSource, comparer: IEqualityComparer<TSource>): Boolean {
    return any { x -> comparer.equals(x, value) }
}

fun <TSource> IEnumerable<TSource>.sequenceEqual(second: IEnumerable<TSource>) = sequenceEqual(second, EqualityComparer<TSource>())

fun <TSource> IEnumerable<TSource>.sequenceEqual(second: IEnumerable<TSource>, comparer: IEqualityComparer<TSource>): Boolean {
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

fun <TSource> IEnumerable<TSource>.union(second: IEnumerable<TSource>) = union(second, EqualityComparer<TSource>())

fun <TSource> IEnumerable<TSource>.union(second: IEnumerable<TSource>, comparer: IEqualityComparer<TSource>): IEnumerable<TSource>
        = Enumerable(UnionEnumerator(getEnumerator(), second.getEnumerator(), comparer))

fun <TSource> IEnumerable<TSource>.except(second: IEnumerable<TSource>) = except(second, EqualityComparer<TSource>())

fun <TSource> IEnumerable<TSource>.except(second: IEnumerable<TSource>, comparer: IEqualityComparer<TSource>): IEnumerable<TSource>
        = Enumerable(ExceptEnumerator(getEnumerator(), second, comparer))

fun <TSource> IEnumerable<TSource>.intersect(second: IEnumerable<TSource>) = intersect(second, EqualityComparer<TSource>())

fun <TSource> IEnumerable<TSource>.intersect(second: IEnumerable<TSource>, comparer: IEqualityComparer<TSource>): IEnumerable<TSource>
        = Enumerable(IntersectEnumerator(getEnumerator(), second, comparer))

fun <TSource, TKey : Comparable<TKey>> IEnumerable<TSource>.orderBy(
        keySelector: (TSource) -> TKey): IOrderedEnumerable<TSource> {
    var comparator = object : Comparator<TKey> {
        override fun compare(o1: TKey, o2: TKey): Int {
            return o1.compareTo(o2)
        }
    }
    return OrderedEnumerable<TSource, TKey>(this, keySelector, comparator, false)
}

fun <TSource, TKey> IEnumerable<TSource>.orderBy(
        keySelector: (TSource) -> TKey,
        comparator: Comparator<TKey>): IOrderedEnumerable<TSource> {
    return OrderedEnumerable<TSource, TKey>(this, keySelector, comparator, false)
}

fun <TSource, TKey : Comparable<TKey>> IEnumerable<TSource>.orderByDescending(
        keySelector: (TSource) -> TKey): IOrderedEnumerable<TSource> {
    var comparator = object : Comparator<TKey> {
        override fun compare(o1: TKey, o2: TKey): Int {
            return o1.compareTo(o2)
        }
    }
    return OrderedEnumerable<TSource, TKey>(this, keySelector, comparator, true)
}

fun <TSource, TKey> IEnumerable<TSource>.orderByDescending(
        keySelector: (TSource) -> TKey,
        comparator: Comparator<TKey>): IOrderedEnumerable<TSource> {
    return OrderedEnumerable<TSource, TKey>(this, keySelector, comparator, true)
}

fun<TSource, TKey : Comparable<TKey>> IOrderedEnumerable<TSource>.thenBy(
        keySelector: (TSource) -> TKey): IOrderedEnumerable<TSource> {
    var comparator = object : Comparator<TKey> {
        override fun compare(o1: TKey, o2: TKey): Int {
            return o1.compareTo(o2)
        }
    }
    return createOrderedEnumerable(keySelector, comparator, false)
}

fun <TSource, TKey> IOrderedEnumerable<TSource>.thenBy(
        keySelector: (TSource) -> TKey,
        comparator: Comparator<TKey>): IOrderedEnumerable<TSource> {
    return createOrderedEnumerable(keySelector, comparator, false)
}

fun <TSource, TKey : Comparable<TKey>> IOrderedEnumerable<TSource>.thenByDescending(
        keySelector: (TSource) -> TKey): IOrderedEnumerable<TSource> {
    var comparator = object : Comparator<TKey> {
        override fun compare(o1: TKey, o2: TKey): Int {
            return o1.compareTo(o2)
        }
    }
    return createOrderedEnumerable(keySelector, comparator, true)
}

fun <TSource, TKey> IOrderedEnumerable<TSource>.thenByDescending(
        keySelector: (TSource) -> TKey,
        comparator: Comparator<TKey>): IOrderedEnumerable<TSource> {
    return createOrderedEnumerable(keySelector, comparator, true)
}

fun <TSource> IEnumerable<TSource>.reverse(): IEnumerable<TSource> {
    return Enumerable<TSource>(toList().reversed())
}

fun <TSource, TResult> IEnumerable<TSource>.select(selector: (TSource) -> TResult): IEnumerable<TResult> {
    return select { t, i -> selector(t) }
}

fun <TSource, TResult> IEnumerable<TSource>.select(selector: (TSource, Int) -> TResult): IEnumerable<TResult>
        = Enumerable(SelectEnumerator(getEnumerator(), selector))

fun <TSource, TResult> IEnumerable<TSource>.selectMany(
        selector: (TSource) -> IEnumerable<TResult>): IEnumerable<TResult> {
    return selectMany { x, y -> selector(x) }
}

fun <TSource, TResult> IEnumerable<TSource>.selectMany(
        selector: (TSource, Int) -> IEnumerable<TResult>): IEnumerable<TResult> {
    return selectMany(selector, { x, y -> y })
}

fun <TSource, TCollection, TResult> IEnumerable<TSource>.selectMany(
        collectionSelector: (TSource) -> IEnumerable<TCollection>,
        resultSelector: (TSource, TCollection) -> TResult): IEnumerable<TResult> {
    return selectMany({ x, y -> collectionSelector(x) }, resultSelector)
}

fun <TSource, TCollection, TResult> IEnumerable<TSource>.selectMany(
        collectionSelector: (TSource, Int) -> IEnumerable<TCollection>, resultSelector: (TSource, TCollection) -> TResult): IEnumerable<TResult>
        = Enumerable(SelectManyEnumerator(getEnumerator(), collectionSelector, resultSelector))

//キャッシュするならIEnumeratorから実装しなければ…
fun <TSource, TKey> IEnumerable<TSource>.groupBy(keySelector: (TSource) -> TKey) = groupBy(keySelector, EqualityComparer<TKey>())

fun <TSource, TKey> IEnumerable<TSource>.groupBy(keySelector: (TSource) -> TKey, comparer: IEqualityComparer<TKey>): IEnumerable<IGrouping<TKey, TSource>> {
    return groupBy(keySelector, { x -> x }, comparer)
}

fun <TSource, TKey, TElement> IEnumerable<TSource>.groupBy(keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement)
        = groupBy(keySelector, elementSelector, EqualityComparer<TKey>())

fun <TSource, TKey, TElement> IEnumerable<TSource>.groupBy(keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement, comparer: IEqualityComparer<TKey>)
        : IEnumerable<IGrouping<TKey, TElement>> {
    var source: IEnumerable<TSource> = this
    return object : IEnumerable<IGrouping<TKey, TElement>> {
        override fun getEnumerator(): IEnumerator<IGrouping<TKey, TElement>> {
            return Lookup<TSource, TKey, TElement>(source, keySelector, elementSelector, comparer).getEnumerator()
        }
    }
}

fun <TSource, TKey, TResult> IEnumerable<TSource>.groupBy(keySelector: (TSource) -> TKey, resultSelector: (TKey, TSource) -> TResult)
        = groupBy(keySelector, resultSelector, EqualityComparer<TKey>())

fun <TSource, TKey, TResult> IEnumerable<TSource>.groupBy(keySelector: (TSource) -> TKey, resultSelector: (TKey, TSource) -> TResult, comparer: IEqualityComparer<TKey>)
        : IEnumerable<TResult> {
    return groupBy(keySelector, { x -> x }, resultSelector, comparer)
}

fun <TSource, TKey, TElement, TResult> IEnumerable<TSource>.groupBy(
        keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement, resultSelector: (TKey, TElement) -> TResult)
        = groupBy(keySelector, elementSelector, resultSelector, EqualityComparer<TKey>())

fun <TSource, TKey, TElement, TResult> IEnumerable<TSource>.groupBy(
        keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement, resultSelector: (TKey, TElement) -> TResult, comparer: IEqualityComparer<TKey>):
        IEnumerable<TResult> {
    var source: IEnumerable<TSource> = this
    return object : IEnumerable<TResult> {
        override fun getEnumerator(): IEnumerator<TResult> {
            var enumerator: IEnumerator<IGrouping<TKey, TElement>>
                    = Lookup<TSource, TKey, TElement>(source, keySelector, elementSelector, comparer).getEnumerator()
            var result = ArrayList<TResult>()
            while (enumerator.moveNext()) {
                var group: IGrouping<TKey, TElement> = enumerator.current
                var groupEnumerator: IEnumerator<TElement> = group.getEnumerator()
                while (groupEnumerator.moveNext()) {
                    result.add(resultSelector(group.key, groupEnumerator.current))
                }
                groupEnumerator.reset()
            }
            enumerator.reset()
            return Enumerable<TResult>(result).getEnumerator()
        }
    }
}

fun <TOuter, TInner, TKey, TResult> IEnumerable<TOuter>.join(
        inner: IEnumerable<TInner>, outerKeySelector: (TOuter) -> TKey, innerKeySelector: (TInner) -> TKey, resultSelector: (TOuter, TInner) -> TResult)
        = join(inner, outerKeySelector, innerKeySelector, resultSelector, EqualityComparer<TKey>())

fun <TOuter, TInner, TKey, TResult> IEnumerable<TOuter>.join(
        inner: IEnumerable<TInner>, outerKeySelector: (TOuter) -> TKey, innerKeySelector: (TInner) -> TKey, resultSelector: (TOuter, TInner) -> TResult, comparer: IEqualityComparer<TKey>)
        : IEnumerable<TResult>
        = Enumerable<TResult>(JoinEnumerator(getEnumerator(), inner.getEnumerator(), outerKeySelector, innerKeySelector, resultSelector, comparer))

fun <TOuter, TInner, TKey, TResult> IEnumerable<TOuter>.groupJoin(
        inner: IEnumerable<TInner>, outerKeySelector: (TOuter) -> TKey, innerKeySelector: (TInner) -> TKey, resultSelector: (TOuter, IEnumerable<TInner>) -> TResult)
        = groupJoin(inner, outerKeySelector, innerKeySelector, resultSelector, EqualityComparer<TKey>())

fun <TOuter, TInner, TKey, TResult> IEnumerable<TOuter>.groupJoin(
        inner: IEnumerable<TInner>, outerKeySelector: (TOuter) -> TKey, innerKeySelector: (TInner) -> TKey, resultSelector: (TOuter, IEnumerable<TInner>) -> TResult, comparer: IEqualityComparer<TKey>)
        : IEnumerable<TResult>
        = Enumerable(GroupJoinEnumerator(getEnumerator(), inner, outerKeySelector, innerKeySelector, resultSelector, comparer))

fun <TSource> IEnumerable<TSource>.concat(second: IEnumerable<TSource>): IEnumerable<TSource>
        = Enumerable(ConcatEnumerator(getEnumerator(), second.getEnumerator()))

inline fun <reified TSource> IEnumerable<TSource>.defaultIfEmpty(): IEnumerable<TSource?> {
    if (getEnumerator().moveNext()) {
        getEnumerator().reset()
        // TSource→TSource?
        return this.select { x -> x }
    } else {
        getEnumerator().reset()
        return arrayOfNulls<TSource>(1).toEnumerable()
    }
}

inline fun <reified TSource> IEnumerable<TSource>.defaultIfEmpty(defaultValue: TSource): IEnumerable<TSource> {
    if (getEnumerator().moveNext()) {
        getEnumerator().reset()
        // TSource→TSource?
        return this.select { x -> x }
    } else {
        getEnumerator().reset()
        return arrayOf(defaultValue).toEnumerable()
    }
}

fun <TFirst, TSecond, TResult> IEnumerable<TFirst>.zip(second: IEnumerable<TSecond>, resultSelector: (TFirst, TSecond) -> TResult): IEnumerable<TResult>
        = Enumerable(ZipEnumerator(getEnumerator(), second.getEnumerator(), resultSelector))

//明示する場合型パラメーター二つ必要になる
//IEnumerableで実装すべきだった恐れ
inline fun <TSource, reified TResult> IEnumerable<TSource>.ofType(): IEnumerable<TResult> = _ofType()

//明示する場合型パラメーター二つ必要になる
//IEnumerableで実装すべきだった恐れ
inline fun <TSource, reified TResult> IEnumerable<TSource>.cast(): IEnumerable<TResult> = _cast()

fun <TSource> IEnumerable<TSource>.toList(): List<TSource> {
    var list = ArrayList<TSource>()
    var enumerator: IEnumerator<TSource> = getEnumerator()
    while (enumerator.moveNext()) {
        list.add(enumerator.current)
    }
    enumerator.reset()
    return list
}

fun <TSource, TKey> IEnumerable<TSource>.toDictionary(
        keySelector: (TSource) -> TKey,
        comparer: IEqualityComparer<TKey> = EqualityComparer<TKey>()): Map<TKey, TSource> {
    return toDictionary(keySelector, { x -> x }, comparer)
}

fun <TSource, TKey, TElement> IEnumerable<TSource>.toDictionary(keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement)
        = toDictionary(keySelector, elementSelector, EqualityComparer<TKey>())

fun <TSource, TKey, TElement> IEnumerable<TSource>.toDictionary(keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement, comparer: IEqualityComparer<TKey>)
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

inline fun<reified TSource> IEnumerable<TSource>.toArray(): Array<TSource> {
    var list: List<TSource> = toList()
    return Array(list.size, { i -> list[i] })
}

fun <TSource, TKey> IEnumerable<TSource>.toLookup(keySelector: (TSource) -> TKey) = toLookup(keySelector, EqualityComparer<TKey>())

fun <TSource, TKey> IEnumerable<TSource>.toLookup(keySelector: (TSource) -> TKey, comparer: IEqualityComparer<TKey>): ILookup<TKey, TSource> {
    return Lookup<TSource, TKey, TSource>(this, keySelector, { x -> x }, comparer)
}

fun <TSource, TKey, TElement> IEnumerable<TSource>.toLookup(keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement)
        = toLookup(keySelector, elementSelector, EqualityComparer<TKey>())

fun <TSource, TKey, TElement> IEnumerable<TSource>.toLookup(
        keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement, comparer: IEqualityComparer<TKey>): ILookup<TKey, TElement> {
    return Lookup<TSource, TKey, TElement>(this, keySelector, elementSelector, comparer)
}

fun <TSource> IEnumerable<TSource>.asEnumerable(): IEnumerable<TSource> {
    return this
}

fun <TSource> IEnumerable<TSource>.forEach(action: (TSource) -> Unit) {
    return forEach { t, i -> action(t) }
}

fun <TSource> IEnumerable<TSource>.forEach(action: (TSource, Int) -> Unit) {
    var enumerator: IEnumerator<TSource> = getEnumerator()
    var index = 0
    while (enumerator.moveNext()) {
        action(enumerator.current, index)
        index++
    }
    enumerator.reset()
}

