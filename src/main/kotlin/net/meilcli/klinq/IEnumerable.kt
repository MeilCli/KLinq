package net.meilcli.klinq

interface IEnumerable<T> {

    fun getEnumerator(): IEnumerator<T>

}