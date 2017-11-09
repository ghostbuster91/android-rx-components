package io.ghostbuster91.android.react.component.example.common

import com.jakewharton.rxrelay2.Relay

interface ReactiveView<S> {
    fun bind(events: Relay<Any>, states: Relay<S>)
}