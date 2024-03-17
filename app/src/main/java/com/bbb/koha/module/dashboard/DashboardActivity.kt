package com.bbb.koha.module.dashboard

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bbb.koha.R
import com.bbb.koha.app.MVVMBindingActivity
import com.bbb.koha.common.Constant
import com.bbb.koha.databinding.ActivityDashboardBinding
import com.bbb.koha.login.LoginActivity
import com.bbb.koha.login.model.UserDetailResponseModel
import com.bbb.koha.module.about_app.AboutAppFragment
import com.bbb.koha.module.about_library.AboutLibraryFragment
import com.bbb.koha.module.about_library.LibraryContactInfoFragment
import com.bbb.koha.module.about_library.RulesAndRegulationFragment
import com.bbb.koha.module.dashboard.bottomsheet.AdvanceFilterFragment
import com.bbb.koha.module.dashboard.bottomsheet.FilterFragment
import com.bbb.koha.module.dashboard.bottomsheet.ScanByFragment
import com.bbb.koha.module.dashboard.fragment.HomeFragment
import com.bbb.koha.module.dashboard.fragment.SearchListFragment
import com.bbb.koha.module.my_account.change_password.ChangePasswordFragment
import com.bbb.koha.module.my_account.charges.ChargesFragment
import com.bbb.koha.module.my_account.discharge.DischargeFragment
import com.bbb.koha.module.my_account.my_qr_code.QRCodeFragment
import com.bbb.koha.module.my_account.personal_detail.PersonalDetailMainFragment
import com.bbb.koha.module.my_account.purchase_suggestions.PurchaseSuggestionListFragment
import com.bbb.koha.module.my_account.reading_history.ReadingHistoryFragment
import com.bbb.koha.module.my_account.summary.SummaryDetailFragment
import com.bbb.koha.module.setting.SettingFragment
import com.bbb.koha.utils.Utils
import com.bbb.koha.view.ConfirmationDialogFragment
import com.google.gson.Gson
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import java.util.Locale


