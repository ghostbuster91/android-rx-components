package io.ghostbuster91.android.react.component.example.common

import io.reactivex.Observable

interface ReactiveView<E, S> {
    fun bindEvents(identifier: String): Observable<E>
    fun subscribe(states: Observable<S>)
}