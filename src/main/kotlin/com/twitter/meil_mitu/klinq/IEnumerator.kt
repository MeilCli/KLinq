package com.twitter.meil_mitu.klinq

interface IEnumerator<T> {

    var current: T
    fun moveNext(): Boolean
    fun reset()
}