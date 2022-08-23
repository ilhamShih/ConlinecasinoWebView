package com.appsplus.online.slot.core

import android.webkit.WebView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

abstract class Web(_webView: WebView, _mSwipeRefreshLayout: SwipeRefreshLayout) {

    protected var web_views: WebView = _webView

    protected var mSwipeRefreshLayout: SwipeRefreshLayout = _mSwipeRefreshLayout

    abstract fun options()

    abstract fun getViews(): WebView

    abstract fun gone()

    abstract fun visible()

    abstract fun getSwipeRefresh(): SwipeRefreshLayout

    abstract fun getConfigSwipe()
}
