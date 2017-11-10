package io.ghostbuster91.android.react.component.example

import android.os.Bundle
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.ghostbuster91.android.react.component.example.common.onLatestFrom
import io.reactivex.Observable
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