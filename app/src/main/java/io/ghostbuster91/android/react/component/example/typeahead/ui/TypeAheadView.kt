package io.ghostbuster91.android.react.component.example.typeahead.ui

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.jakewharton.rxbinding2.widget.textChanges
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.ghostbuster91.android.react.component.example.R
import io.ghostbuster91.android.react.component.example.State
import io.ghostbuster91.android.react.component.example.common.ReactiveView
import io.ghostbuster91.android.react.component.example.typAheadApiProvider
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead.Event
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead.ValidationState
import io.ghostbuster91.android.react.component.example.typeahead.TypeAheadReducer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.type_ahead_view.view.*

class TypeAheadView : LinearLayout, ReactiveView<ValidationState> {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        LayoutInflater.from(context).inflate(R.layout.type_ahead_view, this, true)
    }

    override fun bind(events: Relay<Any>, states: Observable<ValidationState>, function: (ValidationState) -> State, globalState: BehaviorRelay<State>) {
        bindReducer(events, states, function, globalState)
        bindEvents(events)
        bindStates(states)
    }

    private fun bindReducer(events: Relay<Any>, states: Observable<ValidationState>, function: (ValidationState) -> State, globalState: BehaviorRelay<State>) {
        TypeAheadReducer(typAheadApiProvider(), ioScheduler = Schedulers.io(), debounceScheduler = Schedulers.computation(), uiScheduler = AndroidSchedulers.mainThread())
                .invoke(events.ofType(Event.TextChanged::class.java), states)
                .bindToLifecycle(this)
                .map { function(it) }
                .subscribe(globalState)
    }

    private fun bindEvents(events: Relay<Any>) {
        typeAheadInput.textChanges()
                .bindToLifecycle(this)
                .map { Event.TextChanged(it.toString()) }
                .subscribe(events)
    }

    private fun bindStates(states: Observable<ValidationState>) {
        states
                .bindToLifecycle(this)
                .distinctUntilChanged()
                .subscribe {
                    when (it) {
                        ValidationState.IDLE -> {
                            typeAheadStatusIndicator.visibility = View.INVISIBLE
                            typeAheadLoadingIndicator.visibility = View.GONE
                        }
                        ValidationState.ERROR -> {
                            typeAheadStatusIndicator.visibility = View.VISIBLE
                            typeAheadStatusIndicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_cloud_off_black_24dp))
                            typeAheadLoadingIndicator.visibility = View.GONE
                        }
                        ValidationState.LOADING -> {
                            typeAheadStatusIndicator.visibility = View.GONE
                            typeAheadLoadingIndicator.visibility = View.VISIBLE
                        }
                        ValidationState.OCCUPIED -> {
                            typeAheadStatusIndicator.visibility = View.VISIBLE
                            typeAheadStatusIndicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_block_black_24dp))
                            typeAheadLoadingIndicator.visibility = View.GONE
                        }
                        ValidationState.FREE -> {
                            typeAheadStatusIndicator.visibility = View.VISIBLE
                            typeAheadStatusIndicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_done_black_24dp))
                            typeAheadLoadingIndicator.visibility = View.GONE
                        }
                    }
                }
    }
}
