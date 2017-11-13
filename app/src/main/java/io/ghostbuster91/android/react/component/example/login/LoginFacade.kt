package io.ghostbuster91.android.react.component.example.login

import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead

interface Login {
    data class State(val firstTypeAhead: TypeAhead.ValidationState,
                     val secondTypeAhead: TypeAhead.ValidationState,
                     val isLoginButtonEnabled: Boolean)
}