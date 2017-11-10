package io.ghostbuster91.android.react.component.example.common

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction

fun <T> Single<T>.startWith(item: T): Observable<T> {
    return toObservable().startWith(item)
}

fun <T, R> Observable<T>.onLatestFrom(other: Observable<R>, action: R.(T) -> R): Observable<R> {
    return withLatestFrom(other, BiFunction { t1, t2 ->
        t2.action(t1)
    })
}