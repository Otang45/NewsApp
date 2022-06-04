package otang.news.ui.activity;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebResourceRequest;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import otang.news.R;
import otang.news.databinding.ReadActivityBinding;
import otang.news.preference.WindowPreference;
import otang.news.util.AppUtils;

public class ReadActivity extends AppCompatActivity {

	private ReadActivityBinding binding;
	private String url;
	public static Uri uri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ReadActivityBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		new WindowPreference(this).applyEdgeToEdgePreference(getWindow(), getColor(R.color.colorSurface));
		AppUtils.addSystemWindowInsetToPadding(binding.getRoot(), false, true, false, true);
		url = getIntent().getStringExtra("url");
		uri = Uri.parse(url);
		android.webkit.CookieSyncManager.createInstance(this);
		// unrelated, just make sure cookies are generally allowed
		android.webkit.CookieManager.getInstance().setAcceptCookie(true);
		// magic starts here
		WebkitCookieManagerProxy coreCookieManager = new WebkitCookieManagerProxy(null,
				java.net.CookiePolicy.ACCEPT_ALL);
		java.net.CookieHandler.setDefault(coreCookieManager);
		//
		binding.web.getSettings().setLoadsImagesAutomatically(true);
		binding.web.getSettings().setJavaScriptEnabled(true);
		// Enable responsive layout
		binding.web.getSettings().setUseWideViewPort(true);
		// Zoom out if the content width is greater than the width of the viewport
		binding.web.getSettings().setLoadWithOverviewMode(true);
		binding.web.getSettings().setSupportZoom(true);
		binding.web.getSettings().setBuiltInZoomControls(true); // allow pinch to zooom
		binding.web.getSettings().setDisplayZoomControls(false);
		binding.web.setWebViewClient(new WebClient());
		binding.web.loadUrl(url);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Check if the key event was the Back button and if there's history
		if ((keyCode == KeyEvent.KEYCODE_BACK) && binding.web.canGoBack()) {
			binding.web.goBack();
			return true;
		}
		// If it wasn't the Back key or there's no web page history, bubble up to the default
		// system behavior (probably exit the activity)
		return super.onKeyDown(keyCode, event);
	}

	private class WebClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
			final Uri uri = request.getUrl();
			return handleUri(uri);
		}

		private boolean handleUri(final Uri uri) {
			final String mainHost = ReadActivity.uri.getHost();
			final String host = uri.getHost();
			final String scheme = uri.getScheme();
			// Based on some condition you need to determine if you are going to load the url 
			// in your web view itself or in a browser. 
			// You can use `host` or `scheme` or any part of the `uri` to decide.
			if (host.equals(mainHost)) {
				// Returning false means that you are going to load this url in the webView itself
				return false;
			} else {
				// Returning true means that you need to handle what to do with the url
				// e.g. open web page in a Browser
				final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				return true;
			}
		}
	}

	// http://stackoverflow.com/questions/18057624/two-way-sync-for-cookies-between-httpurlconnection-java-net-cookiemanager-and
	public class WebkitCookieManagerProxy extends CookieManager {
		private android.webkit.CookieManager webkitCookieManager;

		public WebkitCookieManagerProxy() {
			this(null, null);
		}

		public WebkitCookieManagerProxy(CookieStore store, CookiePolicy cookiePolicy) {
			super(null, cookiePolicy);
			this.webkitCookieManager = android.webkit.CookieManager.getInstance();
		}

		@Override
		public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
			// make sure our args are valid
			if ((uri == null) || (responseHeaders == null))
				return;
			// save our url once
			String url = uri.toString();
			// go over the headers
			for (String headerKey : responseHeaders.keySet()) {
				// ignore headers which aren't cookie related
				if ((headerKey == null)
						|| !(headerKey.equalsIgnoreCase("Set-Cookie2") || headerKey.equalsIgnoreCase("Set-Cookie")))
					continue;
				// process each of the headers
				for (String headerValue : responseHeaders.get(headerKey)) {
					this.webkitCookieManager.setCookie(url, headerValue);
				}
			}
		}

		@Override
		public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
			// make sure our args are valid
			if ((uri == null) || (requestHeaders == null))
				throw new IllegalArgumentException("Argument is null");
			// save our url once
			String url = uri.toString();
			// prepare our response
			Map<String, List<String>> res = new java.util.HashMap<String, List<String>>();
			// get the cookie
			String cookie = this.webkitCookieManager.getCookie(url);
			// return it
			if (cookie != null)
				res.put("Cookie", Arrays.asList(cookie));
			return res;
		}

		public String get(String url, String cookieName) {
			String cookieValues = this.webkitCookieManager.getCookie(url);
			if (cookieValues != null) {
				String[] cookies = cookieValues.split(";");
				// Put all Cookies in a HashMap with cookieKey & cookieToken
				HashMap<String, String> cookieMap = new HashMap<String, String>();
				for (String cookie : cookies) {
					String[] cs = cookie.split("=");
					cookieMap.put(cs[0].trim(), cs[1].trim());
				}
				if (cookieMap.containsKey(cookieName)) {
					String cookieValue = cookieMap.get(cookieName);
					return cookieValue;
				}
			}
			return null;
		}

		public void clearAllCookies() {
			this.webkitCookieManager.removeAllCookie();
		}

		@Override
		public CookieStore getCookieStore() {
			// we don't want anyone to work with this cookie store directly
			throw new UnsupportedOperationException();
		}
	}
}