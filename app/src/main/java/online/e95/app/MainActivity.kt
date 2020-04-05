package online.e95.app

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebSettings
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MyWebViewClient.WebViewClientListener {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        webView.settings.javaScriptEnabled = true
        webView.settings.allowContentAccess = true
        webView.settings.allowFileAccess = true
        webView.settings.allowUniversalAccessFromFileURLs = true
        webView.settings.allowFileAccessFromFileURLs = true
        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = true
        if (Build.VERSION.SDK_INT >= 21) {
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webView.webViewClient = MyWebViewClient(this, this)
        webView.loadUrl(getString(R.string.home_url))

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
            webView.reload()
        }

        query.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                webView.loadUrl(getUrl())
            }
            false
        }
        btnGo.setOnClickListener {
            webView.loadUrl(getUrl())
        }
    }

    private fun getUrl() : String {
        val encodedQuery = Uri.encode(query.text.toString())
        return "https://www.google.com/search?q=$encodedQuery"
    }

    override fun onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack()
        }
        else finish()
    }

    override fun onStartLoad() {
        progress.visibility = View.VISIBLE
    }

    override fun onLoaded() {
        progress.visibility = View.GONE
    }

    override fun onError() {
        progress.visibility = View.GONE
        webView.loadUrl(getString(R.string.error_url))
    }

}
