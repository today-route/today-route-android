package com.maru.todayroute.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentHomeBinding
import com.maru.todayroute.service.RouteRecordingService
import com.maru.todayroute.ui.MainViewModel
import com.maru.todayroute.util.BaseFragment
import com.maru.todayroute.util.MapViewLifecycleObserver
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PathOverlay
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), OnMapReadyCallback {

    private val viewModel: HomeViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var naverMap: NaverMap

    private lateinit var locationCallback: LocationCallback
    private lateinit var path: PathOverlay

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.activityViewModel = activityViewModel
        binding.viewModel = viewModel
        viewLifecycleOwner.lifecycle.addObserver(
            MapViewLifecycleObserver(
                binding.mvMap,
                savedInstanceState
            )
        )
        if (hasNotLocationPermission()) {
            requestLocationPermission()
        } else {
            binding.mvMap.getMapAsync(this)
        }
        setButtonClickListener()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.showOverlayOnCurrentLocation.observe(viewLifecycleOwner) {
            showOverlayOnCurrentLocation(it)
        }

        viewModel.updateUserLocation.observe(viewLifecycleOwner) { update ->
            if (!update) {
                stopLocationUpdates()
            }
        }

        viewModel.moveToAddRouteFragment.observe(viewLifecycleOwner) { pair ->
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddRouteFragment(pair.first, pair.second))
        }

        viewModel.updatePath.observe(viewLifecycleOwner) { geoCoord ->
            updatePath(geoCoord)
        }

        viewModel.moveMapCameraToCurrentLocation.observe(viewLifecycleOwner) { currentLocation ->
            moveMapCameraToCurrentLocation(currentLocation)
        }

        viewModel.isRecording.observe(viewLifecycleOwner) { isRecording ->
            if (isRecording) {
                startForegroundService()
                setButtonText(isRecording)
            } else {
                stopForegroundService()
                setButtonText(isRecording)
            }
        }
    }

    private fun initLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    viewModel.userMoves(latitude, longitude)
                }
            }
        }
    }

    private fun initPathOverlay() {
        path = PathOverlay()
        with(path) {
            color = requireContext().getColor(R.color.purple)
            outlineColor = requireContext().getColor(R.color.purple)
            width = 30
        }
        viewModel.showPath()
    }

    @SuppressLint("MissingPermission")
    private fun setLocationChangedListener() {
        initLocationCallback()
        val locationRequest = LocationRequest.create().apply {
            interval = 1000
            priority = Priority.PRIORITY_LOW_POWER
            maxWaitTime = 1000
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun setButtonClickListener() {
        binding.btnStartRecordRoute.setOnClickListener {
            viewModel.startRecording()
            getLastLocation()
        }

        binding.btnCurrentLocation.setOnClickListener {
            viewModel.moveMapCameraToCurrentLocation()
        }
    }

    private fun setButtonText(isRecording: Boolean) {
        if (isRecording) {
            binding.btnStartRecordRoute.text = "루트 기록 종료"
        } else {
            binding.btnStartRecordRoute.text = "루트 기록 시작"
        }
    }

    private fun startForegroundService() {
        Intent(context, RouteRecordingService::class.java).run {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) requireContext().startForegroundService(this)
            else requireContext().startService(this)
        }
    }

    private fun stopForegroundService() {
        val intent = Intent(context, RouteRecordingService::class.java)
        intent.action = TrackingInfo.ACTION_RECORDING_STOP
        requireContext().startService(intent)
    }

    private fun updatePath(geoCoordList: List<LatLng>) {
            path.coords = geoCoordList
            path.map = naverMap
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        initPathOverlay()
        getLastLocation()
        setLocationChangedListener()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val currentLocation = LatLng(location.latitude, location.longitude)
                setMapCameraZoom(16.0)
                viewModel.initCurrentLocation(currentLocation)
            }
        }
    }

    private fun showOverlayOnCurrentLocation(currentLocation: LatLng) {
        naverMap.locationOverlay.apply {
            isVisible = true
            position = currentLocation
        }
    }

    private fun moveMapCameraToCurrentLocation(currentLocation: LatLng) {
        val cameraUpdate = CameraUpdate.scrollTo(
            currentLocation
        ).animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun setMapCameraZoom(level: Double) {
        naverMap.moveCamera(CameraUpdate.zoomTo(level))
    }

    private fun hasNotLocationPermission(): Boolean {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        else
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) return false
        }

        return true
    }

    private fun requestLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
                        || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
                        || permissions[Manifest.permission.ACCESS_BACKGROUND_LOCATION] ?: false
                -> {
                    binding.mvMap.getMapAsync(this)
                }
                else -> {

                }
            }
        }

        locationPermissionRequest.launch(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            else
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopLocationUpdates()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mvMap.onLowMemory()
    }
}