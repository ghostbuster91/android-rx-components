package io.ghostbuster91.android.react.component.example

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

val events by lazy { PublishRelay.create<Any>() }
val states: BehaviorRelay<TypeAhead.ValidationState> by lazy {
    BehaviorRelay.createDefault(TypeAhead.initialState)
}
var typAheadApiProvider: () -> TypeAhead.Api = {
    object : TypeAhead.Api {
        override fun call(s: String): Single<Boolean> {
            return Single.never()
        }
    }
}
var typeAheadSchedulerProvider: () -> Scheduler = { Schedulers.io() }