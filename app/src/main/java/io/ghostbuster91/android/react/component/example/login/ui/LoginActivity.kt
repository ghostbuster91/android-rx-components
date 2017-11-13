package io.ghostbuster91.android.react.component.example.login.ui

import android.os.Bundle
import com.jakewharton.rxbinding2.view.enabled
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.ghostbuster91.android.react.component.example.*
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead
import io.reactivex.Observable
import kotlinx.android.synthetic.main.example_activity.*

class LoginActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.example_activity)
        uiEvents().bindToLifecycle(this).subscribe(events)
        states
                .bindToLifecycle(this)
                .publish()
                .also {
                    typeAheadView.subscribe(it.map { it.firstTypeAhead })
                    secondTypeAheadView.subscribe(it.map { it.secondTypeAhead })
                    it.map { it.isLoginButtonEnabled }.subscribe(loginButton.enabled())
                }
                .connect()

    }

    private fun uiEvents(): Observable<TypeAhead.Event.TextChanged> {
        return Observable.merge(
                typeAheadView.bindEvents(firstTypeAheadReducer.identifier),
                secondTypeAheadView.bindEvents(secondTypeAheadReducer.identifier))
    }
}