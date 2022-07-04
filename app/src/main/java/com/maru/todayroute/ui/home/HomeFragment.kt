package com.maru.todayroute.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.maru.todayroute.databinding.FragmentHomeBinding
import com.maru.todayroute.util.MapViewLifecycleObserver
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PathOverlay
import kotlin.math.roundToInt

class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var naverMap: NaverMap
    private lateinit var currentLocation: Pair<Double, Double>

    private var isRecording = false
    private var recordStartTime = -1L

    private lateinit var locationCallback: LocationCallback
    private lateinit var geoCoordList: MutableList<LatLng>
    private lateinit var path: PathOverlay

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(MapViewLifecycleObserver(binding.mvMap, savedInstanceState))
        if (hasNotLocationPermission()) {
            requestLocationPermission()
        } else {
            binding.mvMap.getMapAsync(this)
        }
        setButtonClickListener()
    }

    private fun initLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.d("location", "${location.latitude} ${location.longitude}")

                    if (!isSameLocation(location.latitude, location.longitude)) {
                        if (isRecording) {
                            geoCoordList.add(LatLng(location.latitude, location.longitude))
                            if (2 <= geoCoordList.size) {
                                path.coords = geoCoordList
                                if (path.map == null) {
                                    path.map = naverMap
                                }
                            }
                        }
                        currentLocation = Pair(location.latitude, location.longitude)
                        moveMapCameraToCurrentLocation(currentLocation)
                        showOverlayOnCurrentLocation(currentLocation)
                    }
                }
            }
        }
    }

    private fun isSameLocation(newLatitude: Double, newLongitude: Double): Boolean {
        val preLatitude = (currentLocation.first * 10000).roundToInt()
        val preLongitude = (currentLocation.second * 10000).roundToInt()
        val targetLatitude = (newLatitude * 10000).roundToInt()
        val targetLongitude = (newLongitude * 10000).roundToInt()

        return preLatitude == targetLatitude && preLongitude == targetLongitude
    }

    private fun initPathOverlay() {
        path = PathOverlay()
        with(path) {
            color = Color.rgb(114, 149, 185)
            outlineColor = Color.rgb(114, 149, 185)
            width = 30
        }
    }

    private fun initGeoCoordList() {
        geoCoordList = mutableListOf(LatLng(currentLocation.first, currentLocation.second))
    }

    @SuppressLint("MissingPermission")
    private fun setLocationChangedListener() {
        initLocationCallback()
        val locationRequest = LocationRequest.create().apply {
            interval = 5
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 10
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun setButtonClickListener() {
        with(binding.btnStartRecordRoute) {
            setOnClickListener {
                if (isRecording) {
                    isRecording = false
                    this.text = "루트 기록 시작"
                    if (isValidRecord()) {
                        stopLocationUpdates()
                        moveToAddNewRouteFragment()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "유효하지 않은 기록입니다. 다시 기록해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    isRecording = true
                    this.text = "루트 기록 종료"
                    getLastLocation()
                    initPathOverlay()
                    initGeoCoordList()
                    recordStartTime = System.currentTimeMillis()
                }
            }
        }

        binding.btnCurrentLocation.setOnClickListener {
            moveMapCameraToCurrentLocation(currentLocation)
        }
    }

    private fun isValidRecord(): Boolean = geoCoordList.size >= 2

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mvMap.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mvMap.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        getLastLocation()
        setLocationChangedListener()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                currentLocation = Pair(location.latitude, location.longitude)

                showOverlayOnCurrentLocation(currentLocation)
                setMapCameraZoom(16.0)
                moveMapCameraToCurrentLocation(currentLocation)
            }
        }
    }

    private fun showOverlayOnCurrentLocation(currentLocation: Pair<Double, Double>) {
        naverMap.locationOverlay.apply {
            isVisible = true
            position = LatLng(currentLocation.first, currentLocation.second)
        }
    }

    private fun moveMapCameraToCurrentLocation(currentLocation: Pair<Double, Double>) {
        val cameraUpdate = CameraUpdate.scrollTo(
            LatLng(
                currentLocation.first,
                currentLocation.second
            )
        ).animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun setMapCameraZoom(level: Double) {
        naverMap.moveCamera(CameraUpdate.zoomTo(level))
    }

    private fun hasNotLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
                        || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
                -> {
                    binding.mvMap.getMapAsync(this)
                }
                else -> {

                }
            }
        }

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun moveToAddNewRouteFragment() {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddNewRoute(geoCoordList.toTypedArray()))
    }
}