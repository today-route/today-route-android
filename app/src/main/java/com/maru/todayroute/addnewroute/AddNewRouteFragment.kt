package com.maru.todayroute.addnewroute

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.maru.todayroute.MainActivity
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentAddNewRouteBinding
import com.maru.todayroute.home.MapViewLifecycleObserver
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PathOverlay


class AddNewRouteFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentAddNewRouteBinding? = null
    private val binding get() = _binding!!

    private lateinit var naverMap: NaverMap
    private val args by navArgs<AddNewRouteFragmentArgs>()
    private lateinit var geoCoordList: ArrayList<LatLng>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        geoCoordList = args.geoCoordList.toCollection(ArrayList())
        viewLifecycleOwner.lifecycle.addObserver(
            MapViewLifecycleObserver(
                binding.mvAddNewRouteMap,
                savedInstanceState
            )
        )
        binding.mvAddNewRouteMap.getMapAsync(this)
        setToolbar()
        setGroupSpinner()
    }

    private fun setToolbar() {
        val activity = activity as MainActivity
        activity.setSupportActionBar(binding.tbAddNewRoute)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.title = "새 루트"
        activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        setHasOptionsMenu(true)
    }

    private fun setGroupSpinner() {
        val groupsDummyData = arrayOf("성희", "은진", "카레모임") // for prototype
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            groupsDummyData
        )

        binding.spnGroupValue.adapter = spinnerAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_add_new_route_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("새 루트 생성을 취소하시겠습니까?")
                    .setMessage("취소하실 경우 기록된 루트가 삭제됩니다.")
                    .setNegativeButton("아니요") { _, _ ->
                    }
                    .setPositiveButton("네") { _, _ ->
                        findNavController().popBackStack()
                    }
                    .show()
            }
            R.id.menu_add_new_route_toolbar_save -> {
                // TODO: 서버에 루트 저장
                Snackbar.make(binding.root, "새로운 루트가 저장되었습니다.", Snackbar.LENGTH_SHORT).show()
                // TODO: 루트 보관함으로 이동
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        unableGesturesExceptZoom()
        drawRoute()
        moveMapCameraToCenterOfRoute()
    }

    private fun unableGesturesExceptZoom() {
        with(naverMap.uiSettings) {
            isScrollGesturesEnabled = false
            isTiltGesturesEnabled = false
            isRotateGesturesEnabled = false
        }
    }

    private fun drawRoute() {
        val path = PathOverlay()
        path.color = Color.YELLOW
        path.outlineColor = Color.YELLOW
        path.width = 20
        path.coords = geoCoordList
        path.map = naverMap
    }

    private fun moveMapCameraToCenterOfRoute() {
        val cameraUpdate =
            CameraUpdate.scrollTo(getCenterCoordinate()).animate(CameraAnimation.Easing)
        naverMap.moveCamera(CameraUpdate.zoomTo(13.0))
        naverMap.moveCamera(cameraUpdate)
    }

    private fun getCenterCoordinate(): LatLng {
        val latitudeList = geoCoordList.map { it.latitude }
        val longitudeList = geoCoordList.map { it.longitude }
        val minLatitude = latitudeList.minOrNull() ?: 0.0
        val maxLatitude = latitudeList.maxOrNull() ?: 0.0
        val minLongitude = longitudeList.minOrNull() ?: 0.0
        val maxLongitude = longitudeList.maxOrNull() ?: 0.0
        val centerLatitude = (minLatitude + maxLatitude) / 2
        val centerLongitude = (minLongitude + maxLongitude) / 2

        return LatLng(centerLatitude, centerLongitude)
    }
}