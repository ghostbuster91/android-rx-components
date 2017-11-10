package io.ghostbuster91.android.react.component.example

import android.os.Bundle
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.ghostbuster91.android.react.component.example.typeahead.TypeAheadReducer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.example_activity.*

class ExampleActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.example_activity)
        typeAheadView.bind(events, states.map { it.firstTypeAhead }, firstTypeAheadReducer.identifier)
        secondTypeAheadView.bind(events, states.map { it.secondTypeAhead }, secondTypeAheadReducer.identifier)

        Observable.merge(
                firstTypeAheadReducer
                        .invoke(events, states.map { it.firstTypeAhead }).onLatestFrom(states) { copy(firstTypeAhead = it) },
                secondTypeAheadReducer
                        .invoke(events, states.map { it.secondTypeAhead }).onLatestFrom(states) { copy(secondTypeAhead = it) })
                .bindToLifecycle(this)
                .subscribe(states)
    }
}

val firstTypeAheadReducer by lazy { createTypeAheadReducer() }
val secondTypeAheadReducer by lazy { createTypeAheadReducer() }

private fun createTypeAheadReducer() =
        TypeAheadReducer(
                api = typAheadApiProvider(),
                ioScheduler = Schedulers.io(),
                debounceScheduler = Schedulers.computation(),
                uiScheduler = AndroidSchedulers.mainThread())

fun <T, R> Observable<T>.onLatestFrom(other: Observable<R>, action: R.(T) -> R): Observable<R> {
    return withLatestFrom(other, BiFunction { t1, t2 ->
        t2.action(t1)
    })
}