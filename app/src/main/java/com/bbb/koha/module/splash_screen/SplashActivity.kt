package com.bbb.koha.module.splash_screen

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
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
import com.bbb.koha.network.ViewModelFactoryClass
import com.bbb.koha.service.LocationService
import com.bbb.koha.service.TokenRefreshService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : MVVMBindingActivity<ActivitySplashBinding>() {
    private lateinit var viewModel: SplashViewModel
    override fun initializeView() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(application)
        )[SplashViewModel::class.java]
        viewModel.getToken()
        setObserver()
    }

    override fun provideViewResource(): Int {
        return R.layout.activity_splash
    }

    override fun onClick(p0: View?) {
    }

    private fun setObserver() {
        viewModel.tokenResponse.observe(this, Observer {
            val gson = Gson().toJson(it)
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
            Log.d("response","$gson")
        })
    }

    override fun onStop() {
        super.onStop()
        stopLocationService()
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            //if(!LocationService().isInstanceCreated()){
            if(!isMyServiceRunning(TokenRefreshService::class.java)){
                stopLocationService()
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

    private fun stopLocationService(){
        stopService(Intent(this,TokenRefreshService::class.java))
    }
}