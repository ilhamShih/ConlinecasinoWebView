package com.appsplus.online.slot.core

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebSettings.LOAD_DEFAULT
import android.webkit.WebView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class Option(_webView: WebView, _mSwipeRefreshLayout: SwipeRefreshLayout) :
    Web(_webView, _mSwipeRefreshLayout) {


    override fun gone() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    override fun visible() {
        mSwipeRefreshLayout.isRefreshing = true
    }

    @SuppressLint("SetJavaScriptEnabled", "WrongConstant")
    override fun options() {
        web_views.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
            useWideViewPort = true
            databaseEnabled = true
            allowFileAccess = true
            cacheMode = LOAD_DEFAULT
        }
        web_views.scrollBarStyle.apply {
            View.SCROLLBARS_INSIDE_OVERLAY
        }

    }

    override fun getViews(): WebView {
        return web_views
    }

    override fun getSwipeRefresh(): SwipeRefreshLayout {
        return mSwipeRefreshLayout
    }

    override fun getConfigSwipe() {
        mSwipeRefreshLayout.isEnabled = false
    }


}