package io.ghostbuster91.android.react.component.example

import com.elpassion.mspek.MiniSpek
import com.elpassion.mspek.MiniSpek.o
import com.elpassion.mspek.MiniSpekRunner
import com.jakewharton.rxrelay2.PublishRelay
import io.ghostbuster91.android.react.component.example.common.Reducer
import io.ghostbuster91.android.react.component.example.login.LoginReducer
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead
import io.ghostbuster91.android.react.component.example.utils.assertLastValueThat
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(MiniSpekRunner::class)
class LoginReducerTest {

    @Test
    fun should() = MiniSpek.mspek("start") {
        val events = PublishRelay.create<Any>()

        "login button should be disabled by default" o {
            val firstTypeAheadReducer: Reducer<TypeAhead.ValidationState> = object : Reducer<TypeAhead.ValidationState> {
                override fun invoke(events: Observable<out Any>): Observable<TypeAhead.ValidationState> {
                    return Observable.just(TypeAhead.ValidationState.IDLE)
                }
            }
            val secondTypeAheadReducer = object : Reducer<TypeAhead.ValidationState> {
                override fun invoke(events: Observable<out Any>): Observable<TypeAhead.ValidationState> {
                    return Observable.just(TypeAhead.ValidationState.IDLE)
                }
            }

            val states = LoginReducer(firstTypeAheadReducer, secondTypeAheadReducer).invoke(events).test()

            states.assertLastValueThat { !isLoginButtonEnabled }
        }

        "should be enabled when both are FREE" o {
            val firstTypeAheadReducer: Reducer<TypeAhead.ValidationState> = object : Reducer<TypeAhead.ValidationState> {
                override fun invoke(events: Observable<out Any>): Observable<TypeAhead.ValidationState> {
                    return Observable.just(TypeAhead.ValidationState.FREE)
                }
            }
            val secondTypeAheadReducer = object : Reducer<TypeAhead.ValidationState> {
                override fun invoke(events: Observable<out Any>): Observable<TypeAhead.ValidationState> {
                    return Observable.just(TypeAhead.ValidationState.FREE)
                }
            }

            val states = LoginReducer(firstTypeAheadReducer, secondTypeAheadReducer).invoke(events).test()
            states.assertLastValueThat { isLoginButtonEnabled }
        }
    }
}