package com.sdkbox.gradle.sdkboxgradlepluginapp;

import android.util.Log;
import android.webkit.WebView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by hugo on 02/03/2017.
 */

public class NetBridge {

    static boolean enable = true;

    public static URLConnection URL_openConnection(URL url) throws IOException {
        Log.d("NetBridge", "URL_openConnection:" + url);
        if (!enable) {
            throw new IOException("Network is denied by NetBridge");
        }

        return url.openConnection();
    }

    public static void HttpURLConnect_connect(URLConnection n) throws IOException {
        Log.d("NetBridge", "HttpURLConnect_connect");
        if (!enable) {
            throw new IOException("Network is denied by NetBridge");
        }

        n.connect();
    }

    public static int HttpURLConnect_getResponseCode(HttpURLConnection n) throws IOException {
        Log.d("NetBridge", "HttpURLConnect_getResponseCode");
        if (!enable) {
            throw new IOException("Network is denied by NetBridge");
        }

        return n.getResponseCode();
    }

    public static int HttpURLConnect_getResponseCode(HttpsURLConnection n) throws IOException {
        Log.d("NetBridge", "HttpURLConnect_getResponseCode");
        if (!enable) {
            throw new IOException("Network is denied by NetBridge");
        }

        return n.getResponseCode();
    }

    public static void WebView_loadUrl(WebView targetInstance, String url) {
        Log.d("NetBridge", "HttpURLConnect_connect");
        if (!enable) {
            return;
        }
        targetInstance.loadUrl(url);
    }

    public static void WebView_loadUrl(WebView targetInstance, String url, Map<String, String> additionalHttpHeaders) {
        Log.d("NetBridge", "HttpURLConnect_connect");
        if (!enable) {
            return;
        }
        targetInstance.loadUrl(url, additionalHttpHeaders);
    }
}
