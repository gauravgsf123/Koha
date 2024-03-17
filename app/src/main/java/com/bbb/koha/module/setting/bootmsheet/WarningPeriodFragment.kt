package com.bbb.koha.module.setting.bootmsheet

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.bbb.koha.R
import com.bbb.koha.app.BaseBottomSheetDialogFragment
import com.bbb.koha.databinding.FragmentWarningPeriodBinding


class WarningPeriodFragment : BaseBottomSheetDialogFragment() {
    private lateinit var binding:FragmentWarningPeriodBinding
    private var chooseDays = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_warning_period,
            container,
            false
        )
        setupSpinnerChooseDays()
        return binding.root
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WarningPeriodFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun setupSpinnerChooseDays() {
        var searchByList = resources.getStringArray(R.array.choose_days)
        var arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,searchByList)
        binding?.spinnerChooseDays?.adapter = arrayAdapter
        binding?.spinnerChooseDays?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                chooseDays = searchByList[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
}