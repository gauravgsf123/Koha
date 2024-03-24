package com.bbb.koha.network

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.bbb.koha.common.Constant
import com.bbb.koha.common.SharedPreference
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

open class AuthInterceptor (val context: Context) : Interceptor {
    protected lateinit var sharedPreference: SharedPreference
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isConnected()) {
            throw NoConnectivityException()
        }
        //var authToken = "MTcwMDAyNDk2MC05NDg0NDItMC44OTQ0NzI1MzYyMzcxMTctZHdmd1gxRVBVaXhWZ1RVTTFxcEhzMjFCVTRPTFBu"
        var authToken = SharedPreference(context).getValueString(Constant.ACCESS_TOKEN)
        //var credentials: String = Credentials.basic("koha@bbb.com", "openlx@321")
        var request = chain.request()
        request = request.newBuilder()
            .header("Content-Type", "Content-Type:application/x-www-form-urlencoded")
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            //.header("Authorization", credentials)
            .header("Authorization","Bearer $authToken")
            .build()

        return try {
            chain.proceed(request)
        } catch (e:Exception) {
            chain.proceed(chain.request().newBuilder().build())
        }

    }

    private fun isConnected(): Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}