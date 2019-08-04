```kotlin

sealed class Result<out E, out A> {
    data class Error<out E>(val error: E) : Result<E, Nothing>()
    data class Success<out A>(val value: A) : Result<Nothing, A>()
}
```
