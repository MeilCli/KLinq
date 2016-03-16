package com.twitter.meil_mitu.klinq

open class Enumerable<T> : IEnumerable<T> {

    private val enumerator: IEnumerator<T>

    constructor(func: () -> Iterator<T>) {
        enumerator = object : IEnumerator<T> {
            private var iterator: Iterator<T> = func()
            private var _current: T? = null
            override var current: T
                get() = _current!!
                set(value) {
                    _current = value
                }

            override fun moveNext(): Boolean {
                if (iterator.hasNext() == false) return false
                _current = iterator.next()
                return true
            }

            override fun reset() {
                iterator = func()
            }
        }
    }

    constructor(iterable: Iterable<T>) : this({ iterable.iterator() }) {
    }

    constructor(enumerator: IEnumerator<T>) {
        this.enumerator = enumerator;
    }

    companion object {
        fun range(start: Int, count: Int): IEnumerable<Int> {
            var enumerator = object : IEnumerator<Int> {
                private var n: Int = 0
                override var current: Int = start - 1

                override fun moveNext(): Boolean {
                    if (n < count) {
                        n++
                        current++
                        return true
                    }
                    return false
                }

                override fun reset() {
                    n = 0
                    current = start - 1
                }
            }
            return Enumerable<Int>(enumerator)
        }

        fun <TResult> repeat(element: TResult, count: Int): IEnumerable<TResult> {
            var enumerator = object : IEnumerator<TResult> {
                private var n: Int = 0
                override var current: TResult = element

                override fun moveNext(): Boolean {
                    if (n < count) {
                        n++
                        return true
                    }
                    return false
                }

                override fun reset() {
                    n = 0
                }
            }
            return Enumerable<TResult>(enumerator)
        }

        fun <TResult> empty(): IEnumerable<TResult> {
            var enumerator = object : IEnumerator<TResult> {
                override var current: TResult
                    get() = throw UnsupportedOperationException()
                    set(value) {
                    }

                override fun moveNext(): Boolean {
                    return false
                }

                override fun reset() {
                }
            }
            return Enumerable<TResult>(enumerator)
        }

    }

    override fun getEnumerator(): IEnumerator<T> {
        return enumerator
    }

}