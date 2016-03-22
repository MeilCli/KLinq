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


fun <TSource> Iterable<TSource>.toEnumerable(): IEnumerable<TSource> = Enumerable<TSource>(this)

fun <TSource> Array<TSource>.toEnumerable(): IEnumerable<TSource> = Enumerable<TSource>({ iterator() })

fun BooleanArray.toEnumerable(): IEnumerable<Boolean> = Enumerable({ iterator() })

fun CharArray.toEnumerable(): IEnumerable<Char> = Enumerable({ iterator() })

fun ByteArray.toEnumerable(): IEnumerable<Byte> = Enumerable({ iterator() })

fun ShortArray.toEnumerable(): IEnumerable<Short> = Enumerable({ iterator() })

fun IntArray.toEnumerable(): IEnumerable<Int> = Enumerable({ iterator() })

fun LongArray.toEnumerable(): IEnumerable<Long> = Enumerable({ iterator() })

fun FloatArray.toEnumerable(): IEnumerable<Float> = Enumerable({ iterator() })

fun DoubleArray.toEnumerable(): IEnumerable<Double> = Enumerable({ iterator() })

fun <TSource> IEnumerable<TSource>.elementAt(index: Int): TSource = _elementAt(index)

fun <TSource> IEnumerable<TSource>.elementAtOrDefault(index: Int): TSource? = _elementAtOrDefault(index)

fun <TSource> IEnumerable<TSource>.first() = first(null)

fun <TSource> IEnumerable<TSource>.first(predicate: ((TSource) -> Boolean)?): TSource = _first(predicate)

fun <TSource> IEnumerable<TSource>.firstOrDefault() = firstOrDefault(null)

fun <TSource> IEnumerable<TSource>.firstOrDefault(predicate: ((TSource) -> Boolean)?): TSource? = _firstOrDefault(predicate)

fun <TSource> IEnumerable<TSource>.last() = last(null)

fun <TSource> IEnumerable<TSource>.last(predicate: ((TSource) -> Boolean)?): TSource = _last(predicate)

fun <TSource> IEnumerable<TSource>.lastOrDefault() = lastOrDefault(null)

fun <TSource> IEnumerable<TSource>.lastOrDefault(predicate: ((TSource) -> Boolean)?): TSource? = _lastOrDefault(predicate)

fun <TSource> IEnumerable<TSource>.single() = single(null)

fun <TSource> IEnumerable<TSource>.single(predicate: ((TSource) -> Boolean)?): TSource = _single(predicate)

fun <TSource> IEnumerable<TSource>.singleOrDefault() = singleOrDefault(null)

fun <TSource> IEnumerable<TSource>.singleOrDefault(predicate: ((TSource) -> Boolean)?): TSource? = _singleOrDefault(predicate)

fun <TSource> IEnumerable<TSource>.where(predicate: (TSource) -> Boolean) = where { t, i -> predicate(t) }

fun <TSource> IEnumerable<TSource>.where(predicate: (TSource, Int) -> Boolean): IEnumerable<TSource>
        = Enumerable(WhereEnumerator(getEnumerator(), predicate))

fun <TSource> IEnumerable<TSource>.distinct() = distinct(EqualityComparer<TSource>())

fun <TSource> IEnumerable<TSource>.distinct(comparer: IEqualityComparer<TSource>): IEnumerable<TSource>
        = Enumerable(DistinctEnumerator(getEnumerator(), comparer))

fun <TSource> IEnumerable<TSource>.skip(count: Int) = skipWhile { t, i -> i < count }

fun <TSource> IEnumerable<TSource>.skipWhile(predicate: (TSource) -> Boolean) = skipWhile { t, i -> predicate(t) }

fun <TSource> IEnumerable<TSource>.skipWhile(predicate: (TSource, Int) -> Boolean): IEnumerable<TSource>
        = Enumerable(SkipWhileEnumerator(getEnumerator(), predicate))

fun <TSource> IEnumerable<TSource>.take(count: Int) = takeWhile { t, i -> i < count }

fun <TSource> IEnumerable<TSource>.takeWhile(predicate: (TSource) -> Boolean) = takeWhile { t, i -> predicate(t) }

fun <TSource> IEnumerable<TSource>.takeWhile(predicate: (TSource, Int) -> Boolean): IEnumerable<TSource>
        = Enumerable(TakeWhileEnumerator(getEnumerator(), predicate))

