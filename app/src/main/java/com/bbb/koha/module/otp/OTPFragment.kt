package com.bbb.koha.module.otp

import android.R.attr.phoneNumber
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bbb.koha.R
import com.bbb.koha.app.BaseBottomSheetDialogFragment
import com.bbb.koha.common.Constant
import com.bbb.koha.databinding.FragmentOtpBinding
import com.bbb.koha.module.set_new_password.SetNewPasswordFragment
import com.bbb.koha.utils.Mailer
import com.bbb.koha.utils.ProgressDialog
import com.bbb.koha.utils.Utils
import com.msg91.sendotpandroid.library.listners.VerificationListener
import com.msg91.sendotpandroid.library.roots.SendOTPConfigBuilder
import com.msg91.sendotpandroid.library.roots.SendOTPResponseCode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class OTPFragment : BaseBottomSheetDialogFragment(), VerificationListener {
    private lateinit var binding:FragmentOtpBinding
    private var otp = "1234"
    private var type = Constant.VerificationType.EMAIL
    private var id = ""
    private var patronId:Int = 0
    private var OTP_LNGTH = 4
    override fun initializeView() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            patronId = it.getInt(Constant.PATRON_ID)
            type = it.getString(Constant.VERIFICATION_TYPE).toString()
            id = it.getString(Constant.ID).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_otp,
            container,
            false
        )
        binding?.run {
            btnContinue.setOnClickListener(this@OTPFragment)
            btnResend.setOnClickListener(this@OTPFragment)
        }
        runTimerTask()
        if(type == Constant.VerificationType.EMAIL){
            sendOTPonMail(id)
        }else if(type == Constant.VerificationType.MOBILE){
            sendOTPonMobile(id)
        }
        return binding.root
    }

    override fun onClick(p0: View?) {
        binding.run {
            when(p0?.id){
                btnContinue.id->{
                    if(otp==binding.pinview.value){
                        dismiss()
                        SetNewPasswordFragment.newInstance(patronId).show(requireFragmentManager(),"SetNewPasswordFragment")
                    }else showToast("Wrong OTP entered")
                }
                btnResend.id->{
                    if(type == Constant.VerificationType.EMAIL){
                        sendOTPonMail(id)
                    }else if(type == Constant.VerificationType.MOBILE){
                        sendOTPonMobile(id)
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(patronId: Int?,type:String,id:String) =
            OTPFragment().apply {
                arguments = Bundle().apply {
                    if (patronId != null) {
                        putInt(Constant.PATRON_ID, patronId)
                        putString(Constant.VERIFICATION_TYPE, type)
                        putString(Constant.ID, id)
                    }
                }
            }
    }

    @SuppressLint("CheckResult")
    private fun sendOTPonMail(email:String) {
        ProgressDialog.showProgressBar(requireContext())
        otp = Utils.getOTP()
        Mailer.sendMail(
            email,
            "Forgot Password KOHA",
            "Your OTP is $otp"
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d("OTP",otp)
                    ProgressDialog.hideProgressBar()
                    Toast.makeText(requireContext(), "OTP has been sent", Toast.LENGTH_SHORT).show()
                    dismiss()
                    runTimerTask()
                }, {
                    Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
                }
            )
    }

    private fun sendOTPonMobile(mobile:String) {
        ProgressDialog.showProgressBar(requireContext())
        otp = Utils.getOTP()
        SendOTPConfigBuilder()
            .setCountryCode(91)
            .setMobileNumber(mobile)
            .setVerifyWithoutOtp(true) //direct verification while connect with mobile network
            .setAutoVerification(activity) //Auto read otp from Sms And Verify
            .setSenderId("BESTBB")
            .setMessage("$otp is Your verification digits test.")
            .setOtpLength(OTP_LNGTH)
            .setVerificationCallBack(this).build()

    }

    private fun runTimerTask() {
        binding.btnResend.isEnabled = false
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                activity?.runOnUiThread(Runnable { binding.btnResend.text = "Resend in " + millisUntilFinished / 1000 })
            }
            override fun onFinish() {
                binding.btnResend.text = "Resend"
                binding.btnResend.isEnabled = true
            }
        }.start()
    }

    override fun onSendOtpResponse(responseCode: SendOTPResponseCode?, message: String?) {
        activity?.runOnUiThread(Runnable {
            if (responseCode === SendOTPResponseCode.DIRECT_VERIFICATION_SUCCESSFUL_FOR_NUMBER || responseCode === SendOTPResponseCode.OTP_VERIFIED) {
                //otp verified OR direct verified by send otp 2.O
            } else if (responseCode === SendOTPResponseCode.READ_OTP_SUCCESS) {
                //Auto read otp from sms successfully
                // you can get otp form message filled
            } else if (responseCode === SendOTPResponseCode.SMS_SUCCESSFUL_SEND_TO_NUMBER || responseCode === SendOTPResponseCode.DIRECT_VERIFICATION_FAILED_SMS_SUCCESSFUL_SEND_TO_NUMBER) {
                // Otp send to number successfully
            } else {
                //exception found
            }
        })
    }
}