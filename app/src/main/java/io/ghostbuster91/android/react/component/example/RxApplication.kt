package io.ghostbuster91.android.react.component.example

import android.app.Application

class RxApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        exampleReducer.invoke(events).subscribe(states)
    }
}