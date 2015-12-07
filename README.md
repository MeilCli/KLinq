# KLinq
LINQ for Kotlin

KLinq implement alike C# LINQ  
and Lazy Evaluation

#### The difference 

- this must use [List,Array,Iterable...].toEnumeable()
 + because some function's name conflict stdlib's function
- **OrDefault function, default is only null
- average function, generics is extended Number
 + because JVM generics must not allow over road
- sum function, generics is extended Number and return Double
 + because JVM generics must not allow over road
- defaultIfEmpty function, empty is only null
- ofType,cast function
 + because i cannot write good code
- add forEach function

#### gradle
	repositories {
	    mavenCentral()
	    maven { url "https://raw.github.com/MeilCli/KLinq/master/klinq/repository" }
	}
	
	dependencies {
		compile 'meilcli:klinq:1.0-SNAPSHOT'
	}


ライセンス
----------

This source is The MIT License.

using [The Kotlin Standard Library][Kotlin_stdlib] [Apache License, Version 2.0][Apache]
[Apache]: http://www.apache.org/licenses/LICENSE-2.0
[Kotlin_stdlib]: https://github.com/JetBrains/kotlin/tree/master/libraries/stdlib
