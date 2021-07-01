package ru.sorokin.flyfilmsx.viewmodel

import android.os.Looper

class MyThread : Thread() {

    private val running = true

    override fun run() {
        Looper.prepare()
        Looper.loop()
    }
}