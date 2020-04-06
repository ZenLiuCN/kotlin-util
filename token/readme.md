# Token generate and parse
## usage
+ set global formula
```kotlin
Tokenizer.setFormula(listOf(Long::class, String::class, String::class))
```
+ generate token
```kotlin
Tokenizer.generator(someToken)
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
