package io.ghostbuster91.android.react.component.example

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead
import io.reactivex.Single
import java.util.*
import java.util.concurrent.TimeUnit

val events by lazy { PublishRelay.create<Any>() }
val states: BehaviorRelay<State> by lazy {
    BehaviorRelay.createDefault(State(TypeAhead.initialState, TypeAhead.initialState))
}
var typAheadApiProvider: () -> TypeAhead.Api = {
    object : TypeAhead.Api {
        val random = Random()
        override fun call(s: String): Single<Boolean> {
            val randomDelay = Math.abs(random.nextLong() % 100) + 100
            return when (s) {
                "internet" -> Single.error<Boolean>(RuntimeException()).delay(randomDelay, TimeUnit.MILLISECONDS)
                "kasper" -> Single.just(false).delay(randomDelay, TimeUnit.MILLISECONDS)
                else -> Single.just(true).delay(randomDelay, TimeUnit.MILLISECONDS)
            }
        }
    }
}

data class State(val firstTypeAhead: TypeAhead.ValidationState,
                 val secondTypeAhead: TypeAhead.ValidationState)