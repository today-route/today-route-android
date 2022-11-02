package com.maru.todayroute.ui.route

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentRouteBinding
import com.maru.todayroute.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RouteFragment : BaseFragment<FragmentRouteBinding>(R.layout.fragment_route) {

    private val viewModel: RouteViewModel by viewModels()
    private val imageListAdapter by lazy { ImageListAdapter(binding) }

    private val args: RouteFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRouteDiaryData()
        setupButtonClickListener()
        setupRecyclerView()
        setupObserver()
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
            mapZoomLevel.observe(viewLifecycleOwner) { zoomLevel ->
                // TODO: Naver Map Zoom level 조정
            }
            photoUrlList.observe(viewLifecycleOwner) { photoUrlList ->
                imageListAdapter.submitList(photoUrlList)
            }
            geoCoordList.observe(viewLifecycleOwner) { geoCoordList ->
                // TODO: Naver Map에 좌표 그리기
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
}