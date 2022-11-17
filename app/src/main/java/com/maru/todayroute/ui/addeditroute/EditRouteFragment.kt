package com.maru.todayroute.ui.addeditroute

import android.Manifest
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentEditRouteBinding
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
class EditRouteFragment :
    BaseFragment<FragmentEditRouteBinding>(R.layout.fragment_edit_route),
    OnMapReadyCallback {

    private val args: EditRouteFragmentArgs by navArgs()

    private val viewModel: EditRouteViewModel by viewModels()
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
        setupMap(savedInstanceState)
        setRouteData()
        setupObserver()
        setupClickListener()
        initRecyclerViewAdapter()
    }

    private fun setupObserver() {
        with(viewModel) {
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
            selectedPhotoUriList.observe(viewLifecycleOwner) { photoUriList ->
                val fileList = mutableListOf<File>()
                photoUriList.forEach { uri ->
                    if (uri.toString().startsWith("https://")) {
                        Glide.with(requireContext()).asBitmap().load(uri).into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                                val file = ImageHandler.optimizeImage(bitmap, 100)
                                file?.let { fileList.add(it) }
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                    } else {
                        val path = ImageHandler.getRealPathFromUriList(requireActivity(), uri)
                        ImageHandler.decodeBitmapFromUri(path)?.let { bitmap ->
                            ImageHandler.optimizeImage(bitmap, 70)?.let { fileList.add(it) }
                        }
                    }
                }

                lifecycleScope.launch {
                    viewModel.editRoute(naverMap.cameraPosition.zoom, fileList)
                }
            }
            moveToPreviousFragment.observe(viewLifecycleOwner) {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupMapObserver() {
        with (viewModel) {
            geoCoordList.observe(viewLifecycleOwner) { geoCoordList ->
                drawRoute(geoCoordList)
            }
            mapSettings.observe(viewLifecycleOwner) {
                moveCameraToCenterCoordinate(it.first, it.second)
            }
        }
    }

    private fun setupClickListener() {
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
            viewModel.tryEditRoute()
        }
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycle.addObserver(
            MapViewLifecycleObserver(
                binding.mvMap,
                savedInstanceState
            )
        )
        binding.mvMap.getMapAsync(this)
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

    private fun moveCameraToCenterCoordinate(center: LatLng, zoomLevel: Double) {
        val cameraUpdate =
            CameraUpdate.scrollTo(center).animate(CameraAnimation.Easing)
        naverMap.moveCamera(CameraUpdate.zoomTo(zoomLevel))
        naverMap.moveCamera(cameraUpdate)
    }

    private fun setRouteData() {
        lifecycleScope.launch {
            viewModel.setRouteData(args.routeId)
        }
    }

    private fun addPhotos(photos: List<Uri>) {
        viewModel.addPhotos(photos)
    }

    private fun hasExternalStorageAccessPermission() = RequestPermissionsUtils.hasPermission(
        requireContext(),
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

    private fun initRecyclerViewAdapter() {
        binding.rvPhotoList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = photoListAdapter
        }
    }

    private fun selectMultipleImagesFromGallery() {
        AccessGalleryUtils.selectMultipleImagesFromGallery(selectMultipleImagesFromGalleryLauncher)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        setupMapObserver()
        with(naverMap.uiSettings) {
            isScrollGesturesEnabled = false
            isTiltGesturesEnabled = false
            isRotateGesturesEnabled = false
        }
    }
}