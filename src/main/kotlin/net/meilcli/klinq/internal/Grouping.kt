package net.meilcli.klinq.internal

import net.meilcli.klinq.Enumerable
import net.meilcli.klinq.IGrouping

internal class Grouping<TKey, TElement> : Enumerable<TElement>, IGrouping<TKey, TElement> {

    override var key: TKey

    constructor(key: TKey, iterable: Iterable<TElement>) : super(iterable) {
        this.key = key
    }
}