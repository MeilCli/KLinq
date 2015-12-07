package com.twitter.meil_mitu.klinq

class Grouping<TKey, TElement> : Enumerable<TElement>, IGrouping<TKey, TElement> {

    override var key: TKey

    constructor(key: TKey, iterable: Iterable<TElement>) : super(iterable) {
        this.key = key
    }
}