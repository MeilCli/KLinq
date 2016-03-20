package net.meilcli.klinq

interface IGrouping<TKey, TElement> : IEnumerable<TElement> {

    var key: TKey

}