fun <TSource : Comparable<TSource>> IEnumerable<TSource>.max() = max { x -> x }

fun <TSource, TResult : Comparable<TResult>> IEnumerable<TSource>.max(selector: (TSource) -> TResult): TResult = _max(selector)

fun <TSource : Comparable<TSource>> IEnumerable<TSource>.min() = min { x -> x }

fun <TSource, TResult : Comparable<TResult>> IEnumerable<TSource>.min(selector: (TSource) -> TResult): TResult = _min(selector)

fun <TSource : Number> IEnumerable<TSource>.average() = average { x -> x }

fun <TSource> IEnumerable<TSource>.average(selector: (TSource) -> Number): Double = _average(selector)

fun <TSource : Number> IEnumerable<TSource>.sum() = sum { x -> x }

fun <TSource> IEnumerable<TSource>.sum(selector: (TSource) -> Number): Double = _sum(selector)

fun <TSource> IEnumerable<TSource>.count() = count(null)

fun <TSource> IEnumerable<TSource>.count(predicate: ((TSource) -> Boolean)?): Int = _count(predicate)

fun <TSource> IEnumerable<TSource>.aggregate(func: (TSource, TSource) -> TSource): TSource = _aggregate(func)

fun <TSource, TAccumulate> IEnumerable<TSource>.aggregate(seed: TAccumulate, func: (TAccumulate, TSource) -> TAccumulate): TAccumulate = aggregate(seed, func, { x -> x })

fun <TSource, TAccumulate, TResult> IEnumerable<TSource>.aggregate(seed: TAccumulate, func: (TAccumulate, TSource) -> TAccumulate, resultSelector: (TAccumulate) -> TResult): TResult
        = _aggregate(seed, func, resultSelector)

fun <TSource> IEnumerable<TSource>.all(predicate: (TSource) -> Boolean): Boolean = _all(predicate)

fun <TSource> IEnumerable<TSource>.any() = any(null)

fun <TSource> IEnumerable<TSource>.any(predicate: ((TSource) -> Boolean)?): Boolean = _any(predicate)

fun <TSource> IEnumerable<TSource>.contains(value: TSource) = contains(value, EqualityComparer<TSource>())

fun <TSource> IEnumerable<TSource>.contains(value: TSource, comparer: IEqualityComparer<TSource>) = any { x -> comparer.equals(x, value) }

fun <TSource> IEnumerable<TSource>.sequenceEqual(second: IEnumerable<TSource>) = sequenceEqual(second, EqualityComparer<TSource>())

fun <TSource> IEnumerable<TSource>.sequenceEqual(second: IEnumerable<TSource>, comparer: IEqualityComparer<TSource>): Boolean = _sequenceEqual(second, comparer)

fun <TSource> IEnumerable<TSource>.union(second: IEnumerable<TSource>) = union(second, EqualityComparer<TSource>())

fun <TSource> IEnumerable<TSource>.union(second: IEnumerable<TSource>, comparer: IEqualityComparer<TSource>): IEnumerable<TSource>
        = Enumerable(UnionEnumerator(getEnumerator(), second.getEnumerator(), comparer))

fun <TSource> IEnumerable<TSource>.except(second: IEnumerable<TSource>) = except(second, EqualityComparer<TSource>())

fun <TSource> IEnumerable<TSource>.except(second: IEnumerable<TSource>, comparer: IEqualityComparer<TSource>): IEnumerable<TSource>
        = Enumerable(ExceptEnumerator(getEnumerator(), second, comparer))

fun <TSource> IEnumerable<TSource>.intersect(second: IEnumerable<TSource>) = intersect(second, EqualityComparer<TSource>())

fun <TSource> IEnumerable<TSource>.intersect(second: IEnumerable<TSource>, comparer: IEqualityComparer<TSource>): IEnumerable<TSource>
        = Enumerable(IntersectEnumerator(getEnumerator(), second, comparer))

fun <TSource, TKey : Comparable<TKey>> IEnumerable<TSource>.orderBy(keySelector: (TSource) -> TKey): IOrderedEnumerable<TSource>
        = OrderedEnumerable<TSource, TKey>(this, keySelector, WrapComparator(), false)

fun <TSource, TKey> IEnumerable<TSource>.orderBy(keySelector: (TSource) -> TKey, comparator: Comparator<TKey>): IOrderedEnumerable<TSource>
        = OrderedEnumerable<TSource, TKey>(this, keySelector, comparator, false)

