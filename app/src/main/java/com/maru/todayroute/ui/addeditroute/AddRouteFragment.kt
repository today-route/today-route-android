package com.maru.todayroute.ui.addeditroute

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentAddRouteBinding
import com.maru.todayroute.util.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PathOverlay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class AddRouteFragment :
    BaseFragment<FragmentAddRouteBinding>(R.layout.fragment_add_route),
    OnMapReadyCallback {

    private val args by navArgs<AddRouteFragmentArgs>()

    private val viewModel: AddRouteViewModel by viewModels()
    private lateinit var naverMap: NaverMap

    private val photoListAdapter by lazy { PhotoListAdapter(viewModel::removePhotoAt) }

    private val selectMultipleImagesFromGalleryLauncher =
        AccessGalleryUtils.selectMultipleImagesFromGalleryLauncher(
            this,
            this::addPhotos
        )

    private val storagePermissionRequest =
        RequestPermissionsUtils.externalStoragePermissionRequest(
            this,
            this::selectMultipleImagesFromGallery
        )


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
            if (hasExternalStorageAccessPermission()) {
                selectMultipleImagesFromGallery()
            } else {
                storagePermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        }

        binding.btnSave.setOnClickListener {
            viewModel.trySaveNewRoute()
        }
    }

    private fun hasExternalStorageAccessPermission() = RequestPermissionsUtils.hasPermission(
        requireContext(),
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

    private fun selectMultipleImagesFromGallery() {
        AccessGalleryUtils.selectMultipleImagesFromGallery(selectMultipleImagesFromGalleryLauncher)
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
            }
            showToastMessage.observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    "사진은 최대 10장까지 선택 가능합니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            selectedPhotoUriList.observe(viewLifecycleOwner) { uriList ->
                val pathList = getRealPathFromUriList(uriList)
                val fileList = mutableListOf<File>()
                for (path in pathList) {
                    ImageHandler.optimizeImage(path)?.let { fileList.add(it) }
                }
                lifecycleScope.launch {
                    saveNewRoute(fileList, naverMap.cameraPosition.zoom)
                }
            }
            moveToRouteFragment.observe(viewLifecycleOwner) { routeId ->
                findNavController().navigate(AddRouteFragmentDirections.actionAddRouteFragmentToRouteFragment(routeId))
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

    private fun addPhotos(photos: List<Uri>) {
        viewModel.addPhotos(photos)
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