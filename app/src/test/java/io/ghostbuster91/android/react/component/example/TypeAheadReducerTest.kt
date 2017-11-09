package io.ghostbuster91.android.react.component.example

import com.elpassion.mspek.MiniSpek.mspek
import com.elpassion.mspek.MiniSpek.o
import com.elpassion.mspek.MiniSpekRunner
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockito_kotlin.*
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead.*
import io.ghostbuster91.android.react.component.example.typeahead.TypeAheadReducer
import io.ghostbuster91.android.react.component.example.utils.assertLastValue
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.SingleSubject
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(MiniSpekRunner::class)
class TypeAheadReducerTest {

    @Test
    fun should() = mspek("start") {
        var apiSubject = SingleSubject.create<Boolean>()
        val api = mock<Api> { on { call(any()) } doAnswer { apiSubject = SingleSubject.create();apiSubject } }
        val events = PublishRelay.create<Event.TextChanged>()
        val stateRelay = BehaviorSubject.createDefault(TypeAhead.initialState)
        val scheduler = TestScheduler()

        TypeAheadReducer(api, ioScheduler = Schedulers.trampoline(), uiScheduler = Schedulers.trampoline(), debounceScheduler = scheduler).run {
            invoke(events, stateRelay).subscribe(stateRelay)
        }
        val state = stateRelay.test()

        "show nothing on start" o {
            state.assertLastValue(ValidationState.IDLE)
        }
        "after text changed to empty" o {
            events.accept(Event.TextChanged(""))
            "still show idle state" o {
                state.assertLastValue(ValidationState.IDLE)
            }
        }
        "after typing text" o {
            events.accept(Event.TextChanged("a"))
            "loader should be shown" o {
                state.assertLastValue(ValidationState.LOADING)
            }
            "and time passes" o {
                scheduler.advanceTimeBy(TypeAheadReducer.DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                "api is called with given text" o {
                    verify(api).call("a")
                }
                "after api return validation OK" o {
                    apiSubject.onSuccess(true)
                    "validation should be ok" o {
                        state.assertLastValue(ValidationState.FREE)
                    }
                    "after erasing text" o {
                        events.accept(Event.TextChanged(""))
                        "state should be idle" o {
                            state.assertLastValue(ValidationState.IDLE)
                        }
                    }
                }
                "after api return validation fail" o {
                    apiSubject.onSuccess(false)
                    "validation should be not ok" o {
                        state.assertLastValue(ValidationState.OCCUPIED)
                    }
                }
                "after api return error" o {
                    apiSubject.onError(RuntimeException())
                    "state should be idle" o {
                        state.assertLastValue(ValidationState.ERROR)
                    }
                    "and user type more" o {
                        events.accept(Event.TextChanged("ab"))
                        "state is loading" o {
                            state.assertLastValue(ValidationState.LOADING)
                        }
                    }
                }
            }
            "and typing more text before time passes" o {
                events.accept(Event.TextChanged("ab"))
                "api is not called" o {
                    verify(api, never()).call(any())
                }
                "loader is visible" o {
                    state.assertLastValue(ValidationState.LOADING)
                }
            }
        }
    }
}