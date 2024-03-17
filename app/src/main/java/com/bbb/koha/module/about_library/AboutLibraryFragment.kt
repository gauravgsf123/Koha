package com.bbb.koha.module.about_library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bbb.koha.R
import com.bbb.koha.app.BaseFragment
import com.bbb.koha.databinding.FragmentAboutLibraryBinding
import com.bbb.koha.module.dashboard.DashboardActivity


class AboutLibraryFragment : BaseFragment() {
    private lateinit var binding : FragmentAboutLibraryBinding


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
            R.layout.fragment_about_library,
            container,
            false
        )
        (activity as DashboardActivity?)?.run {
            showToolbar(true)
            setAppTitle(resources.getString(R.string.about_library))
        }
        return binding.root
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AboutLibraryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}