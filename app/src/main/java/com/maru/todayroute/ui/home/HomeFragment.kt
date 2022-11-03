package com.maru.todayroute.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
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

        viewModel.moveToAddRouteFragment.observe(viewLifecycleOwner) { geoCoordArray ->
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddRouteFragment(geoCoordArray))
        }

        viewModel.updatePath.observe(viewLifecycleOwner) { geoCoord ->
            updatePath(geoCoord)
        }

        viewModel.moveMapCameraToCurrentLocation.observe(viewLifecycleOwner) { currentLocation ->
            moveMapCameraToCurrentLocation(currentLocation)

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
    }

    @SuppressLint("MissingPermission")
    private fun setLocationChangedListener() {
        initLocationCallback()
        val locationRequest = LocationRequest.create().apply {
            interval = 1000
            priority = Priority.PRIORITY_HIGH_ACCURACY
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
            initPathOverlay()
            getLastLocation()
        }

        binding.btnCurrentLocation.setOnClickListener {
            viewModel.moveMapCameraToCurrentLocation()
        }
    }

    private fun updatePath(geoCoordList: List<LatLng>) {
        path.coords = geoCoordList
        if (path.map == null) {
            path.map = naverMap
        }
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

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mvMap.onLowMemory()
    }
}