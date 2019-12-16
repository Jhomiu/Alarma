package com.example.alarma

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var alarma: AlarmManager
    lateinit var picker: TimePicker
    lateinit var text: TextView
    lateinit var context: Context
    lateinit var btempezar: Button
    lateinit var btparar: Button
    var hour: Int = 0
    var min: Int = 0
    lateinit var intent: PendingIntent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.context = this
        alarma = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        picker = findViewById(R.id.tp) as TimePicker
        text = findViewById(R.id.update_text) as TextView
        btempezar = findViewById(R.id.btempezar) as Button
        btparar = findViewById(R.id.btparar) as Button
        var c: Calendar = Calendar.getInstance();
        var zIntent: Intent = Intent(this,Alarma::class.java)
        //Boton Empezar
        btempezar.setOnClickListener(object : View.OnClickListener{
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onClick(v: View?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    c.set(Calendar.HOUR_OF_DAY,picker.hour)
                    c.set(Calendar.MINUTE,picker.minute)
                    c.set(Calendar.SECOND,0)
                    c.set(Calendar.MILLISECOND,0)
                    hour =picker.hour
                    min = picker.minute
                }else{
                    c.set(Calendar.HOUR_OF_DAY,picker.currentHour)
                    c.set(Calendar.MINUTE,picker.currentMinute)
                    c.set(Calendar.SECOND,0)
                    c.set(Calendar.MILLISECOND,0)
                    hour =picker.currentHour
                    min = picker.currentMinute

                }
                var hr_str:String = hour.toString()
                var min_str:String = min.toString()
                if(hour > 12){
                    hr_str = (hour - 12).toString()
                }
                if(min < 10){
                    min_str = "0$min"
                }
                set_alarm_text("Alarm set to: $hr_str : $min_str")
                zIntent.putExtra("extra","on")
                intent = PendingIntent.getBroadcast(this@MainActivity,0,zIntent,PendingIntent.FLAG_CANCEL_CURRENT)
                alarma.setExact(AlarmManager.RTC_WAKEUP,c.timeInMillis,intent)

            }
        })
        //Boton Parar
        btparar.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                set_alarm_text("Alarm off")
                intent = PendingIntent.getBroadcast(this@MainActivity,0,zIntent,PendingIntent.FLAG_UPDATE_CURRENT)
                alarma.cancel(intent)
                zIntent.putExtra("extra","off")
                sendBroadcast(zIntent)
            }
        })

    }

    private fun set_alarm_text (s: String){
        update_text.setText(s)
    }
}
