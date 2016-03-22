package net.meilcli.klinq.internal

import net.meilcli.klinq.*
import java.util.*

internal class Lookup<TSource, TKey, TElement> : ILookup<TKey, TElement> {

    private val enumerator: IEnumerator<IGrouping<TKey, TElement>>
    private var list = ArrayList<IGrouping<TKey, TElement>>()
    private var comparer: IEqualityComparer<TKey>
    override var count: Int

    constructor(
            source: IEnumerable<TSource>,
            keySelector: (TSource) -> TKey,
            elementSelector: (TSource) -> TElement,
            comparer: IEqualityComparer<TKey>) {
        this.comparer = comparer
        var map = HashMap<TKey, ArrayList<TElement>>()
        var enumerator: IEnumerator<TSource> = source.getEnumerator()
        while (enumerator.moveNext()) {
            var key: TKey = keySelector(enumerator.current)
            if (map.keys.toEnumerable().contains(key, comparer)) {
                map[key]!!.add(elementSelector(enumerator.current))
            } else {
                map.put(key, ArrayList<TElement>())
                map[key]!!.add(elementSelector(enumerator.current))
            }
        }
        enumerator.reset()
        for (key in map.keys) {
            list.add(Grouping<TKey, TElement>(key, map[key]!!))
        }
        count = list.count()
        this.enumerator = list.toEnumerable().getEnumerator()
    }

    override fun contains(key: TKey): Boolean {
        return list.toEnumerable().any { x -> comparer.equals(x.key, key) }
    }

    override fun get(key: TKey): IEnumerable<TElement> {
        return list.toEnumerable().single { x -> comparer.equals(x.key, key) }
    }

    override fun getEnumerator(): IEnumerator<IGrouping<TKey, TElement>> {
        return enumerator
    }
}