package com.ramadhan.mysayur.view.maps

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.os.Build
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startForegroundService
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.ramadhan.mysayur.R
import com.ramadhan.mysayur.common.service.LocationService
import com.ramadhan.mysayur.databinding.FragmentMapsBinding
import java.util.concurrent.TimeUnit
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private var isTracking = false

    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null

    private var allLatLng = ArrayList<LatLng>()
    private var movingMarker: Marker? = null

    private var isFromList: Boolean = false

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val mapsViewModel: MapsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        if (isFromList) {
            mapsViewModel.getAllLocations()
            mapsViewModel.locations.observe(viewLifecycleOwner) {
                for (location in it) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(location.latitude.toString())
                    )

                    //focusing to all location
                    val bounds = boundsBuilder
                    bounds.include(latLng)
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds.build(),
                            resources.displayMetrics.widthPixels,
                            resources.displayMetrics.heightPixels,
                            300
                        )
                    )
                }
            }
        } else {
            getMyLastLocation()
            createLocationRequest()
            createLocationCallback()

            binding.btnStart.setOnClickListener {
                if (!isTracking) {
                    clearMaps()
                    updateTrackingStatus(true)
                    startLocationUpdate()
                    startLocationService()
                } else {
                    updateTrackingStatus(false)
                    stopLocationUpdate()
                    stopLocationService()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


        val bundle = arguments?.getBoolean("isFromList")

        if (bundle != null) {
            isFromList = bundle
        }

        if (isFromList) {
            binding.btnStart.visibility = View.GONE
        } else {
            binding.btnStart.visibility = View.VISIBLE
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun startLocationService() {
        val intent = Intent(requireContext(), LocationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(requireContext(), intent)
        } else {
            requireContext().startService(intent)
        }
    }

    private fun stopLocationService() {
        val intent = Intent(requireContext(), LocationService::class.java)
        requireContext().stopService(intent)
    }

    private fun stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback!!)
    }

    private fun startLocationUpdate() {
        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest!!,
                locationCallback!!,
                Looper.getMainLooper()
            )

        } catch (e: SecurityException) {
            Log.e("MapsActivity", "Security Exception: ${e.message}")
        }
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.d(
                        "MapsActivity",
                        "onLocationRes ${location.latitude}, ${location.longitude}"
                    )

                    // Draw polyline
                    val newLatLng = LatLng(location.latitude, location.longitude)
                    allLatLng.add(newLatLng)


                    // Set boundaries
                    boundsBuilder.include(newLatLng)
                    val bounds = boundsBuilder.build()
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            64
                        )
                    )

                    // Update or create moving marker
                    if (movingMarker == null) {
                        // Create a new marker if it doesn't exist
                        movingMarker = mMap.addMarker(
                            MarkerOptions()
                                .position(newLatLng)
                                .title("Current Location")
                                .icon(
                                    vectorTBitmap(
                                        R.drawable.ic_android,
                                        Color.RED
                                    )
                                )
                        )
                    } else {
                        // Move the existing marker to the new location
                        movingMarker?.position = newLatLng
                    }
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        if (!isFromList) {
            stopLocationUpdate()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isFromList && isTracking) {
            startLocationUpdate()
        }
    }

    private fun updateTrackingStatus(newStatus: Boolean) {
        isTracking = newStatus
        if (isTracking) {
            binding.btnStart.text = getString(R.string.stop_tracking)
        } else {
            binding.btnStart.text = getString(R.string.start_tracking)
        }
    }


    private val resolutionLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                Log.d("MapsActivity", "User has agreed to change location settings")
            }

            RESULT_CANCELED -> {
                Log.d("MapsActivity", "User has not agreed to change location settings")
            }
        }
    }

    private fun createLocationRequest() {
        val priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val interval = TimeUnit.MINUTES.toMillis(5)
        val maxWaitTime = TimeUnit.MINUTES.toMillis(5)

        locationRequest = LocationRequest.Builder(
            priority,
            interval
        ).apply {
            setMaxUpdateDelayMillis(maxWaitTime)
        }.build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)

        val client = LocationServices.getSettingsClient(requireContext())
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                getMyLastLocation()
            }

            .addOnFailureListener { e ->
                if (e is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(e.resolution).build()
                        )
                    } catch (sendEx: Exception) {
                        Toast.makeText(requireContext(), "${sendEx.message}", Toast.LENGTH_SHORT)
                            .show()
                        Log.e("MapsActivity", "Error get location setting: ${sendEx.message}")
                    }

                }
            }
    }

    private fun getMyLastLocation() {
        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    showStartMarker(location)
                } else {
                    Toast.makeText(requireContext(),
                        getString(R.string.location_not_found), Toast.LENGTH_SHORT)
                        .show()
                }
            }

        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showStartMarker(location: Location) {
        val startLocation = LatLng(location.latitude, location.longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(startLocation)
                .title(getString(R.string.my_first_location_label))
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 17f))
    }

    private var boundsBuilder = LatLngBounds.Builder()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getMyLastLocation()
            }

            permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getMyLastLocation()
            }

            else -> {
                Toast.makeText(requireContext(),
                    getString(R.string.permission_denied_label), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun vectorTBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
            ?: return BitmapDescriptorFactory.defaultMarker()

        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun clearMaps() {
        mMap.clear()
        allLatLng.clear()
        boundsBuilder = LatLngBounds.Builder()
        movingMarker = null
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}