package com.appsplus.online.slot.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.yandex.metrica.push.firebase.MetricaMessagingService


class FirebaseMessagingMasterService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        createNotifiCanall()
        MetricaMessagingService().processPush(this, remoteMessage)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        MetricaMessagingService().apply {
            processToken(this, token)
            onNewToken(token)
        }
    }

    fun createNotifiCanall() {
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel =
                NotificationChannel("manager", "Manager", NotificationManager.IMPORTANCE_HIGH)
            mChannel.apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            }
            mNotificationManager.createNotificationChannel(mChannel)
        }
    }
}