package com.bbb.koha.app

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bbb.koha.common.SharedPreference
import com.bbb.koha.module.dashboard.DashboardActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseFragment: Fragment(), View.OnClickListener {
    protected lateinit var sharedPreference: SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        initializeView()
    }
    protected open fun initializeView(){}

    override fun onClick(p0: View?) {
    }

    protected open fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    /*protected fun hideKeyboard(context: Context, view:View){
        val imm = context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }*/
}