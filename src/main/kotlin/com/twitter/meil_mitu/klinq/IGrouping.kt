package com.twitter.meil_mitu.klinq

interface IGrouping<TKey, TElement> : IEnumerable<TElement> {

    var key: TKey

}