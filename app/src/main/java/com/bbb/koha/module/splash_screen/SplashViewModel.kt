package com.bbb.koha.module.splash_screen

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbb.koha.R
import com.bbb.koha.dashboard.MainRepository
import com.bbb.koha.dashboard.TripSheetResponse
import com.bbb.koha.login.model.ValidateUserRequestModel
import com.bbb.koha.login.model.ValidateUserResponseModel
import com.bbb.koha.module.splash_screen.model.LibraryFeatureResponseModel
import com.bbb.koha.module.splash_screen.model.LibraryResponseModel
import com.bbb.koha.module.splash_screen.model.RequestModel
import com.bbb.koha.network.Resource
import com.bbb.koha.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class SplashViewModel(var app: Application) : ViewModel(){
    private var mContext: Context = app.applicationContext
    var repository = SplashRepository()
    var tokenResponse: MutableLiveData<TokenResponse?> = MutableLiveData()
    private var mLibraryResponseModel = MutableLiveData<Resource<List<LibraryResponseModel>>>()
    var libraryResponseModel: LiveData<Resource<List<LibraryResponseModel>>> = mLibraryResponseModel
    private var mLibraryFeatureResponseModel = MutableLiveData<Resource<List<LibraryFeatureResponseModel>>>()
    var libraryFeatureResponseModel: LiveData<Resource<List<LibraryFeatureResponseModel>>> = mLibraryFeatureResponseModel
    fun getToken() {
        viewModelScope.launch() {
            try {
                val response = repository.getToken()
                tokenResponse.value = response?.body()
            } catch (e: Exception) {
            }
        }
    }

    fun getLibraryDetail(requestModel: RequestModel) {
        if (Utils.hasInternetConnection(mContext)) {
            mLibraryResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getLibraryDetail(requestModel)
                mLibraryResponseModel.value = response?.let { handleLibraryDetailResponse(it) }
            }
        } else mLibraryResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleLibraryDetailResponse(response: Response<List<LibraryResponseModel>>): Resource<List<LibraryResponseModel>> {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(mContext.resources.getString(R.string.some_thing_went_wrong))
            }
        }
        return Resource.Error(response.message())
    }

    fun getLibraryFeature(requestModel: RequestModel) {
        if (Utils.hasInternetConnection(mContext)) {
            mLibraryFeatureResponseModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.getLibraryFeature(requestModel)
                mLibraryFeatureResponseModel.value = response?.let { handleLibraryFeatureResponse(it) }
            }
        } else mLibraryFeatureResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleLibraryFeatureResponse(response: Response<List<LibraryFeatureResponseModel>>): Resource<List<LibraryFeatureResponseModel>> {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(mContext.resources.getString(R.string.some_thing_went_wrong))
            }
        }
        return Resource.Error(response.message())
    }
}