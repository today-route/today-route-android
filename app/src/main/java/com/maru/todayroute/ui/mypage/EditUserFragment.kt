package com.maru.todayroute.ui.mypage

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentEditUserBinding
import com.maru.todayroute.util.BaseFragment
import com.maru.todayroute.util.ImageHandler
import com.maru.todayroute.util.RequestPermissionsUtils
import com.maru.todayroute.util.Utils.convertSingleToDoubleDigit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        setupObserver()
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
            btnSave.setOnClickListener {
                lifecycleScope.launch {
                    this@EditUserFragment.viewModel.editUser(
                        binding.etUserNickname.text.toString(),
                        binding.etUserBirthday.text.toString()
                    )
                }
            }
        }
    }

    private fun setupObserver() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            Glide.with(this).asBitmap().load(user.profileUrl).into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.ivUserProfile.setImageBitmap(bitmap)
                    viewModel.setProfileImageBitmap(bitmap)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
        }
        viewModel.moveToPreviousFragment.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
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
        val path = ImageHandler.getRealPathFromUriList(requireContext(), uri)
        val bitmap = ImageHandler.decodeBitmapFromUri(path)
        bitmap?.let {
            binding.ivUserProfile.setImageBitmap(bitmap)
            viewModel.setProfileImageBitmap(bitmap)
            viewModel.isFromGallery()
        }
    }
}