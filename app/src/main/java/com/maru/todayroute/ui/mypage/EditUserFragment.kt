package com.maru.todayroute.ui.mypage

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentEditUserBinding
import com.maru.todayroute.util.BaseFragment
import com.maru.todayroute.util.RequestPermissionsUtils
import com.maru.todayroute.util.Utils.convertSingleToDoubleDigit
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditUserFragment : BaseFragment<FragmentEditUserBinding>(R.layout.fragment_edit_user) {

    private val viewModel: EditUserViewModel by viewModels()
    private val args: EditUserFragmentArgs by navArgs()

    private val storagePermissionRequest =
        RequestPermissionsUtils.externalStoragePermissionRequest(
            this,
            this::selectImagesFromGallery
        )

    private val requestGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        if (it.resultCode == RESULT_OK && it.data != null) {
            try {
                val currentImageUri = it.data!!.data
                currentImageUri?.let { uri ->
                    setNewProfileImage(uri)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private val birthday by lazy {
        args.user.birthday.substring(0 until 10).split("-").map { it.toInt() }
            .toMutableList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        viewModel.setUserData(args.user)
        birthday[1] -= 1
        setupButtonClickListener()
    }

    private fun setupButtonClickListener() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            etUserBirthday.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    { _, year, m, d ->
                        val month = (m + 1).toString().convertSingleToDoubleDigit()
                        val dayOfMonth = d.toString().convertSingleToDoubleDigit()
                        binding.etUserBirthday.setText("${year}-${month}-${dayOfMonth}")
                        birthday[0] = year
                        birthday[1] = m
                        birthday[2] = d
                    },
                    birthday[0],
                    birthday[1],
                    birthday[2]
                ).show()
            }
            val editPhotoButtonList = listOf(ivUserProfile, btnEditProfile)
            editPhotoButtonList.forEach { button ->
                button.setOnClickListener {
                    if (hasExternalStorageAccessPermission()) {
                        selectImagesFromGallery()
                    } else {
                        storagePermissionRequest.launch(
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        )
                    }
                }
            }
        }
    }

    private fun hasExternalStorageAccessPermission() = RequestPermissionsUtils.hasPermission(
        requireContext(),
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

    private fun selectImagesFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.type = "image/*"
        requestGalleryLauncher.launch(intent)
    }

    private fun setNewProfileImage(uri: Uri) {
        Glide.with(binding.ivUserProfile).load(uri).into(binding.ivUserProfile)
    }
}