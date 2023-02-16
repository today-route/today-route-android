package com.maru.todayroute.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.maru.todayroute.R
import com.maru.todayroute.ui.MainActivity
import com.maru.todayroute.ui.home.TrackingInfo

class RouteRecordingService: Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        setLocationChangedListener()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action != null && intent.action!!.equals(TrackingInfo.ACTION_RECORDING_STOP, true)) {
            stopForeground(true)
            stopSelf()
        } else {
            val notification = generateForegroundNotification()
            startForeground(NOTIFICATION_ID, notification)
        }
        return START_NOT_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun setLocationChangedListener() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    TrackingInfo.addGeoCoord(latitude, longitude)
                }
            }
        }
        val locationRequest = LocationRequest.create().apply {
            interval = 1000
            priority = Priority.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 1000
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun generateForegroundNotification(): Notification {
        val notificationManager: NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val intentMainLanding = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intentMainLanding, PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_content_title))
            .setContentText(getString(R.string.notification_content_text))
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        const val NOTIFICATION_ID = 198
        const val NOTIFICATION_CHANNEL_ID = "recording route"
    }
}