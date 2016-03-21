package net.meilcli.klinq.internal

import net.meilcli.klinq.IGrouping

class Grouping<TKey, TElement> : Enumerable<TElement>, IGrouping<TKey, TElement> {

    override var key: TKey

    constructor(key: TKey, iterable: Iterable<TElement>) : super(iterable) {
        this.key = key
    }
}