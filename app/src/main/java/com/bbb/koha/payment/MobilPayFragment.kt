package com.bbb.koha.payment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.bbb.koha.R
import com.bbb.koha.app.BaseFragment
import com.bbb.koha.databinding.FragmentMobilPayBinding
import com.bbb.koha.module.dashboard.DashboardActivity


class MobilPayFragment(var url:String) : BaseFragment() {

    private lateinit var binding:FragmentMobilPayBinding
    private val finalUrl = "https://mobylpe.com/payBill/receipt.php?id=AxrvxW5Fo6091020&back=false"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_mobil_pay,
            container,
            false
        )
        binding.run {
            webView.webViewClient = WebViewClient()

            // this will load the url of the website


            // this will enable the javascript settings, it can also allow xss vulnerabilities
            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.settings.loadWithOverviewMode = true
            webView.settings.setAllowUniversalAccessFromFileURLs(true);

            // if you want to enable zoom feature
            webView.settings.setSupportZoom(true)

            webView.webViewClient = object :WebViewClient(){
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    Log.d("WebView", "your current url when webpage loading..$url")
                    return true
                }
                override fun onPageFinished(view: WebView?, url: String?) {
                    if(url?.contains("receipt.php")==true){
                        Log.d("WebView", "match $url")
                        //showConfirmation()
                    }
                }

                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler,
                    error: SslError?
                ) {
                    handler.proceed()
                    Log.d("WebView", "error $error")
                }
            }


            /*webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                    super.onPageStarted(view, url, favicon)
                    Log.d("WebView", "your current url when webpage loading..$url")
                }

                override fun onPageFinished(view: WebView, url: String) {
                    Log.d("WebView", "your current url when webpage loading.. finish$url")
                    if(finalUrl == url){
                        Log.d("WebView", "match $url")
                    }
                    super.onPageFinished(view, url)
                }

                override fun onLoadResource(view: WebView, url: String) {
                    // TODO Auto-generated method stub
                    super.onLoadResource(view, url)
                }

                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    println("when you click on any interlink on webview that time you got url :-$url")
                    return super.shouldOverrideUrlLoading(view, url)
                }
            }*/

            webView.loadUrl(url)
        }
        return binding.root
    }

    private fun showConfirmation() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Your payment is successful")
            .setCancelable(false)
            .setPositiveButton("done") { dialog, id ->
                (activity as DashboardActivity).onBackPressed()

            }
        val alert = builder.create()
        alert.show()
    }

}