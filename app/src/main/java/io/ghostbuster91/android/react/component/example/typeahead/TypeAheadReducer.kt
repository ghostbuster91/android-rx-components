package io.ghostbuster91.android.react.component.example.typeahead

import io.ghostbuster91.android.react.component.example.common.Reducer
import io.ghostbuster91.android.react.component.example.common.startWith
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead.ValidationState
import io.reactivex.Observable
import io.reactivex.Observable.merge
import io.reactivex.Scheduler
import io.reactivex.Single
import java.util.*
import java.util.concurrent.TimeUnit

class TypeAheadReducer(private val api: TypeAhead.Api,
                       private val debounceScheduler: Scheduler,
                       private val ioScheduler: Scheduler,
                       private val uiScheduler: Scheduler) : Reducer<Any, TypeAhead.ValidationState> {

    override val identifier: String = UUID.randomUUID().toString()

    override fun invoke(events: Observable<out Any>, states: Observable<TypeAhead.ValidationState>): Observable<TypeAhead.ValidationState> {
        val relatedEvent = events
                .ofType(TypeAhead.Event.TextChanged::class.java)
                .filter { it.identifier == identifier }

        return merge(relatedEvent
                .switchMap { event ->
                    if (event.text.isNotEmpty()) {
                        Single.timer(DEBOUNCE_TIME, TimeUnit.MILLISECONDS, debounceScheduler)
                                .flatMap {
                                    api.call(event.text)
                                            .subscribeOn(ioScheduler)
                                            .observeOn(uiScheduler)
                                            .map { if (it) ValidationState.FREE else ValidationState.OCCUPIED }
                                            .onErrorReturn { ValidationState.ERROR }
                                }
                                .startWith(ValidationState.LOADING)
                    } else {
                        Observable.empty()
                    }
                }, relatedEvent
                .filter { it.text.isEmpty() }.map { ValidationState.IDLE })
                .startWith(TypeAhead.ValidationState.IDLE)
    }

    companion object {
        val DEBOUNCE_TIME = 200L
    }
}
