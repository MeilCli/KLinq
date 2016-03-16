package com.twitter.meil_mitu.klinq

interface ILookup<TKey, TElement> : IEnumerable<IGrouping<TKey, TElement>> {
    var count: Int

    fun get(key: TKey): IEnumerable<TElement>
    fun contains(key: TKey): Boolean
}