package com.github.kr328.ibr.utils

import kotlin.concurrent.thread

class SingleThreadPool {
    @Synchronized
    fun execute(runnable: () -> Unit): Boolean {
        return if (running)
            false
        else {
            running = true
            thread {
                runnable.invoke()
                synchronized(this) {
                    running = false
                }
            }
            true
        }
    }

    private var running: Boolean = false
}