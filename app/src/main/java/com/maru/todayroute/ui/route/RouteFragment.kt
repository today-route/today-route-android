package com.maru.todayroute.ui.route

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentRouteBinding
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
class RouteFragment : BaseFragment<FragmentRouteBinding>(R.layout.fragment_route), OnMapReadyCallback {

    private val viewModel: RouteViewModel by viewModels()
    private val imageListAdapter by lazy { ImageListAdapter(binding) }

    private lateinit var naverMap: NaverMap

    private val args: RouteFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRouteDiaryData()
        setupMap(savedInstanceState)
        setupButtonClickListener()
        setupRecyclerView()
        setupObserver()
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycle.addObserver(
            MapViewLifecycleObserver(
                binding.mvRouteMap,
                savedInstanceState
            )
        )
        binding.mvRouteMap.getMapAsync(this)
    }

    private fun setRouteDiaryData() {
        viewModel.setRouteDiaryData(args.routeId)
    }

    private fun setupObserver() {
        with (viewModel) {
            date.observe(viewLifecycleOwner) { date ->
                binding.tvRouteDateTop.text = date
            }
            title.observe(viewLifecycleOwner) { title ->
                binding.tvRouteTitle.text = title
            }
            contents.observe(viewLifecycleOwner) { contents ->
                binding.tvContents.text = contents
            }
            photoUrlList.observe(viewLifecycleOwner) { photoUrlList ->
                imageListAdapter.submitList(photoUrlList)
            }
        }
    }

    private fun setupMapObserver() {
        with (viewModel) {
            mapZoomLevel.observe(viewLifecycleOwner) { zoomLevel ->
                naverMap.moveCamera(CameraUpdate.zoomTo(zoomLevel.toDouble()))
            }
            centerCoord.observe(viewLifecycleOwner) { center ->
                moveCameraToCenterCoordinate(center)
            }
            geoCoordList.observe(viewLifecycleOwner) { geoCoordList ->
                drawRoute(geoCoordList)
            }
        }
    }


    private fun setupRecyclerView() {
        with (binding.rvRouteImage) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = imageListAdapter
        }
    }

    private fun setupButtonClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        setupMapObserver()
        viewModel.setGeoCoordsOnMap()
        with(viewModel) {
            getCenterCoordinate()
        }
        unableGestures()
    }

    private fun drawRoute(geoCoordList: List<LatLng>) {
        val path = PathOverlay()
        path.color = Color.rgb(114, 149, 185)
        path.outlineColor = Color.rgb(114, 149, 185)
        path.width = 20
        path.coords = geoCoordList
        path.map = naverMap
    }

    private fun unableGestures() {
        with(naverMap.uiSettings) {
            isScrollGesturesEnabled = false
            isTiltGesturesEnabled = false
            isRotateGesturesEnabled = false
            isZoomGesturesEnabled = false
            isZoomControlEnabled = false
        }
    }

    private fun moveCameraToCenterCoordinate(center: LatLng) {
        val cameraUpdate =
            CameraUpdate.scrollTo(center).animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
    }
}