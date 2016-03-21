package net.meilcli.klinq.internal

import java.util.*

class WrapComparator<TKey : Comparable<TKey>> : Comparator<TKey> {
    override fun compare(o1: TKey, o2: TKey): Int {
        return o1.compareTo(o2)
    }
}