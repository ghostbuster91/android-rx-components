package io.ghostbuster91.android.react.component.example.typeahead

import io.reactivex.Single

interface TypeAhead {
    interface Api {
        fun call(s: String): Single<Boolean>
    }

    sealed class Event {
        data class TextChanged(val text: String) : Event()
    }

    enum class ValidationState {
        IDLE,
        LOADING,
        OCCUPIED,
        FREE,
        ERROR
    }

    companion object {
        val initialState = ValidationState.IDLE
    }
}