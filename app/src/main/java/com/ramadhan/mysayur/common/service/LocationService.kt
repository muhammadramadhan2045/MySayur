    package com.ramadhan.mysayur.common.service

    import android.Manifest
    import android.annotation.SuppressLint
    import android.app.Notification
    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.app.Service
    import android.content.Intent
    import android.content.pm.PackageManager.PERMISSION_GRANTED
    import android.location.Location
    import android.os.Build
    import android.os.IBinder
    import android.os.Looper
    import androidx.core.app.NotificationCompat
    import com.google.android.gms.location.FusedLocationProviderClient
    import com.google.android.gms.location.LocationCallback
    import com.google.android.gms.location.LocationRequest
    import com.google.android.gms.location.LocationResult
    import com.google.android.gms.location.LocationServices
    import com.ramadhan.mysayur.core.domain.model.LocationTracker
    import com.ramadhan.mysayur.core.domain.usecase.LocationUseCase
    import org.koin.android.ext.android.inject
    import java.util.concurrent.TimeUnit

    class LocationService : Service() {
        private lateinit var fusedLocationClient: FusedLocationProviderClient
        private lateinit var locationRequest: LocationRequest
        private lateinit var locationCallback: LocationCallback

        private val locationUseCase: LocationUseCase by inject()

        override fun onCreate() {
            super.onCreate()

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


            createLocationRequest()
            startLocationUpdates()
        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            startForeground(1, createNotification("Location Service Started"))
            return START_STICKY
        }

        private fun createNotification(
            word: String
        ): Notification {
            val notificationId = "Location Service"
            val channelName = "Background Location Service"


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    notificationId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    enableLights(false)
                    enableVibration(false)
                }

                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            return NotificationCompat.Builder(this, notificationId)
                .setContentTitle("Location Service")
                .setContentText(word)
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .build()
        }

        private fun startLocationUpdates() {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)

                    for (location in locationResult.locations) {
                        val latitude = location.latitude
                        val longitude = location.longitude

                        saveLocationToUseCase(location)
                        updateNotification("Latitude: $latitude, Longitude: $longitude")
                    }
                }
            }

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }

        private fun saveLocationToUseCase(location: Location?) {
            location?.let {
                locationUseCase.saveLocation(
                    LocationTracker(
                        latitude = it.latitude,
                        longitude = it.longitude,
                        timestamp = it.time
                    )
                )
            }
        }

        private fun updateNotification(locationText: String) {
            val notification = createNotification(locationText)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notification)
        }

        private fun createLocationRequest() {
            locationRequest = LocationRequest.Builder(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                TimeUnit.MINUTES.toMillis(5),
            ).apply {
                setMaxUpdateDelayMillis(TimeUnit.MINUTES.toMillis(5))
            }.build()
        }

        override fun onBind(
            intent: Intent
        ): IBinder? {
            return null
        }

        override fun onDestroy() {
            super.onDestroy()
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }