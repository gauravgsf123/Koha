package com.bbb.koha.module.forgot

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbb.koha.R
import com.bbb.koha.app.BaseBottomSheetDialogFragment
import com.bbb.koha.app.BaseFragment
import com.bbb.koha.common.Constant
import com.bbb.koha.databinding.FragmentForgotBinding
import com.bbb.koha.module.otp.OTPFragment
import com.bbb.koha.network.Resource
import com.bbb.koha.network.ViewModelFactoryClass
import com.bbb.koha.utils.Mailer
import com.bbb.koha.utils.ProgressDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


class ForgotFragment : BaseBottomSheetDialogFragment() {
    private lateinit var binding:FragmentForgotBinding
    private lateinit var viewModel:ForgotViewModel
    private var type = Constant.VerificationType.EMAIL

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
            R.layout.fragment_forgot,
            container,
            false
        )
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[ForgotViewModel::class.java]
        binding?.run {
            tvSkip.setOnClickListener(this@ForgotFragment)
            btnSubmit.setOnClickListener(this@ForgotFragment)
        }
        setObserver()
        return binding.root
    }

    private fun setObserver() {
        viewModel.userDetailResponseModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    if(response?.data?.isNotEmpty() == true) {
                        //sendMailOrSMS(response?.data?.get(0)?.patronId)
                        dismiss()
                        if(type == Constant.VerificationType.EMAIL){
                            OTPFragment.newInstance(response?.data?.get(0)?.patronId,type,binding.etEmail.text.toString())
                                .show(requireFragmentManager(), "OTPFragment")
                        }else if(type == Constant.VerificationType.MOBILE){
                            OTPFragment.newInstance(response?.data?.get(0)?.patronId,type,binding.etMobile.text.toString())
                                .show(requireFragmentManager(), "OTPFragment")
                        }

                    }else showToast(getString(R.string.no_result_found))
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

    private fun sendOTP(){

    }



    private fun validate(){
        binding?.run {
            if (!TextUtils.isEmpty(etEmail.text.toString())) {
                type = Constant.VerificationType.EMAIL
                var query = "{\"email\":{\"-like\":\"${etEmail.text.trim()}\"}}"
                viewModel.getUserDetailByEmail(query)
            } else if (!TextUtils.isEmpty(etMobile.text.toString())) {
                type = Constant.VerificationType.MOBILE
                var query = "{\"phone\":{\"-like\":\"${etMobile.text.trim()}\"}}"
                viewModel.getUserDetailByEmail(query)
            }else{
                showToast(getString(R.string.please_enter_either_email_or_mobile))
            }
        }
    }

    override fun onClick(view: View?) {
        binding?.run {
            when (view?.id) {
                tvSkip.id->{dismiss()}
                btnSubmit.id->{
                    hideKeyboard(view)
                    validate()
                }

            }
        }
    }

    override fun initializeView() {

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ForgotFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}