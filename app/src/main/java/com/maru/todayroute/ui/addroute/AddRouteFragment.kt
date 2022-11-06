package com.maru.todayroute.ui.addroute

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
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
import java.io.File

@AndroidEntryPoint
class AddRouteFragment : BaseFragment<FragmentAddRouteBinding>(R.layout.fragment_add_route),
    OnMapReadyCallback {

    private val args by navArgs<AddRouteFragmentArgs>()

    private val viewModel: AddRouteViewModel by viewModels()
    private lateinit var naverMap: NaverMap

    private val photoListAdapter by lazy { PhotoListAdapter(viewModel::removePhotoAt) }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val photoList = mutableListOf<Uri>()

                if (result.data?.clipData != null) {
                    val data = result.data?.clipData
                    data?.let { clipData ->
                        for (i in 0 until clipData.itemCount) {
                            val uri = clipData.getItemAt(i).uri
                            photoList.add(uri)
                        }
                    }
                } else {
                    // 이미지를 한 장만 선택한 경우
                    result.data?.data?.let { uri ->
                        photoList.add(uri)
                    }
                }
                viewModel.addPhotos(photoList)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewLifecycleOwner.lifecycle.addObserver(
            MapViewLifecycleObserver(
                binding.mvMap,
                savedInstanceState
            )
        )
        viewModel.setGeoCoordList(args.geoCoordArray.toList())
        viewModel.setDate(args.date)
        binding.mvMap.getMapAsync(this)
        setupButtonClickListener()
        setUpObserver()
        initRecyclerViewAdapter()
    }

    @SuppressLint("Range")
    private fun getRealPathFromUriList(uriList: List<Uri>): List<String> {
        val realPathList = mutableListOf<String>()

        for (uri in uriList) {
            val proj = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = requireActivity().contentResolver.query(uri, proj, null, null, null)
            cursor!!.moveToNext()
            val path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
            cursor.close()
            realPathList.add(path)
        }

        return realPathList
    }

    private fun initRecyclerViewAdapter() {
        binding.rvPhotoList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = photoListAdapter
        }
    }

    private fun setupButtonClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnAddPhotos.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Media.CONTENT_TYPE
            )
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            activityResultLauncher.launch(intent)
        }

        binding.btnSave.setOnClickListener {
            viewModel.saveNewRoute(naverMap.cameraPosition.zoom)
        }
    }

    private fun setUpObserver() {
        with(viewModel) {
            drawRoute.observe(viewLifecycleOwner) { geoCoordList ->
                drawRoute(geoCoordList)
            }
            centerCoord.observe(viewLifecycleOwner) { center ->
                moveCameraToCenterCoordinate(center)
            }
            photoUriList.observe(viewLifecycleOwner) { photoUriList ->
                photoListAdapter.submitList(photoUriList)
                viewModel.setPhotoRealPathList(getRealPathFromUriList(photoUriList))
            }
            showToastMessage.observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    "사진은 최대 10장까지 선택 가능합니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        with(viewModel) {
            getCenterCoordinate()
            drawRoute()
        }
        unableGesturesExceptZoom()
    }

    private fun drawRoute(geoCoordList: List<LatLng>) {
        val path = PathOverlay()
        with(path) {
            color = requireContext().getColor(R.color.purple)
            outlineColor = requireContext().getColor(R.color.purple)
            width = 30
            coords = geoCoordList
            map = naverMap
        }
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