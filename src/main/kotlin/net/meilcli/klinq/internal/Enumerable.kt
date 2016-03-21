package net.meilcli.klinq.internal

import net.meilcli.klinq.*
import java.util.*

internal class GroupByEnumerable<TSource, TKey, TElement>(
        val source: IEnumerable<TSource>, val keySelector: (TSource) -> TKey, val elementSelector: (TSource) -> TElement, val comparer: IEqualityComparer<TKey>) :
        IEnumerable<IGrouping<TKey, TElement>> {
    override fun getEnumerator(): IEnumerator<IGrouping<TKey, TElement>> {
        return Lookup<TSource, TKey, TElement>(source, keySelector, elementSelector, comparer).getEnumerator()
    }
}

internal class GroupByEnumerable2<TSource, TKey, TElement, TResult>(
        val source: IEnumerable<TSource>, val keySelector: (TSource) -> TKey, val elementSelector: (TSource) -> TElement,
        val resultSelector: (TKey, TElement) -> TResult, val comparer: IEqualityComparer<TKey>) : IEnumerable<TResult> {
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

