package net.meilcli.klinq

interface IEnumerator<T> {

    var current: T
    fun moveNext(): Boolean
    fun reset()
}