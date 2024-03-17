package com.bbb.koha.network

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bbb.koha.dashboard.MainViewModel
import com.bbb.koha.login.LoginViewModel
import com.bbb.koha.module.dashboard.DashboardViewModel
import com.bbb.koha.module.forgot.ForgotViewModel
import com.bbb.koha.module.my_account.change_password.ChangePasswordViewModel
import com.bbb.koha.module.my_account.charges.ChargesViewModel
import com.bbb.koha.module.my_account.personal_detail.PersonalDetailViewModel
import com.bbb.koha.module.my_account.purchase_suggestions.SuggestionViewModel
import com.bbb.koha.module.my_account.reading_history.ReadingHistoryViewModel
import com.bbb.koha.module.my_account.summary.SummaryDetailViewModel
import com.bbb.koha.module.registration.RegistrationViewModel
import com.bbb.koha.module.reset_password.ResetPasswordViewModel
import com.bbb.koha.module.set_new_password.SetNewPasswordViewModel
import com.bbb.koha.module.splash_screen.SplashViewModel

class ViewModelFactoryClass(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return RegistrationViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(ForgotViewModel::class.java)) {
            return ForgotViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(ResetPasswordViewModel::class.java)) {
            return ResetPasswordViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(SetNewPasswordViewModel::class.java)) {
            return SetNewPasswordViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(PersonalDetailViewModel::class.java)) {
            return PersonalDetailViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(SummaryDetailViewModel::class.java)) {
            return SummaryDetailViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(ReadingHistoryViewModel::class.java)) {
            return ReadingHistoryViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)) {
            return ChangePasswordViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(ChargesViewModel::class.java)) {
            return ChargesViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(SuggestionViewModel::class.java)) {
            return SuggestionViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}