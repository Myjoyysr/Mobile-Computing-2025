package com.myjoyy.composetutorial2.notifications

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.myjoyy.composetutorial2.MainActivity


//https://github.com/android/socialite/blob/main/app/src/main/java/com/google/android/samples/socialite/repository/NotificationHelper.kt

class NotificationHelper (private val activity: Activity) {
    //
    // 1. Request permissions
    //
    fun requestPermission(launcher: ActivityResultLauncher<String>){
        if (ActivityCompat.checkSelfPermission(
                activity.applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }else {
            firstNotification("Hello World!", "Notifications are already enabled!")
        }
    }

    //
    // 2. Create channel
    //

    fun notificationChannel() {
        val channel = NotificationChannel(
            "channel_id",
            "notification",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "channel for all notifications"
        }
        val manager = activity.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    //
    // 3. Create notification
    //

    fun firstNotification(title: String, content: String){
        val notificationManager = activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(activity, MainActivity::class.java).apply {
            // no new main if top and if not top bring top
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val notificationIntent = PendingIntent.getActivity(activity,0,intent,PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(activity, "channel_id")
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