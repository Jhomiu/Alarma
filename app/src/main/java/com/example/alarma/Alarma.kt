package com.example.alarma

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class Alarma: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var recuperar_datos: String = intent!!.getStringExtra("extra")

        var service: Intent = Intent(context,TonoAlarma::class.java)
        service.putExtra("extra",recuperar_datos)
        context!!.startService(service)

    }
}