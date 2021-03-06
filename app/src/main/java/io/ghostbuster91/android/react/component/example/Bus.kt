package io.ghostbuster91.android.react.component.example

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.ghostbuster91.android.react.component.example.login.Login
import io.ghostbuster91.android.react.component.example.login.LoginReducer
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead
import io.ghostbuster91.android.react.component.example.typeahead.TypeAheadReducer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

val events by lazy { PublishRelay.create<Any>() }
val states: BehaviorRelay<Login.State> by lazy {
    BehaviorRelay.create<Login.State>()
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

val firstTypeAheadReducer by lazy { createTypeAheadReducer() }
val secondTypeAheadReducer by lazy { createTypeAheadReducer() }
val exampleReducer by lazy { LoginReducer(firstTypeAheadReducer, secondTypeAheadReducer) }

private fun createTypeAheadReducer() =
        TypeAheadReducer(
                api = typAheadApiProvider(),
                ioScheduler = Schedulers.io(),
                debounceScheduler = Schedulers.computation(),
                uiScheduler = AndroidSchedulers.mainThread())

