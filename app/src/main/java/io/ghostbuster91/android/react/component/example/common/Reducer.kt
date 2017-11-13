package io.ghostbuster91.android.react.component.example.common

import io.reactivex.Observable

interface Reducer<in E, S> {
    fun invoke(events: Observable<out E>, states: Observable<S>): Observable<S>
    val identifier: String
}