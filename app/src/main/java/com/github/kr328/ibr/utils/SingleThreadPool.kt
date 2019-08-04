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

    @Synchronized
    fun isRunning(): Boolean = running

    private var running: Boolean = false
}