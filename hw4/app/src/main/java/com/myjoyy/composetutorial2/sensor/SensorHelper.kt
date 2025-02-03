package com.myjoyy.composetutorial2.sensor

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.myjoyy.composetutorial2.MainActivity
import kotlin.math.abs


//https://developer.android.com/develop/sensors-and-location/sensors/sensors_overview#sensor-availability
//https://developer.android.com/develop/background-work/services/fgs/launch
class SensorHelper : Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    override fun onCreate(){
        super.onCreate()

        sensorNotificationChannel()

        val notification: Notification = NotificationCompat.Builder(this, "sensor_channel_id")
            .setSmallIcon(android.R.drawable.btn_star_big_on)
            .setContentTitle("Sensor service starting")
            .setContentText("Gyro being listened")
            .build()

        startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        //val lux = event.values[0]
        // Do something with this sensor value.

        val yAxisValue = abs(event.values[1])

        if (yAxisValue > 5.0) {
            //notifications
            sensorNotification("Sensor notification", "Phone is spinning")
        }
    }
/*
    fun startSensorListener(){
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }
*/

    fun sensorNotificationChannel() {
        val channel = NotificationChannel(
            "sensor_channel_id",
            "notification",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "channel for sensor notifications"
        }
        val manager = this.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }



    private fun sensorNotification(title: String, content: String){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, MainActivity::class.java).apply {
            // no new main if top and if not top bring top
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val notificationIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, "sensor_channel_id")
            .setSmallIcon(android.R.drawable.btn_star_big_on)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(notificationIntent)
            .build()
        notificationManager.notify(1, notification)
    }

}