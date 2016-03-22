package net.meilcli.klinq.internal

import net.meilcli.klinq.IEnumerator

internal class EnumerableIterator<TSource>(val enumrator: IEnumerator<TSource>) : Iterator<TSource> {

    init {
        enumrator.reset()
    }

    override fun next() = enumrator.current

    override fun hasNext() = enumrator.moveNext()

}

