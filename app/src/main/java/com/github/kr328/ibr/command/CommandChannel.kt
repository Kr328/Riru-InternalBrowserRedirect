package com.github.kr328.ibr.command

import android.os.Handler
import android.os.Message

class CommandChannel {
    private class H(val handle: (Message) -> Unit) : Handler() {
        override fun handleMessage(msg: Message) {
            handle(msg)
        }
    }

    val receivers: MutableMap<Int, (Any?) -> Unit> = mutableMapOf()
    private val handler = H {
        receivers[it.what]?.apply {
            this(it.obj)
        }
    }

    inline fun <reified T> registerReceiver(command: String, crossinline receiver: (String, T?) -> Unit) {
        receivers[command.hashCode()] = {
            if (it is T?) {
                receiver(command, it)
            }
        }
    }

    fun unregisterReceiver(command: String) {
        receivers.remove(command.hashCode())
    }

    fun <T> sendCommand(command: String, arg: T? = null, delay: Long = 0) {
        handler.sendMessageDelayed(handler.obtainMessage(command.hashCode(), arg), delay)
    }

    fun cancelCommand(command: String) {
        handler.removeMessages(command.hashCode())
    }
}