@file:Suppress("DEPRECATION")

package otang.app.news.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.CookieSyncManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.internal.EdgeToEdgeUtils
import otang.app.news.databinding.ReadActivityBinding
import java.io.IOException
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.CookieStore
import java.net.URI

class ReadActivity : AppCompatActivity() {
    private lateinit var binding: ReadActivityBinding
    private lateinit var url: String

    @Suppress("DEPRECATION")
    @SuppressLint("RestrictedApi", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ReadActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
        url = intent.getStringExtra("url").toString()
        uri = Uri.parse(url)
        CookieSyncManager.createInstance(this)
        // unrelated, just make sure cookies are generally allowed
        android.webkit.CookieManager.getInstance().setAcceptCookie(true)
        // magic starts here
        val coreCookieManager = WebkitCookieManagerProxy(
            CookiePolicy.ACCEPT_ALL
        )
        CookieHandler.setDefault(coreCookieManager)
        //
        binding.web.settings.loadsImagesAutomatically = true
        binding.web.settings.javaScriptEnabled = true
        // Enable responsive layout
        binding.web.settings.useWideViewPort = true
        // Zoom out if the content width is greater than the width of the viewport
        binding.web.settings.loadWithOverviewMode = true
        binding.web.settings.setSupportZoom(true)
        binding.web.settings.builtInZoomControls = true // allow pinch to zooom
        binding.web.settings.displayZoomControls = false
        binding.web.webViewClient = WebClient()
        binding.web.loadUrl(url)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        // Check if the key event was the Back button and if there's history
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.web.canGoBack()) {
            binding.web.goBack()
            return true
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event)
    }

    private inner class WebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val uri = request.url
            return handleUri(uri)
        }

        private fun handleUri(uri: Uri): Boolean {
            val mainHost = Companion.uri!!.host
            val host = uri.host
            uri.scheme
            // Based on some condition you need to determine if you are going to load the url
            // in your web view itself or in a browser.
            // You can use `host` or `scheme` or any part of the `uri` to decide.
            return if (host == mainHost) {
                // Returning false means that you are going to load this url in the webView itself
                false
            } else {
                // Returning true means that you need to handle what to do with the url
                // e.g. open web page in a Browser
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
                true
            }
        }
    }

    // http://stackoverflow.com/questions/18057624/two-way-sync-for-cookies-between-httpurlconnection-java-net-cookiemanager-and
    inner class WebkitCookieManagerProxy @JvmOverloads constructor(
        cookiePolicy: CookiePolicy? = null
    ) : CookieManager(null, cookiePolicy) {
        private val webkitCookieManager: android.webkit.CookieManager = android.webkit.CookieManager.getInstance()

        @Throws(IOException::class)
        override fun put(uri: URI, responseHeaders: Map<String, List<String>>) {
            // make sure our args are valid
            // save our url once
            val url = uri.toString()
            // go over the headers
            for (headerKey in responseHeaders.keys) {
                // ignore headers which aren't cookie related
                if (!(headerKey.equals(
                    "Set-Cookie2",
                    ignoreCase = true
                ) || headerKey.equals("Set-Cookie", ignoreCase = true))
                ) continue
                // process each of the headers
                for (headerValue in responseHeaders[headerKey]!!) {
                    webkitCookieManager.setCookie(url, headerValue)
                }
            }
        }

        @Throws(IOException::class)
        override fun get(
            uri: URI,
            requestHeaders: Map<String, List<String>>
        ): Map<String, List<String>> {
            // make sure our args are valid
            require(!(false)) { "Argument is null" }
            // save our url once
            val url = uri.toString()
            // prepare our response
            val res: MutableMap<String, List<String>> = HashMap()
            // get the cookie
            val cookie = webkitCookieManager.getCookie(url)
            // return it
            if (cookie != null) res["Cookie"] = listOf(cookie)
            return res
        }

        operator fun get(url: String?, cookieName: String): String? {
            val cookieValues = webkitCookieManager.getCookie(url)
            if (cookieValues != null) {
                val cookies = cookieValues.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                // Put all Cookies in a HashMap with cookieKey & cookieToken
                val cookieMap = HashMap<String, String>()
                for (cookie in cookies) {
                    val cs = cookie.split("=".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    cookieMap[cs[0].trim { it <= ' ' }] = cs[1].trim { it <= ' ' }
                }
                if (cookieMap.containsKey(cookieName)) {
                    return cookieMap[cookieName]
                }
            }
            return null
        }

        override fun getCookieStore(): CookieStore {
            // we don't want anyone to work with this cookie store directly
            throw UnsupportedOperationException()
        }
    }

    companion object {
        var uri: Uri? = null
    }
}