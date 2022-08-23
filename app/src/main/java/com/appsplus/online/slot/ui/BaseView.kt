package com.appsplus.online.slot.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.appsplus.online.slot.R
import com.appsplus.online.slot.config.*
import com.appsplus.online.slot.core.Option
import com.appsplus.online.slot.databinding.DefmainBinding


class BaseView : AppCompatActivity() {
    private lateinit var binding: DefmainBinding
    lateinit var option: Option
    var url = ""
    var cotrollOnStart = true
    var cotrollonReceivedError = false
    var resultLauncherPermission: ActivityResultLauncher<String>? = null
    var activityResultLaunchs: ActivityResultLauncher<Intent>? = null
    var valueCallbackArray: ValueCallback<Array<Uri>>? = null
    private var timeMillis = 0L
    lateinit var dialogBox: DialogBox
    lateinit var preferences: Preferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DefmainBinding.inflate(layoutInflater)

        //разрешения
        registerPermLauncher()
        //регистрируем запрос открыть файлы
        ImageOpenResultLauncher()
        // хранилище
        preferences = Preferences(this)
        //диалог
        dialogBox = DialogBox()

        option = Option(binding.viewHost, binding.swipeRefreshLayout)
        // WebView settings
        option.options()
        option.getConfigSwipe()

        // URL Constructor
         url = URL_FIRST_PART
             .plus(URL_SECOND_PART)
             .plus(URL_APPS_FLEIER_PART.plus(preferences.appsFleier))
             .plus(URL_GOOGLE_ADSS_PART.plus(preferences.googleAdss))
             .plus(URL_YANDEX_DEV_ID.plus(preferences.getYandId))

        setContentView(binding.root)
        /**---------- Условие диалога*/
        if (preferences.messageHasBeenShown == 1 && !option.getViews().url.toString()
                .startsWith(URL_FIRST_PART)
        ) {
            dialogBox.dialogBox(
                getString(R.string.dialog_mess),
                "OK",
                this
            )
        }

    }
    /**  @param cotrollOnStart true пока в фокусе */
    override fun onStart() {
        if (cotrollOnStart) {
            option.getViews().loadUrl(url)

        }
        // WebView Client
        option.getViews().apply {
            webViewClient = setupWebViewClient()
            webChromeClient = setupChromeWebViewClient()

        }

        super.onStart()
    }
    /**  @param cotrollOnStart false если не в фокусе, блокируем onStart option.getViews().loadUrl(url)*/
    override fun onPause() {
        cotrollOnStart = false
        super.onPause()
    }


    private fun setupChromeWebViewClient(): WebChromeClient {

        return object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                valueCallbackArray = filePathCallback
                resultLauncherPermission?.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                return true

            }

        }
    }

    private fun setupWebViewClient(): WebViewClient {
        return object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url: String = request?.url.toString()
                return if (url.startsWith("market://")) {
                    view?.context!!.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    true
                } else {
                    startProgress(true)
                    false
                }

            }


            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                startProgress(true)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                option.gone()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                if (request!!.isForMainFrame) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (error?.errorCode == ERROR_HOST_LOOKUP || error?.errorCode == ERROR_CONNECT) {
                            if(!cotrollonReceivedError){
                                cotrollonReceivedError = true
                                view?.loadUrl(URL_ERROR)
                            }
                            startProgress(false)


                        }
                    }
                }

            }

        }
    }

    fun registerPermLauncher() {
        resultLauncherPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    when {
                        granted -> {
                            // true Permission
                            ImageOpen()
                        }
                        !shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                            // false Permission
                        }
                        else -> {

                            // false Permission
                        }
                    }
                }
            }
    }

    fun ImageOpenResultLauncher() {

        activityResultLaunchs =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // получаем  -1
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    var uriArray: Array<Uri>? = null
                    if (intent != null) {
                        val uriString = intent.dataString
                        uriArray = arrayOf(Uri.parse(uriString))
                        val data = intent.clipData
                        if (data != null) {
                            uriArray = Array(data.itemCount) { i ->
                                data.getItemAt(i).uri
                            }
                        }
                        if (uriString != null)
                            uriArray = arrayOf(Uri.parse(uriString))
                    } else {
                        //intent == null
                    }
                    valueCallbackArray!!.onReceiveValue(uriArray)
                    valueCallbackArray = null
                    startProgress(false)

                } else {
                    valueCallbackArray!!.onReceiveValue(arrayOf())
                    valueCallbackArray = null
                    startProgress(false)
                }
            }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && option.getViews().canGoBack()) {
            if (option.getViews().canGoBack()) {
                option.getViews().goBack()
                cotrollonReceivedError = false
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {

        if (!option.getViews().canGoBack()) {
            if (System.currentTimeMillis() - timeMillis > 2000) {
                Toast.makeText(this, getString(R.string.exit_toast), Toast.LENGTH_SHORT).show()
                timeMillis = System.currentTimeMillis()
            } else {
                finish()
            }
        }
    }

    fun ImageOpen() {
        startProgress(true)
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        i.type = "image/*"
        i.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        activityResultLaunchs?.launch(Intent.createChooser(i, "Image Chooser"))
    }


    fun startProgress(con: Boolean) {
        if (con) {
            if (!option.getSwipeRefresh().isRefreshing) {
                option.visible()
            }
        } else {
            if (option.getSwipeRefresh().isRefreshing) {
                option.gone()
            }

        }
    }


}