class DashboardActivity : MVVMBindingActivity<ActivityDashboardBinding>() {
    private lateinit var userDetail: UserDetailResponseModel
    companion object {
        private const val REQUEST_CODE_STT = 1
    }
    private val textToSpeechEngine: TextToSpeech by lazy {
        TextToSpeech(this,
            TextToSpeech.OnInitListener { status ->
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeechEngine.language = Locale.UK
                }
            })
    }

    override fun initializeView() {
        onViewClick()
        viewHideGone()
        binding?.homeToolbarView?.binding?.tvTitle?.setText(sharedPreference.getValueString(Constant.LIBRARY_NAME))
        if(sharedPreference.getStartPage(Constant.START_PAGE)==Constant.StartScreen.LIBRARY_HOME) {
            val fragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit()
        }else{
            //binding?.bottomTab?bi.menu?.getItem(3)?.isChecked = true
            binding?.bottomTab?.binding?.bottomNavigationView?.menu?.getItem(3)?.isChecked = true
            val fragment = SummaryDetailFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit()
        }
        setupNavigation()

        if (sharedPreference.getValueInt(Constant.PATRON_ID)!=0)
            binding?.tvLogout?.text = resources.getString(R.string.logout)
        else binding?.tvLogout?.text = resources.getString(R.string.login)

        updateStatusBarColor("#000000")

    }

    override fun provideViewResource(): Int {
        return R.layout.activity_dashboard
    }

    fun updateStatusBarColor(color: String?) { // Color must be in hexadecimal fromat
        /*val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(color)*/

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.button_color)
    }

    override fun onClick(view: View?) {
        binding?.run {
            closeDrawer()
            when (view?.id) {
                tvHome.id -> {
                    binding?.bottomTab?.binding?.bottomNavigationView?.menu?.getItem(1)?.isChecked = true
                    val fm: FragmentManager = supportFragmentManager
                    for (i in 0 until fm.backStackEntryCount) {
                        fm.popBackStack()
                    }
                    replaceFragment(HomeFragment())
                }
                tvSummary.id -> loginUser(SummaryDetailFragment())
                tvPersonalDetails.id -> loginUser(PersonalDetailMainFragment())
                tvReadingHistory.id -> loginUser(ReadingHistoryFragment())
                tvCharge.id -> loginUser(ChargesFragment())
                tvChangePassword.id -> loginUser(ChangePasswordFragment())
                tvPurchaseSuggestions.id -> loginUser(PurchaseSuggestionListFragment())
                tvMyQRCode.id -> loginUser(QRCodeFragment())
                tvDischarge.id -> loginUser(DischargeFragment())
                tvAboutUs.id -> loginUser(AboutLibraryFragment())
                tvRulesRegulation.id -> loginUser(RulesAndRegulationFragment())
                tvContactUs.id -> loginUser(LibraryContactInfoFragment())
                consSearchCircle.id -> {
                    AdvanceFilterFragment().show(
                        supportFragmentManager,
                        "AdvanceFilterFragment"
                    )
                }
                consFolderSearch.id -> {
                    ScanByFragment().show(
                        supportFragmentManager,
                        "ScanByFragment"
                    )
                }
                consAboutApp.id -> replaceFragment(AboutAppFragment())
                consSetting.id -> replaceFragment(SettingFragment())
                homeToolbarView.binding.filter.id -> FilterFragment().show(
                    supportFragmentManager,
                    "FilterFragment"
                )
                tvLogout.id -> checkUserLogin()
                ivClose.id -> closeDrawer()
                toolbarView.binding.rlBack.id -> {
                    showToolbar(false)
                    onBackPressed()
                }

                homeToolbarView.binding.toolbarDrawer.id -> {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        closeDrawer()
                    } else {
                        openDrawer()
                    }
                }
                homeToolbarView.binding.ivVoice.id -> {
                    val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    sttIntent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                    sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")

                    try {
                        startActivityForResult(sttIntent, REQUEST_CODE_STT)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                        Toast.makeText(this@DashboardActivity, "Your device does not support STT.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_STT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    result?.let {
                        val recognizedText = it[0]
                        binding!!.homeToolbarView.binding.editTextSearch.setText(recognizedText)
                        if(recognizedText.isNotEmpty()) callSearchApi()
                    }
                }
            }
        }
    }

    private fun callSearchApi(){
        val searchText = binding!!.homeToolbarView.binding.editTextSearch.text.toString()
        val query = "{\"-or\":[{\"author\":{\"-like\":\"%$searchText%\"}},{\"title\":{\"-like\":\"%$searchText%\"}},{\"publisher\":{\"-like\":\"%$searchText%\"}},{\"isbn\":{\"-like\":\"%$searchText%\"}},{\"series_title\":{\"-like\":\"%$searchText%\"}}]}"
        val bundle = Bundle()
        bundle.putString(Constant.LIBRARY,"")
        bundle.putString(Constant.CATEGORY,"")
        bundle.putString(Constant.SEARCH_BY,"")
        bundle.putString(Constant.SEARCH,query)
        bundle.putInt(Constant.FRAGMENT_TYPE,Constant.FragmentType.SEARCH)
        val fragment = SearchListFragment.newInstance()
        fragment.arguments = bundle
        replaceFragment(fragment)
    }

    override fun onPause() {
        textToSpeechEngine.stop()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!sharedPreference.getValueBoolean(Constant.IS_LOGIN, false)) {
            val token = sharedPreference.getValueString(Constant.ACCESS_TOKEN)
            sharedPreference.clearSharedPreference()
            sharedPreference.save(Constant.ACCESS_TOKEN, token!!)
        }
        textToSpeechEngine.shutdown()

    }

    private fun checkUserLogin(){
        if (sharedPreference.getValueInt(Constant.PATRON_ID)!=0) {
            showConformationDialog()
        }else{
            startNewActivity(LoginActivity())
            finish()
        }
    }

    private fun loginUser(fragment:Fragment) {
        if (sharedPreference.getValueInt(Constant.PATRON_ID)!=0) {
            replaceFragment(fragment)
        }else{
            ConfirmationDialogFragment.newInstance(
                resources.getString(R.string.do_you_want_login),
                object : ConfirmationDialogFragment.ConfirmationDialogFragmentListener {
                    override fun onConfirmationCancelClick() {}
                    override fun onConfirmationOKClick() {
                        startNewActivity(LoginActivity())
                        finish()
                    }

                }
            ).show(supportFragmentManager, "logout")
        }
    }

    private fun showConformationDialog() {
        ConfirmationDialogFragment.newInstance(
            resources.getString(R.string.are_you_sure_you_want_to_logout),
            object : ConfirmationDialogFragment.ConfirmationDialogFragmentListener {

                override fun onConfirmationCancelClick() {

                }

                override fun onConfirmationOKClick() {
                    var token = sharedPreference.getValueString(Constant.ACCESS_TOKEN)
                    sharedPreference.clearSharedPreference()
                    sharedPreference.save(Constant.IS_LOGIN, false)
                    sharedPreference.save(Constant.ACCESS_TOKEN, token!!)
                    startNewActivity(LoginActivity())
                    finish()
                }

            }
        ).show(supportFragmentManager, "logout")
    }

    private fun setupNavigation() {
        var userDetailResponseModel = sharedPreference.getValueString(Constant.USER_DETAIL)
        if (!userDetailResponseModel.isNullOrEmpty()) {
            userDetail =
                Gson().fromJson(userDetailResponseModel, UserDetailResponseModel::class.java)
            binding?.run {
                profileName.text = "${userDetail.firstname} ${userDetail.surname}"
                tvUserName.text = "${userDetail.userid}"
                tvInitials.text =
                    Utils.getNameInitials("${userDetail.firstname} ${userDetail.surname}")
            }
        }
    }

    private fun viewHideGone() {
        binding?.run {
            lnrHome.setOnClickListener {
                if (consMyAccount.visibility == View.VISIBLE) {
                    consMyAccount.visibility = View.GONE
                    lnrHome.setBackgroundResource(R.color.white)
                    tvMyAccount.setTextColor(resources.getColor(R.color.text_color_secondary))
                } else {
                    tvMyAccount.setTextColor(resources.getColor(R.color.white))
                    consMyAccount.visibility = View.VISIBLE
                    lnrHome.setBackgroundResource(R.color.btbBack)
                }
            }

            lnrLibrary.setOnClickListener {
                if (consAboutLibrary.visibility == View.VISIBLE) {
                    consAboutLibrary.visibility = View.GONE
                    lnrLibrary.setBackgroundResource(R.color.white)
                    tvAboutLibrary.setTextColor(resources.getColor(R.color.text_color_secondary))
                } else {
                    tvAboutLibrary.setTextColor(resources.getColor(R.color.white))
                    consAboutLibrary.visibility = View.VISIBLE
                    lnrLibrary.setBackgroundResource(R.color.btbBack)
                }
            }
        }
    }

    private fun onViewClick() {
        binding?.run {
            tvHome.setOnClickListener(this@DashboardActivity)
            tvSummary.setOnClickListener(this@DashboardActivity)
            tvPersonalDetails.setOnClickListener(this@DashboardActivity)
            tvReadingHistory.setOnClickListener(this@DashboardActivity)
            tvCharge.setOnClickListener(this@DashboardActivity)
            tvChangePassword.setOnClickListener(this@DashboardActivity)
            tvPurchaseSuggestions.setOnClickListener(this@DashboardActivity)
            tvMyQRCode.setOnClickListener(this@DashboardActivity)
            consAboutApp.setOnClickListener(this@DashboardActivity)
            consSetting.setOnClickListener(this@DashboardActivity)
            consFolderSearch.setOnClickListener(this@DashboardActivity)
            consSearchCircle.setOnClickListener(this@DashboardActivity)
            tvDischarge.setOnClickListener(this@DashboardActivity)
            tvAboutUs.setOnClickListener(this@DashboardActivity)
            tvRulesRegulation.setOnClickListener(this@DashboardActivity)
            tvContactUs.setOnClickListener(this@DashboardActivity)
            tvLogout.setOnClickListener(this@DashboardActivity)
            bottomTab.binding.fabSearch.setOnClickListener(this@DashboardActivity)
            toolbarView.binding.rlBack.setOnClickListener(this@DashboardActivity)
            homeToolbarView.binding.toolbarDrawer.setOnClickListener(this@DashboardActivity)
            homeToolbarView.binding.filter.setOnClickListener(this@DashboardActivity)
            homeToolbarView.binding.ivVoice.setOnClickListener(this@DashboardActivity)
            ivClose.setOnClickListener(this@DashboardActivity)


            consHome.setOnClickListener {
                consHome.setBackgroundResource(R.color.btbBack)
                tvHome.setTextColor(ContextCompat.getColor(this@DashboardActivity, R.color.white))
                ivHomee.setImageResource(R.drawable.baseline_home_24)

            }
            consNotification.setOnClickListener {
                consNotification.setBackgroundResource(R.color.btbBack)
                tvNotification.setTextColor(
                    ContextCompat.getColor(
                        this@DashboardActivity,
                        R.color.white
                    )
                )
                ivNotification.setImageResource(R.drawable.notification_white)

            }

            homeToolbarView.binding.editTextSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard(v)
                    val searchText = homeToolbarView.binding.editTextSearch.text.toString()
                    val query = "{\"-or\":[{\"author\":{\"-like\":\"%$searchText%\"}},{\"title\":{\"-like\":\"%$searchText%\"}},{\"publisher\":{\"-like\":\"%$searchText%\"}},{\"isbn\":{\"-like\":\"%$searchText%\"}},{\"series_title\":{\"-like\":\"%$searchText%\"}}]}"
                    val bundle = Bundle()
                    bundle.putString(Constant.LIBRARY,"")
                    bundle.putString(Constant.CATEGORY,"")
                    bundle.putString(Constant.SEARCH_BY,"")
                    bundle.putString(Constant.SEARCH,query)
                    bundle.putInt(Constant.FRAGMENT_TYPE,Constant.FragmentType.SEARCH)
                    val fragment = SearchListFragment.newInstance()
                    fragment.arguments = bundle
                    replaceFragment(fragment)
                    return@OnEditorActionListener true
                }
                false
            })

            /*consSearchCircle.setOnClickListener {
                consSearchCircle.setBackgroundResource(R.color.btbBack)
                tvSearchCircle.setTextColor(
                    ContextCompat.getColor(
                        this@DashboardActivity,
                        R.color.white
                    )
                )
                ivNotification.setImageResource(R.drawable.notification_white)

            }*/

           /* consFolderSearch.setOnClickListener {
                consFolderSearch.setBackgroundResource(R.color.btbBack)
                tvSearchBook.setTextColor(
                    ContextCompat.getColor(
                        this@DashboardActivity,
                        R.color.white
                    )
                )
                ivNotification.setImageResource(R.drawable.notification_white)

            }*/

            bottomTab.binding.bottomNavigationView.setOnNavigationItemSelectedListener {
                if (it.itemId == R.id.home) {
                    val fm: FragmentManager = supportFragmentManager
                    for (i in 0 until fm.backStackEntryCount) {
                        fm.popBackStack()
                    }
                    replaceFragment(HomeFragment())
                } else if (it.itemId == R.id.barCode) {
                    /*val options = ScanOptions()
                    options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES)
                    options.setPrompt("Scan a barcode")
                    options.setCameraId(0) // Use a specific camera of the device
                    options.setBeepEnabled(false)
                    options.setBarcodeImageEnabled(true)
                    barcodeLauncher.launch(options)*/
                    ScanByFragment().show(
                        supportFragmentManager,
                        "ScanByFragment"
                    )
                }else if(it.itemId == R.id.summary){
                    loginUser(SummaryDetailFragment())
                }else if(it.itemId == R.id.profile){
                    loginUser(PersonalDetailMainFragment())
                }
                true
            }
            bottomTab.binding.fabSearch.setOnClickListener {
                AdvanceFilterFragment().show(
                    supportFragmentManager,
                    "AdvanceFilterFragment"
                )
            }

        }

    }

    private val barcodeLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this@DashboardActivity, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(
                this@DashboardActivity,
                "Scanned: " + result.contents,
                Toast.LENGTH_LONG
            ).show()

            val bundle = Bundle()
            bundle.putString(Constant.LIBRARY, "")
            bundle.putString(Constant.CATEGORY, "")
            bundle.putString(Constant.SEARCH_BY, "external_id")
            bundle.putString(Constant.SEARCH, result.contents)
            bundle.putInt(Constant.FRAGMENT_TYPE, Constant.FragmentType.BARCODE_SCAN)
            val fragment = SearchListFragment.newInstance()
            fragment.arguments = bundle

            replaceFragment(fragment)
        }
    }

    fun showToolbar(visible: Boolean) {
        binding?.run {
            if (visible) {
                homeToolbarView.visibility = View.GONE
                toolbarView.visibility = View.VISIBLE
            } else {
                homeToolbarView.visibility = View.VISIBLE
                toolbarView.visibility = View.GONE
            }
        }
    }

    fun showReadingHistoryToolbar(visible: Boolean) {
        binding?.run {
            if (visible) {
                homeToolbarView.visibility = View.GONE
                toolbarView.visibility = View.GONE
            } else {
                homeToolbarView.visibility = View.VISIBLE
                toolbarView.visibility = View.GONE
            }
        }
    }

    fun showHomeToolbar() {
        binding?.run {
            homeToolbarView.visibility = View.VISIBLE
            toolbarView.visibility = View.GONE
        }
    }

    fun setAppTitle(title: String) {
        binding?.toolbarView?.binding?.tvTitle?.text = title
    }

    override fun onBackPressed() {
        if (binding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            showReadingHistoryToolbar(false)
            super.onBackPressed()
        }
    }

    private fun openDrawer() {
        binding?.drawerLayout?.openDrawer(GravityCompat.START)
    }

    private fun closeDrawer() {
        binding?.drawerLayout?.closeDrawer(GravityCompat.START)
    }


    fun popBack() {
        val fm: FragmentManager = supportFragmentManager
        fm.popBackStack()
    }
}