package com.example.alarma

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat

class TonoAlarma : Service() {
    companion object{
        lateinit var tono: Ringtone
    }
    var id: Int = 0
    var run: Boolean = false
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var estado: String = intent!!.getStringExtra("extra")
        assert(estado!=null)
        when (estado){
            "on" ->id = 1
            "off" -> id = 0
        }
        cambios()
        return START_NOT_STICKY
    }

    private fun sonarAlarma(){
        var alarmaUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alarmaUri ==null){
            alarmaUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        tono = RingtoneManager.getRingtone(baseContext,alarmaUri)
        tono.play()
    }

    private fun cambios(){
        if (!this.run && id ==1){
            sonarAlarma()
            this.run = true
            this.id = 0
            sacarNotificacion()

        }else if (this.run && id ==0){
            tono.stop()
            this.run = false
            this.id=0
        }else if (!this.run && id == 0){
            this.run = false
            this.id = 0

        }else if (this.run && id ==1){
            this.run = false
            this.id = 0
        }else{

        }
    }


    private fun sacarNotificacion(){
        var maIntent: Intent = Intent(this,MainActivity::class.java)
        var pIntent : PendingIntent = PendingIntent.getActivity(this,0,maIntent,0)
        val sonidoUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var notificacionManager :NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var notificacion: Notification = NotificationCompat.Builder(this)
            .setContentTitle("Alarma")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setSound(sonidoUri)
            .setContentText("Pulse aqui")
            .setContentIntent(pIntent)
            .setAutoCancel(true)
            .build()
        notificacionManager.notify(0,notificacion)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.run=false
    }
}