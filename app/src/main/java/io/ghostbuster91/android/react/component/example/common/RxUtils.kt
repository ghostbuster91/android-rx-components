package io.ghostbuster91.android.react.component.example.common

import io.reactivex.Observable
import io.reactivex.Single

fun <T> Single<T>.startWith(item: T): Observable<T> {
    return toObservable().startWith(item)
}
