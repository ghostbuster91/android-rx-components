package io.ghostbuster91.android.react.component.example

import com.elpassion.mspek.MiniSpek
import com.elpassion.mspek.MiniSpek.o
import com.elpassion.mspek.MiniSpekRunner
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead.Event
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead.ValidationState
import io.ghostbuster91.android.react.component.example.typeahead.TypeAheadReducer
import io.ghostbuster91.android.react.component.example.utils.assertLastValue
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.SingleSubject
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(MiniSpekRunner::class)
class TypeAheadReducer_SchedulersTest {

    @Test
    fun should() = MiniSpek.mspek("start") {
        val apiSubject = SingleSubject.create<Boolean>()
        val api = mock<TypeAhead.Api> { on { call(any()) } doReturn (apiSubject) }
        val events = PublishRelay.create<TypeAhead.Event.TextChanged>()
        val stateRelay = BehaviorSubject.createDefault(TypeAhead.initialState)
        val ioScheduler = TestScheduler()
        val uiScheduler = TestScheduler()
        val debounceScheduler = TestScheduler()

        TypeAheadReducer(api, ioScheduler = ioScheduler, uiScheduler = uiScheduler, debounceScheduler = debounceScheduler).run {
            invoke(events, stateRelay).subscribe(stateRelay)
        }
        val state = stateRelay.test()

        "after text changed" o {
            events.accept(Event.TextChanged("a"))
            "and time passses" o {
                debounceScheduler.advanceTimeBy(TypeAheadReducer.DEBOUNCE_TIME, TimeUnit.MILLISECONDS)

                "api should have no observers" o {
                    Assert.assertFalse(apiSubject.hasObservers())
                }

                "and io scheduler triggers action" o {
                    ioScheduler.triggerActions()

                    "should have observers" o {
                        Assert.assertTrue(apiSubject.hasObservers())
                    }

                    "and api returns free" o {
                        apiSubject.onSuccess(true)

                        "state should be loading" o {
                            state.assertLastValue(ValidationState.LOADING)
                        }
                        "and ui scheduler triggers action" o {
                            uiScheduler.triggerActions()

                            "state should be validation free" o {
                                state.assertLastValue(ValidationState.FREE)
                            }
                        }
                    }
                }
            }
        }
    }
}