package com.nf98.moviecatalogue.helper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

internal class InternetCheck(private val mConsumer: Consumer) {

    init {
        GlobalScope.launch(Dispatchers.IO){
            val isConnect =
                try {
                    val sock = Socket()
                    sock.connect(InetSocketAddress("8.8.8.8", 53), 1500)
                    sock.close()
                    true
                } catch (e: IOException) {
                    false
                }

            withContext(Dispatchers.Main){
                mConsumer.accept(isConnect)
            }
        }
    }

    interface Consumer {
        fun accept(internet: Boolean)
    }
}