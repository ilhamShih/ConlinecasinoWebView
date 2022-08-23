package com.appsplus.online.slot.core

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebSettings.LOAD_DEFAULT
import android.webkit.WebView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class Option(view: WebView, refreshLayout: SwipeRefreshLayout) :
    Web(view, refreshLayout) {


    override fun gone() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun visible() {
        swipeRefreshLayout.isRefreshing = true
    }

    @SuppressLint("SetJavaScriptEnabled", "WrongConstant")
    override fun options() {
        webView.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
            useWideViewPort = true
            databaseEnabled = true
            allowFileAccess = true
            cacheMode = LOAD_DEFAULT
        }
        webView.scrollBarStyle.apply {
            View.SCROLLBARS_INSIDE_OVERLAY
        }

    }

    override fun getViews(): WebView {
        return webView
    }

    override fun getSwipeRefresh(): SwipeRefreshLayout {
        return swipeRefreshLayout
    }

    override fun getConfigSwipe() {
        swipeRefreshLayout.isEnabled = false
    }


}