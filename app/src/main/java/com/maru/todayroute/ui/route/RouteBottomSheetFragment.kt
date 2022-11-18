package com.maru.todayroute.ui.route

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maru.todayroute.R
import com.maru.todayroute.databinding.DialogRouteBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}