# Token generate and parse
## supported payload type
+ Long
+ Int
+ Byte
+ String
## usage
+ set global formula
```kotlin
Tokenizer.setFormula(listOf(Long::class, String::class, Int::class))
```
+ generate token
```kotlin
Tokenizer.generator(listOf(1L,"someusername",Role.USER.ordinal))
// will return null if formula is invalid,else return token String
```
+ parse token
```kotlin
Tokenizer.parse(someToken)
// will return null if token is invalid,else return formula value with BsonShortId
```
## benchmark
```bash
-------with String-------
32.4092826 ms/op for generate
18.4951459 ms/op for parse
-------with out String-------
2.43097075 ms/op for generate
2.75908355 ms/op for parse
```
