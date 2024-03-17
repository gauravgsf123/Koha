package com.bbb.koha.module.my_account.purchase_suggestions

import com.bbb.koha.login.model.ValidateUserRequestModel
import com.bbb.koha.module.my_account.purchase_suggestions.model.SuggestionModel
import com.bbb.koha.network.RetrofitInstance


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class SuggestionRepository {
    suspend fun getSuggestions(suggestedBy:String?) = RetrofitInstance.apiService?.getSuggestions(suggestedBy)
    suspend fun getLibraries() = RetrofitInstance.apiService?.getLibraries()
    suspend fun getItem() = RetrofitInstance.apiService?.getItem()
    suspend fun addSuggestions(suggestionModel: SuggestionModel) = RetrofitInstance.apiService?.addSuggestions(suggestionModel)
    suspend fun deleteSuggestions(suggestionId: Int) = RetrofitInstance.apiService?.deleteSuggestions(suggestionId)
}