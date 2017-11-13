package io.ghostbuster91.android.react.component.example.login

import io.ghostbuster91.android.react.component.example.common.Reducer
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables.combineLatest

class LoginReducer(private val firstTypeAheadReducer: Reducer<TypeAhead.ValidationState>,
                   private val secondTypeAheadReducer: Reducer<TypeAhead.ValidationState>) : Reducer<Login.State> {
    override fun invoke(events: Observable<out Any>): Observable<Login.State> {
        return combineLatest(
                firstTypeAheadReducer.invoke(events),
                secondTypeAheadReducer.invoke(events),
                { a, b -> Login.State(a, b, a == TypeAhead.ValidationState.FREE && b == TypeAhead.ValidationState.FREE) })
    }
}