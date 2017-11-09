package io.ghostbuster91.android.react.component.example.typeahead

import io.ghostbuster91.android.react.component.example.common.Reducer
import io.ghostbuster91.android.react.component.example.common.startWith
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class TypeAheadReducer(private val api: TypeAhead.Api,
                       private val scheduler: Scheduler) : Reducer<TypeAhead.Event, TypeAhead.ValidationState> {
    override fun invoke(events: Observable<TypeAhead.Event>, states: Observable<TypeAhead.ValidationState>): Observable<TypeAhead.ValidationState> {
        return events.ofType(TypeAhead.Event.TextChanged::class.java)
                .filter { it.text.isNotEmpty() }
                .switchMap { event ->
                    Single.timer(50, TimeUnit.MILLISECONDS, scheduler)
                            .flatMap {
                                api.call(event.text)
                                        .map { if (it) TypeAhead.ValidationState.FREE else TypeAhead.ValidationState.OCCUPIED }
                                        .onErrorReturn { TypeAhead.ValidationState.ERROR }
                            }
                            .startWith(TypeAhead.ValidationState.LOADING)
                }
    }

}