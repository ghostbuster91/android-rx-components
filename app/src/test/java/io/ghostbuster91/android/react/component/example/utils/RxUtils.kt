package io.ghostbuster91.android.react.component.example.utils

import io.reactivex.observers.TestObserver
import junit.framework.Assert.assertTrue
import org.junit.Assert

fun <T> TestObserver<T>.assertLastValue(value: T) {
    Assert.assertEquals(value, values().last())
}

fun <T> TestObserver<T>.assertLastValueThat(predicate: T.() -> Boolean) {
    assertTrue("Last value does not match predicate. ${values().last()}", values().last().predicate())
}