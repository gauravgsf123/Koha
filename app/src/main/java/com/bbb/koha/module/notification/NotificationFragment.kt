package com.bbb.koha.module.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbb.koha.R
import com.bbb.koha.app.BaseFragment
import com.bbb.koha.databinding.FragmentNotificationBinding
import com.bbb.koha.module.dashboard.DashboardActivity
import com.bbb.koha.module.dashboard.DashboardViewModel
import com.bbb.koha.module.my_account.charges.ChargeAdapter
import com.bbb.koha.module.my_account.charges.ChargesResponseModel
import com.bbb.koha.module.notification.model.NotificationModel
import com.bbb.koha.module.notification.model.NotificationRequestModel
import com.bbb.koha.network.Resource
import com.bbb.koha.network.ViewModelFactoryClass
import com.bbb.koha.utils.ProgressDialog


class NotificationFragment : BaseFragment() {
    private lateinit var binding:FragmentNotificationBinding
    private lateinit var viewModel:NotificationViewModel
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
            R.layout.fragment_notification,
            container,
            false
        )
        (activity as DashboardActivity?)?.run {
            showToolbar(true)
            setAppTitle(getString(R.string.notifications))
        }
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[NotificationViewModel::class.java]
        viewModel.getNotification(NotificationRequestModel("1"))
        setObserver()
        return binding.root
    }

    private fun setObserver(){
        viewModel.notificationRequestModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    response.data?.let { setupRecylerView(it) }
                }
                is Resource.Loading -> {
                    ProgressDialog.showProgressBar(requireContext())
                }
                is Resource.Error -> {
                    ProgressDialog.hideProgressBar()
                    response.message?.let { showToast(it) }
                }
                else -> {
                    ProgressDialog.hideProgressBar()
                    response.message?.let { showToast(it) }
                }
            }
        }
    }

    private fun setupRecylerView(data:List<NotificationModel>){
        binding.rvNotification.adapter = NotificationAdapter(data as ArrayList<NotificationModel>)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            NotificationFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}