fun <TSource, TKey : Comparable<TKey>> IEnumerable<TSource>.orderByDescending(keySelector: (TSource) -> TKey): IOrderedEnumerable<TSource>
        = OrderedEnumerable<TSource, TKey>(this, keySelector, WrapComparator(), true)

fun <TSource, TKey> IEnumerable<TSource>.orderByDescending(keySelector: (TSource) -> TKey, comparator: Comparator<TKey>): IOrderedEnumerable<TSource>
        = OrderedEnumerable<TSource, TKey>(this, keySelector, comparator, true)

fun<TSource, TKey : Comparable<TKey>> IOrderedEnumerable<TSource>.thenBy(keySelector: (TSource) -> TKey): IOrderedEnumerable<TSource>
        = createOrderedEnumerable(keySelector, WrapComparator(), false)

fun <TSource, TKey> IOrderedEnumerable<TSource>.thenBy(keySelector: (TSource) -> TKey, comparator: Comparator<TKey>): IOrderedEnumerable<TSource>
        = createOrderedEnumerable(keySelector, comparator, false)

fun <TSource, TKey : Comparable<TKey>> IOrderedEnumerable<TSource>.thenByDescending(keySelector: (TSource) -> TKey): IOrderedEnumerable<TSource>
        = createOrderedEnumerable(keySelector, WrapComparator(), true)

fun <TSource, TKey> IOrderedEnumerable<TSource>.thenByDescending(keySelector: (TSource) -> TKey, comparator: Comparator<TKey>): IOrderedEnumerable<TSource>
        = createOrderedEnumerable(keySelector, comparator, true)

fun <TSource> IEnumerable<TSource>.reverse(): IEnumerable<TSource> = Enumerable<TSource>(toList().reversed())

fun <TSource, TResult> IEnumerable<TSource>.select(selector: (TSource) -> TResult) = select { t, i -> selector(t) }

fun <TSource, TResult> IEnumerable<TSource>.select(selector: (TSource, Int) -> TResult): IEnumerable<TResult>
        = Enumerable(SelectEnumerator(getEnumerator(), selector))

fun <TSource, TResult> IEnumerable<TSource>.selectMany(selector: (TSource) -> IEnumerable<TResult>) = selectMany { x, y -> selector(x) }

fun <TSource, TResult> IEnumerable<TSource>.selectMany(selector: (TSource, Int) -> IEnumerable<TResult>) = selectMany(selector, { x, y -> y })

fun <TSource, TCollection, TResult> IEnumerable<TSource>.selectMany(collectionSelector: (TSource) -> IEnumerable<TCollection>, resultSelector: (TSource, TCollection) -> TResult)
        = selectMany({ x, y -> collectionSelector(x) }, resultSelector)

fun <TSource, TCollection, TResult> IEnumerable<TSource>.selectMany(
        collectionSelector: (TSource, Int) -> IEnumerable<TCollection>, resultSelector: (TSource, TCollection) -> TResult): IEnumerable<TResult>
        = Enumerable(SelectManyEnumerator(getEnumerator(), collectionSelector, resultSelector))

//キャッシュするならIEnumeratorから実装しなければ…
fun <TSource, TKey> IEnumerable<TSource>.groupBy(keySelector: (TSource) -> TKey) = groupBy(keySelector, EqualityComparer<TKey>())

fun <TSource, TKey> IEnumerable<TSource>.groupBy(keySelector: (TSource) -> TKey, comparer: IEqualityComparer<TKey>) = groupBy(keySelector, { x -> x }, comparer)

fun <TSource, TKey, TElement> IEnumerable<TSource>.groupBy(keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement)
        = groupBy(keySelector, elementSelector, EqualityComparer<TKey>())

fun <TSource, TKey, TElement> IEnumerable<TSource>.groupBy(keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement, comparer: IEqualityComparer<TKey>)
        : IEnumerable<IGrouping<TKey, TElement>>
        = GroupByEnumerable(this, keySelector, elementSelector, comparer)

fun <TSource, TKey, TResult> IEnumerable<TSource>.groupBy(keySelector: (TSource) -> TKey, resultSelector: (TKey, TSource) -> TResult)
        = groupBy(keySelector, resultSelector, EqualityComparer<TKey>())

