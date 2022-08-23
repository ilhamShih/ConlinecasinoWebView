package com.appsplus.online.slot.core

import android.webkit.WebView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

abstract class Web(view: WebView, refreshLayout: SwipeRefreshLayout) {

    protected var webView: WebView = view

    protected var swipeRefreshLayout: SwipeRefreshLayout = refreshLayout

    abstract fun options()

    abstract fun getViews(): WebView

    abstract fun gone()

    abstract fun visible()

    abstract fun getSwipeRefresh(): SwipeRefreshLayout

    abstract fun getConfigSwipe()
}
