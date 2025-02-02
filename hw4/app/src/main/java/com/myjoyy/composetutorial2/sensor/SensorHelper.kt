package com.myjoyy.composetutorial2.sensor

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.myjoyy.composetutorial2.notifications.NotificationHelper
import kotlin.math.abs


//https://developer.android.com/develop/sensors-and-location/sensors/sensors_overview#sensor-availability
class SensorHelper(activity: Activity, val notificationHelper: NotificationHelper) : SensorEventListener {
    private var sensorManager: SensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private lateinit var sensor: Sensor

    init{

        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!
        }
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
            notificationHelper.firstNotification("Sensor notification", "Phone is spinning")
        }
    }

    fun startSensorListener(){
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }


}