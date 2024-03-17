package com.bbb.koha.module.my_account.charges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbb.koha.R
import com.bbb.koha.app.BaseFragment
import com.bbb.koha.common.Constant
import com.bbb.koha.databinding.FragmentChargesBinding
import com.bbb.koha.module.dashboard.DashboardActivity
import com.bbb.koha.network.Resource
import com.bbb.koha.network.ViewModelFactoryClass
import com.bbb.koha.utils.ProgressDialog


class ChargesFragment : BaseFragment() {
    private lateinit var binding:FragmentChargesBinding
    private lateinit var viewModel:ChargesViewModel
    var totalAmount = 0

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
            R.layout.fragment_charges,
            container,
            false
        )
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[ChargesViewModel::class.java]
        (activity as DashboardActivity?)?.run {
            showToolbar(true)
            //setTitle(getString(R.string.your_personal_detail))
        }
        viewModel.getCharges(sharedPreference.getValueInt(Constant.PATRON_ID))
        setObserver()
        binding.tvFine.setOnClickListener(this)
        binding.tvPay.setOnClickListener(this)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        selectedView(binding.tvFine)
    }

    private fun setObserver() {
        viewModel.chargesResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    setupChargeRecylerView(response.data!!)
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

    private fun setupChargeRecylerView(data:List<ChargesResponseModel>){
        data.forEach {
            totalAmount+=it.amountOutstanding!!
        }
        binding.tvTotalDue.text = "₹ ${totalAmount.toFloat()}"
        binding.rvCharge.adapter = ChargeAdapter(data as ArrayList<ChargesResponseModel>)
    }

    override fun onClick(p0: View?) {
        binding.run {
            when(p0?.id){
                tvFine.id->selectedView(tvFine)
                tvPay.id->{
                    selectedView(tvPay)
                    var bundle = Bundle()
                    bundle.putString(Constant.TOTAL_AMOUNT,"$totalAmount")
                    bundle.putString(Constant.TOTAL_AMOUNT,"$totalAmount")
                    val fragmentPayBottomSheetFragment = PayBottomSheetFragment(object : CallBackInterface{
                        override fun onBackPress() {
                            selectedView(binding.tvFine)
                        }

                    })
                    fragmentPayBottomSheetFragment.arguments = bundle
                    fragmentPayBottomSheetFragment.show(parentFragmentManager,"PayBottomSheetFragment")
                }
            }
        }
    }

    private fun selectedView(textView: TextView){
        binding.run {
            tvFine.isSelected = false
            tvPay.isSelected = false
            tvFine.setTextColor(resources.getColor(R.color.primary_dark))
            tvPay.setTextColor(resources.getColor(R.color.primary_dark))
        }
        textView.isSelected = true
        textView.setTextColor(resources.getColor(R.color.white))
    }

    interface CallBackInterface{
        fun onBackPress()
    }

}