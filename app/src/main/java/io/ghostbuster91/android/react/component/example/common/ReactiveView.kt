package io.ghostbuster91.android.react.component.example.common

import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable

interface ReactiveView<S> {
    fun bind(events: Relay<Any>, states: Observable<S>, identifier: String)
}