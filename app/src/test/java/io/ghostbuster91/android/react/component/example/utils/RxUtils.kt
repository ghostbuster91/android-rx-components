package io.ghostbuster91.android.react.component.example.utils

import io.reactivex.observers.TestObserver
import org.junit.Assert

fun <T> TestObserver<T>.assertLastValue(value: T) {
    Assert.assertEquals(value, values().last())
}