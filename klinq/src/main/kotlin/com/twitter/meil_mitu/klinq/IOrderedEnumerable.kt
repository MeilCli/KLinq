package com.twitter.meil_mitu.klinq

import java.util.*

interface IOrderedEnumerable<TElement> : IEnumerable<TElement> {
    fun <TKey> createOrderedEnumerable(keySelector: (TElement) -> TKey, comparator: Comparator<TKey>, descending: Boolean): IOrderedEnumerable<TElement>
}