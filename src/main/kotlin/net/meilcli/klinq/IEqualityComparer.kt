package net.meilcli.klinq

interface IEqualityComparer<T> {

    fun equals(t1: T, t2: T): Boolean
    fun getHashCode(t: T): Int

}