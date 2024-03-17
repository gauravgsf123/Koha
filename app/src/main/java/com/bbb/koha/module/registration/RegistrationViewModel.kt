package com.bbb.koha.module.registration

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbb.koha.R
import com.bbb.koha.login.model.UserDetailResponseModel
import com.bbb.koha.login.model.ValidateUserRequestModel
import com.bbb.koha.login.model.ValidateUserResponseModel
import com.bbb.koha.module.registration.model.AllCategoryResponseModel
import com.bbb.koha.module.registration.model.AllLibraryResponseModel
import com.bbb.koha.module.registration.model.RegisterUserRequestModel
import com.bbb.koha.module.registration.model.RegisterUserResponseModel
import com.bbb.koha.network.Resource
import com.bbb.koha.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response

class RegistrationViewModel(var app: Application) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private var mContext: Context = app.applicationContext
    private val repository = RegistrationRepository()
    private var mAllLibraryResponseModel = MutableLiveData<Resource<List<AllLibraryResponseModel>>>()
    var allLibraryResponseModel: LiveData<Resource<List<AllLibraryResponseModel>>> = mAllLibraryResponseModel
    private var mAllCategoryResponseModel = MutableLiveData<Resource<List<AllCategoryResponseModel>>>()
    var allCategoryResponseModel: LiveData<Resource<List<AllCategoryResponseModel>>> = mAllCategoryResponseModel
    private var mRegisterUserRequestModel = MutableLiveData<Resource<RegisterUserResponseModel>>()
    var registerUserRequestModel: LiveData<Resource<RegisterUserResponseModel>> = mRegisterUserRequestModel


    fun getLibraries() {
        if (Utils.hasInternetConnection(mContext)) {
            viewModelScope.launch {
                val response = repository.getLibraries()
                mAllLibraryResponseModel.value = response?.let { handleLibrariesResponse(it) }
            }
        } else mAllLibraryResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleLibrariesResponse(response: Response<List<AllLibraryResponseModel>>): Resource<List<AllLibraryResponseModel>>? {
            response.body()?.let {
                return when (response.code()) {
                    200 -> Resource.Success("Success",it)
                    else -> Resource.Error(app.resources.getString(R.string.some_thing_went_wrong))
                }
            }
        return Resource.Error(response.message())
    }

    fun getCategory() {
        if (Utils.hasInternetConnection(mContext)) {
            viewModelScope.launch {
                val response = repository.getCategory()
                mAllCategoryResponseModel.value = response?.let { handleCategoryResponse(it) }
            }
        } else mAllCategoryResponseModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleCategoryResponse(response: Response<List<AllCategoryResponseModel>>): Resource<List<AllCategoryResponseModel>>? {
        response.body()?.let {
            return when (response.code()) {
                200 -> Resource.Success("Success",it)
                else -> Resource.Error(app.resources.getString(R.string.some_thing_went_wrong))
            }
        }
        return Resource.Error(response.message())
    }

    fun registerUser(requestModel: RegisterUserRequestModel) {
        if (Utils.hasInternetConnection(mContext)) {
            mRegisterUserRequestModel.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = repository.registerUser(requestModel)
                mRegisterUserRequestModel.value = response?.let { handleRegisterUserResponse(it) }
            }
        } else mRegisterUserRequestModel.value =
            Resource.Error(app.resources.getString(R.string.no_internet))
    }

    private fun handleRegisterUserResponse(response: Response<RegisterUserResponseModel>): Resource<RegisterUserResponseModel>? {
        //if (response.isSuccessful) {
        response.body()?.let {
            return when (response.code()) {
                201 -> Resource.Success("Success",it)
                else -> Resource.Error(app.resources.getString(R.string.some_thing_went_wrong))
            }
        }
        // }
        return Resource.Error(response.message())
    }


}