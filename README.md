# KLinq
LINQ for Kotlin

KLinq implement alike C# LINQ  
and Deferred(Lazy) Execution  
[日本語解説](http://meilcli.net/product/klinq.html)

#### The difference 
- this must use [List,Array,Iterable...].toEnumeable()
 + because some function's name conflict stdlib's function
- **OrDefault function, default is only null
- average function, generics is extended Number
 + because JVM generics must not allow overload
- sum function, generics is extended Number and return Double
 + because JVM generics must not allow overload
- defaultIfEmpty function, empty is only null
- ofType,cast function
 + because extension function
- add forEach function

#### gradle
	dependencies {
		compile 'net.meilcli:klinq:1.5'
	}

#### Usage
	import net.meilcli.klinq.*
	
	var linq  = arrayOf(1,2).toEnumerable()
	linq.where{ x -> x>0}...

#### License

This source is The MIT License.

using [The Kotlin Standard Library](https://github.com/JetBrains/kotlin/tree/master/libraries/stdlib) : [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
