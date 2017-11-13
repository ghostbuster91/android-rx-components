package io.ghostbuster91.android.react.component.example

import io.ghostbuster91.android.react.component.example.common.Reducer
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables

class ExampleReducer(private val firstTypeAheadReducer: Reducer<TypeAhead.ValidationState>,
                     private val secondTypeAheadReducer: Reducer<TypeAhead.ValidationState>) : Reducer<State> {
    override fun invoke(events: Observable<out Any>): Observable<State> {
        return Observables.combineLatest(
                firstTypeAheadReducer.invoke(events),
                secondTypeAheadReducer.invoke(events),
                { a, b -> State(a, b, a == TypeAhead.ValidationState.FREE && b == TypeAhead.ValidationState.FREE) })
    }
}