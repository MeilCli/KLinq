package com.twitter.meil_mitu.klinq

interface IEnumerable<T> {

    fun getEnumerator(): IEnumerator<T>

}