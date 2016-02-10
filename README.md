# KLinq
LINQ for Kotlin

KLinq implement alike C# LINQ  
and Deferred(Lazy) Execution  
そのうちドキュメントは書く(ちゃんと書くとは言っていない)

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
	repositories {
	    mavenCentral()
	    maven { url "https://raw.github.com/MeilCli/KLinq/master/klinq/repository" }
	}
	
	dependencies {
		compile 'meilcli:klinq:1.2'
	}

#### Usage
	import com.twitter.meil_mitu.klinq.*
	
	var linq  = arrayOf(1,2).toEnumerable()
	linq.where{ x -> x>0}...

#### License

This source is The MIT License.

using [The Kotlin Standard Library][Kotlin_stdlib] [Apache License, Version 2.0][Apache]
[Apache]: http://www.apache.org/licenses/LICENSE-2.0
[Kotlin_stdlib]: https://github.com/JetBrains/kotlin/tree/master/libraries/stdlib
