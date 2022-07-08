package com.applications.cleaner

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.webkit.*
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity


class PayemntWebview : AppCompatActivity() {
    private var order_id: String? = ""
    private var wb_donate: WebView? = null
    private var lastUrl: String? = ""
    private var rl_fetching_location: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payemnt_webview)
        order_id = intent.getStringExtra("order_id")
        wb_donate = findViewById(R.id.wb_donate)
        rl_fetching_location = findViewById(R.id.rl_fetching_location)
        findViewById<View>(R.id.iv_back).setOnClickListener { onBackPressed() }
        rl_fetching_location!!.setVisibility(View.VISIBLE)
        wb_donate!!.getSettings().javaScriptEnabled = true
        /*wb_donate!!.getSettings().loadWithOverviewMode = true
        wb_donate!!.getSettings().allowUniversalAccessFromFileURLs = true
        wb_donate!!.getSettings().useWideViewPort = true
        wb_donate!!.getSettings().domStorageEnabled = true
        wb_donate!!.setWebViewClient(WebViewClient())
      //  wb_donate!!.setWebChromeClient(WebChromeClient())
        wb_donate!!.getSettings().pluginState = WebSettings.PluginState.ON_DEMAND
        wb_donate!!.clearCache(true)
        wb_donate!!.clearHistory()
        wb_donate!!.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        //  wb_donate!!.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
         // wb_donate!!.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
        wb_donate!!.getSettings().builtInZoomControls = true
        wb_donate!!.getSettings()
           .setUserAgentString("Mozilla/5.0 (Linux; CPU iPhone OS 9_3 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13E233 Safari/601.1")
       */ wb_donate!!.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
                lastUrl = url;
                if (url.equals(
                        "https://website.thebespokecleanercrm.com/",
                        ignoreCase = true
                    )
                ) {
                    setResult(RESULT_OK)
                    finish()
                }
                return shouldOverrideUrlLoading(url)
            }

            override fun onPageFinished(view: WebView, url: String) {
                lastUrl = url;
                if (url.equals(
                        "https://website.thebespokecleanercrm.com/",
                        ignoreCase = true
                    )
                ) {
                    finish()
                }
                rl_fetching_location!!.setVisibility(View.GONE)
               // Handler().postDelayed({ rl_fetching_location!!.setVisibility(View.GONE) }, 2000)
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                rl_fetching_location!!.setVisibility(View.VISIBLE)
            }

            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(
                webView: WebView,
                request: WebResourceRequest
            ): Boolean {
                val uri = request.url
                lastUrl = uri.toString();
                rl_fetching_location!!.setVisibility(View.VISIBLE)
                if (uri.toString().equals(
                        "https://website.thebespokecleanercrm.com/",
                        ignoreCase = true
                    )
                ) {
                    setResult(RESULT_OK)
                    finish()
                }
                return shouldOverrideUrlLoading(uri.toString())
            }

            private fun shouldOverrideUrlLoading(overloadUrl: String): Boolean {
                wb_donate!!.loadUrl(overloadUrl)
                return true // Returning True means that application wants to leave the current WebView and handle the url itself, otherwise return false.
            }
        })
        wb_donate!!.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }
        })
        wb_donate!!.loadUrl("https://website.thebespokecleanercrm.com/thank-payment?booking-number=" + order_id)
    }

    override fun onBackPressed() {
        if (lastUrl!!.startsWith("https://website.thebespokecleanercrm.com/thank-you?booking-number") || lastUrl.equals(
                "https://website.thebespokecleanercrm.com/"
            )
        ) {
            setResult(RESULT_OK)
            finish()
        } else
            super.onBackPressed()
    }
}