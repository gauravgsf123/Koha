package com.bbb.koha.module.splash_screen

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bbb.koha.R
import com.bbb.koha.app.BaseActivity
import com.bbb.koha.app.MVVMBindingActivity
import com.bbb.koha.common.Constant
import com.bbb.koha.common.SharedPreference
import com.bbb.koha.dashboard.MainViewModel
import com.bbb.koha.databinding.ActivityMainBinding
import com.bbb.koha.databinding.ActivitySplashBinding
import com.bbb.koha.login.LoginActivity
import com.bbb.koha.module.dashboard.DashboardActivity
import com.bbb.koha.module.splash_screen.model.RequestModel
import com.bbb.koha.network.Resource
import com.bbb.koha.network.ViewModelFactoryClass
import com.bbb.koha.service.LocationService
import com.bbb.koha.service.TokenRefreshService
import com.bbb.koha.utils.ProgressDialog
import com.bbb.koha.utils.Utils
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SplashActivity : MVVMBindingActivity<ActivitySplashBinding>() {
    private lateinit var viewModel: SplashViewModel
    var c: Date? = null
    var df: SimpleDateFormat? = null
    override fun initializeView() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(application)
        )[SplashViewModel::class.java]
        setObserver()
        checkCurrentDate()
    }

    override fun provideViewResource(): Int {
        return R.layout.activity_splash
    }

    private fun checkCurrentDate(){
        df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        c = Calendar.getInstance().time
        val currentDate = df!!.format(c)
        Log.d("current_date",currentDate+sharedPreference.getValueString(Constant.CURRENT_DATE))
        if(currentDate!=sharedPreference.getValueString(Constant.CURRENT_DATE)){
            var request = RequestModel("1")
            viewModel.getLibraryDetail(request)
        }else viewModel.getToken()
        sharedPreference.save(Constant.CURRENT_DATE,currentDate)

    }

    override fun onClick(p0: View?) {
    }

    private fun setObserver() {
        viewModel.tokenResponse.observe(this, Observer {
            it?.let { it1 ->
                sharedPreference.save(Constant.ACCESS_TOKEN, it1.accessToken!!)
                sharedPreference.save(Constant.TOKEN_TYPE, it1.tokenType!!)
                sharedPreference.save(Constant.EXPIRES_IN, it1.expiresIn!!)
            }
            CoroutineScope(Dispatchers.Main).launch {
                delay(100)
                if (sharedPreference.getValueBoolean(Constant.IS_LOGIN, false)) {
                    startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()
                }
            }
        })

        viewModel.libraryResponseModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    var endDate = response?.data?.get(0)?.endDate
                    var isSubscription = endDate?.let { Utils.isSubscription(it) }
                    if (isSubscription != null && isSubscription<1) {
                        showConfirmation()
                    }else{
                        var request = RequestModel("1")
                        viewModel.getLibraryFeature(request)
                    }

                }
                is Resource.Loading -> {
                    ProgressDialog.showProgressBar(this)
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
        viewModel.libraryFeatureResponseModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    viewModel.getToken()
                    ProgressDialog.hideProgressBar()
                    response.data?.get(0)?.let {it1->
                        sharedPreference.save(Constant.DISCHARGE, it1.discharge?.toInt()!!)
                        sharedPreference.save(Constant.HOLD, it1.hold?.toInt()!!)
                        sharedPreference.save(Constant.PAYMENT_GATEWAY, it1.paymentGateway?.toInt()!!)
                        sharedPreference.save(Constant.SMS, it1.sms?.toInt()!!)
                        sharedPreference.save(Constant.RENEW, it1.renew?.toInt()!!)
                    }
                }
                is Resource.Loading -> {
                    ProgressDialog.showProgressBar(this)
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


    private fun showConfirmation() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your subscription has been expired. Please contact your Admin")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
                finishAffinity()
            }
        val alert = builder.create()
        alert.show()
    }

    override fun onStop() {
        super.onStop()
        stopService()
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            //if(!LocationService().isInstanceCreated()){
            if(!isMyServiceRunning(TokenRefreshService::class.java)){
                stopService()
                startService(Intent(this,TokenRefreshService::class.java))
            }
        }, 2000)
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager: ActivityManager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun stopService(){
        stopService(Intent(this,TokenRefreshService::class.java))
    }
}