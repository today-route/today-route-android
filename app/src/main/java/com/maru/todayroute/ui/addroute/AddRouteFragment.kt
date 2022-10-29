package com.maru.todayroute.ui.addroute

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
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

    private val photoListAdapter by lazy { PhotoListAdapter() }
    private val photoList = mutableListOf<Bitmap>()

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            if (result.data?.clipData != null) {
                val data = result.data?.clipData
                data?.let { clipData ->
                    val count = data.itemCount

                    if (10 < count || 10 < photoList.size + count) {
                        Toast.makeText(requireContext(), "사진은 최대 10장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        for (i in 0 until clipData.itemCount) {
                            val uri = clipData.getItemAt(i).uri
                            if (28 <= Build.VERSION.SDK_INT) {
                                val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
                                photoList.add(ImageDecoder.decodeBitmap(source))
                            } else {
                                photoList.add(MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri))
                            }
                        }
                    }
                }
            } else {
                // 이미지를 한 장만 선택한 경우
                result.data?.data?.let { uri ->
                    if (28 <= Build.VERSION.SDK_INT) {
                        val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
                        photoList.add(ImageDecoder.decodeBitmap(source))
                    } else {
                        photoList.add(MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri))
                    }
                }
            }

            photoListAdapter.submitList(ArrayList(photoList))
        }
    }

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

        binding.rvPhotoList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = photoListAdapter
        }
    }

    private fun setupButtonClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnAddPhotos.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.CONTENT_TYPE)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            activityResultLauncher.launch(intent)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mvMap.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mvMap.onLowMemory()
    }
}