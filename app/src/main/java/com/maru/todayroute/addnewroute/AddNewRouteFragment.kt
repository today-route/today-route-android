package com.maru.todayroute.addnewroute

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.maru.todayroute.MainActivity
import com.maru.todayroute.R
import com.maru.todayroute.databinding.FragmentAddNewRouteBinding


class AddNewRouteFragment : Fragment() {
    private var _binding: FragmentAddNewRouteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        val groupsDummyData = arrayOf("성희", "은진", "카레모임")
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
                // TODO: 다이얼로그 띄우기
            }
            R.id.menu_add_new_route_toolbar_save -> {
                // TODO: 저장되었다는 메세지 띄우기
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}