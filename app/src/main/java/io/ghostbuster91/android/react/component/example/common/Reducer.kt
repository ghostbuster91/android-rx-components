package io.ghostbuster91.android.react.component.example.common

import io.reactivex.Observable
import java.util.*

interface Reducer<S> {
    fun invoke(events: Observable<out Any>): Observable<S>
}

interface Identifiable {
    val identifier: String
}

class RandomIdentifier : Identifiable {
    override val identifier: String = UUID.randomUUID().toString()
}