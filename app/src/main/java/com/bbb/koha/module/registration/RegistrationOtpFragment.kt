package com.bbb.koha.module.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bbb.koha.R
import com.bbb.koha.app.BaseBottomSheetDialogFragment
import com.bbb.koha.databinding.FragmentRegistrationOtpBinding
import com.bbb.koha.module.registration.model.RegisterUserRequestModel


class RegistrationOtpFragment(var listener: OnActionCompleteListener) : BaseBottomSheetDialogFragment() {
    private lateinit var binding:FragmentRegistrationOtpBinding
    private var otp = "1234"
    private lateinit var registerUserRequestModel: RegisterUserRequestModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            registerUserRequestModel = it.getParcelable("requestModel")!!
            otp = it.getString("otp")!!

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_registration_otp,
            container,
            false
        )
        binding?.run {
            btnContinue.setOnClickListener{
                if(otp==binding.pinview.value){
                    dismiss()
                    listener.onActionComplete(registerUserRequestModel)
                    //SetNewPasswordFragment.newInstance(patronId).show(requireFragmentManager(),"SetNewPasswordFragment")
                }
            }
        }

        return binding.root
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(registerUserRequestModel: RegisterUserRequestModel, otp: String, listener:OnActionCompleteListener) =
            RegistrationOtpFragment(listener).apply {
                arguments = Bundle().apply {
                    putParcelable("requestModel",registerUserRequestModel)
                    putString("otp",otp)
                }
            }
    }

    interface OnActionCompleteListener {
        fun onActionComplete(registerUserRequestModel: RegisterUserRequestModel)
    }
}