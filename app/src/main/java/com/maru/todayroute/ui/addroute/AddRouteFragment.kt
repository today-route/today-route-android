package com.maru.todayroute.ui.addroute

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentAddRouteBinding
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
class AddRouteFragment : BaseFragment<FragmentAddRouteBinding>(R.layout.fragment_add_route),
    OnMapReadyCallback {

    private val args by navArgs<AddRouteFragmentArgs>()

    private val viewModel: AddRouteViewModel by viewModels()
    private lateinit var naverMap: NaverMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycle.addObserver(
            MapViewLifecycleObserver(
                binding.mvMap,
                savedInstanceState
            )
        )
        viewModel.setGeoCoordList(args.geoCoordArray.toList())
        binding.mvMap.getMapAsync(this)
        setupButtonClickListener()
        setUpObserver()
    }

    private fun setupButtonClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpObserver() {
        viewModel.drawRoute.observe(viewLifecycleOwner) { geoCoordList ->
            drawRoute(geoCoordList)
        }

        viewModel.centerCoord.observe(viewLifecycleOwner) { center ->
            moveCameraToCenterCoordinate(center)
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        with (viewModel) {
            getCenterCoordinate()
            drawRoute()
        }
        unableGesturesExceptZoom()
    }

    private fun drawRoute(geoCoordList: List<LatLng>) {
        val path = PathOverlay()
        path.color = Color.rgb(114, 149, 185)
        path.outlineColor = Color.rgb(114, 149, 185)
        path.width = 20
        path.coords = geoCoordList
        path.map = naverMap
    }

    private fun moveCameraToCenterCoordinate(center: LatLng) {
        val cameraUpdate =
            CameraUpdate.scrollTo(center).animate(CameraAnimation.Easing)
        naverMap.moveCamera(CameraUpdate.zoomTo(14.0))
        naverMap.moveCamera(cameraUpdate)
    }

    private fun unableGesturesExceptZoom() {
        with(naverMap.uiSettings) {
            isScrollGesturesEnabled = false
            isTiltGesturesEnabled = false
            isRotateGesturesEnabled = false
        }
    }
}