fun <TSource, TKey, TResult> IEnumerable<TSource>.groupBy(keySelector: (TSource) -> TKey, resultSelector: (TKey, TSource) -> TResult, comparer: IEqualityComparer<TKey>)
        = groupBy(keySelector, { x -> x }, resultSelector, comparer)

fun <TSource, TKey, TElement, TResult> IEnumerable<TSource>.groupBy(
        keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement, resultSelector: (TKey, TElement) -> TResult)
        = groupBy(keySelector, elementSelector, resultSelector, EqualityComparer<TKey>())

fun <TSource, TKey, TElement, TResult> IEnumerable<TSource>.groupBy(
        keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement, resultSelector: (TKey, TElement) -> TResult, comparer: IEqualityComparer<TKey>):
        IEnumerable<TResult>
        = GroupByEnumerable2(this, keySelector, elementSelector, resultSelector, comparer)

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

inline fun <reified TSource> IEnumerable<TSource>.defaultIfEmpty(): IEnumerable<TSource?> = _defaultIfEmpty()

inline fun <reified TSource> IEnumerable<TSource>.defaultIfEmpty(defaultValue: TSource): IEnumerable<TSource> = _defaultIfEmpty(defaultValue)

fun <TFirst, TSecond, TResult> IEnumerable<TFirst>.zip(second: IEnumerable<TSecond>, resultSelector: (TFirst, TSecond) -> TResult): IEnumerable<TResult>
        = Enumerable(ZipEnumerator(getEnumerator(), second.getEnumerator(), resultSelector))

//明示する場合型パラメーター二つ必要になる
//IEnumerableで実装すべきだった恐れ
inline fun <TSource, reified TResult> IEnumerable<TSource>.ofType(): IEnumerable<TResult> = _ofType()

//明示する場合型パラメーター二つ必要になる
//IEnumerableで実装すべきだった恐れ
inline fun <TSource, reified TResult> IEnumerable<TSource>.cast(): IEnumerable<TResult> = _cast()

fun <TSource> IEnumerable<TSource>.toList(): List<TSource> = _toList()

fun <TSource, TKey> IEnumerable<TSource>.toDictionary(keySelector: (TSource) -> TKey) = toDictionary(keySelector, EqualityComparer<TKey>())

fun <TSource, TKey> IEnumerable<TSource>.toDictionary(keySelector: (TSource) -> TKey, comparer: IEqualityComparer<TKey>)
        = toDictionary(keySelector, { x -> x }, comparer)

fun <TSource, TKey, TElement> IEnumerable<TSource>.toDictionary(keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement)
        = toDictionary(keySelector, elementSelector, EqualityComparer<TKey>())

fun <TSource, TKey, TElement> IEnumerable<TSource>.toDictionary(keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement, comparer: IEqualityComparer<TKey>)
        : Map<TKey, TElement>
        = _toDictionary(keySelector, elementSelector, comparer)

inline fun<reified TSource> IEnumerable<TSource>.toArray(): Array<TSource> = _toArray()

fun <TSource, TKey> IEnumerable<TSource>.toLookup(keySelector: (TSource) -> TKey) = toLookup(keySelector, EqualityComparer<TKey>())

fun <TSource, TKey> IEnumerable<TSource>.toLookup(keySelector: (TSource) -> TKey, comparer: IEqualityComparer<TKey>): ILookup<TKey, TSource>
        = Lookup<TSource, TKey, TSource>(this, keySelector, { x -> x }, comparer)

fun <TSource, TKey, TElement> IEnumerable<TSource>.toLookup(keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement)
        = toLookup(keySelector, elementSelector, EqualityComparer<TKey>())

fun <TSource, TKey, TElement> IEnumerable<TSource>.toLookup(
        keySelector: (TSource) -> TKey, elementSelector: (TSource) -> TElement, comparer: IEqualityComparer<TKey>): ILookup<TKey, TElement>
        = Lookup<TSource, TKey, TElement>(this, keySelector, elementSelector, comparer)

fun <TSource> IEnumerable<TSource>.asEnumerable(): IEnumerable<TSource> = this

fun <TSource> IEnumerable<TSource>.forEach(action: (TSource) -> Unit) = forEach { t, i -> action(t) }

fun <TSource> IEnumerable<TSource>.forEach(action: (TSource, Int) -> Unit) = _forEach(action)

