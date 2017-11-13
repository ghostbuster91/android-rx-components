package io.ghostbuster91.android.react.component.example

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.elpassion.android.commons.espresso.isDisplayed
import com.elpassion.android.commons.espresso.onId
import com.elpassion.android.commons.espresso.typeText
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.mock
import io.ghostbuster91.android.react.component.example.login.ui.LoginActivity
import io.ghostbuster91.android.react.component.example.typeahead.TypeAhead
import io.reactivex.subjects.SingleSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TypeAheadViewTest {

    private var subject = SingleSubject.create<Boolean>()
    private val api = mock<TypeAhead.Api> { on { call(any()) } doAnswer { subject = SingleSubject.create();subject } }

    @Rule
    @JvmField
    val rule = ActivityTestRule<LoginActivity>(LoginActivity::class.java)

    @Before
    fun setUp() {
        typAheadApiProvider = { api }
    }

    @Test
    fun shouldShowLoading() {
        onId(R.id.typeAheadInput).typeText("a")
        onId(R.id.typeAheadLoadingIndicator).isDisplayed()
    }
}
