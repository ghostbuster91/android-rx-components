package io.ghostbuster91.android.react.component.example.typeahead

import io.ghostbuster91.android.react.component.example.common.Reducer
import io.ghostbuster91.android.react.component.example.common.startWith
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead.ValidationState
import io.reactivex.Observable
import io.reactivex.Observable.merge
import io.reactivex.Scheduler
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class TypeAheadReducer(private val api: TypeAhead.Api,
                       private val debounceScheduler: Scheduler,
                       private val ioScheduler: Scheduler,
                       private val uiScheduler: Scheduler) : Reducer<TypeAhead.Event.TextChanged, TypeAhead.ValidationState> {

    override fun invoke(events: Observable<TypeAhead.Event.TextChanged>, states: Observable<TypeAhead.ValidationState>): Observable<TypeAhead.ValidationState> {
        return merge(events
                .filter { it.text.isNotEmpty() }
                .switchMap { event ->
                    Single.timer(DEBOUNCE_TIME, TimeUnit.MILLISECONDS, debounceScheduler)
                            .flatMap {
                                api.call(event.text)
                                        .subscribeOn(ioScheduler)
                                        .observeOn(uiScheduler)
                                        .map { if (it) ValidationState.FREE else ValidationState.OCCUPIED }
                                        .onErrorReturn { ValidationState.ERROR }
                            }
                            .startWith(ValidationState.LOADING)
                }, events.filter { it.text.isEmpty() }.map { ValidationState.IDLE })
    }

    companion object {
        val DEBOUNCE_TIME = 200L
    }
}