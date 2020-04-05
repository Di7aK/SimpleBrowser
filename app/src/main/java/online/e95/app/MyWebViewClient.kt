package online.e95.app

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.webkit.*
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.webkit.URLUtil.isNetworkUrl



class MyWebViewClient(private val context: Context, private val listener: WebViewClientListener) : WebViewClient() {

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        super.onReceivedError(view, request, error)

        Handler(Looper.getMainLooper()).post {
            listener.onError()
        }
    }

    override fun onPageFinished(view: WebView, url: String?) {
        super.onPageFinished(view, url)

        Handler(Looper.getMainLooper()).post {
            listener.onLoaded()
        }
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)

        Handler(Looper.getMainLooper()).post {
            listener.onStartLoad()
        }
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (isNetworkUrl(url)) {
            return false
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
        return true
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
        val url = request.url.toString()
        if (isNetworkUrl(url)) {
            return false
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
        return true
    }

    interface WebViewClientListener {
        fun onStartLoad()

        fun onLoaded()

        fun onError()
    }
}