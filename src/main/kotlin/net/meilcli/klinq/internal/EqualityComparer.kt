package net.meilcli.klinq.internal

import net.meilcli.klinq.IEqualityComparer

internal class EqualityComparer<T> : IEqualityComparer<T> {

    override fun equals(t1: T, t2: T): Boolean {
        return t1!!.equals(t2!!)
    }

    override fun getHashCode(t: T): Int {
        return t!!.hashCode()
    }
}