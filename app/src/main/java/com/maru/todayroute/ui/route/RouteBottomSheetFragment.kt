package com.maru.todayroute.ui.route

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maru.todayroute.R
import com.maru.todayroute.databinding.DialogRouteBottomSheetBinding

class RouteBottomSheetFragment : BottomSheetDialogFragment() {

    private val args: RouteBottomSheetFragmentArgs by navArgs()
    private var _binding: DialogRouteBottomSheetBinding? = null
    private val binding get() = _binding ?: error("binding이 초기화되지 않았습니다.")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_route_bottom_sheet, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonClickListener()
    }

    private fun setupButtonClickListener() {
        binding.btnEditRoute.setOnClickListener {
            findNavController().navigate(
                RouteBottomSheetFragmentDirections.actionRouteBottomSheetToEditRouteFragment(
                    args.routeId
                )
            )
        }
        binding.btnDeleteRoute.setOnClickListener {
            showAlertDialog("루트 삭제", "정말로 루트를 삭제하시겠습니까?", "삭제") {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    "DELETE",
                    true
                )
                findNavController().popBackStack()
            }
        }
    }

    private fun showAlertDialog(title: String, message: String, positive: String, positiveButtonClicked: () -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positive) { _, _ ->
                positiveButtonClicked.invoke()
            }
            .setNegativeButton("아니요") { dialog, _ ->
                dialog.dismiss()
            }
        alertDialogBuilder.show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}