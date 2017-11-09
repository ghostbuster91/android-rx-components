package io.ghostbuster91.android.react.component.example.common

import io.reactivex.Observable

typealias Reducer<E, S> = (events: Observable<E>, states: Observable<S>) -> Observable<S>