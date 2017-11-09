package io.ghostbuster91.android.react.component.example.common

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import io.ghostbuster91.android.react.component.example.State
import io.reactivex.Observable

interface ReactiveView<S> {
    fun bind(events: Relay<Any>, states: Observable<S>, function: (S) -> State, globalState: BehaviorRelay<State>)
}