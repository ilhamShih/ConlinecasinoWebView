package com.appsplus.online.slot.config

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.appsplus.online.slot.R

/**--- URL Builder ----*/

const val URL_FIRST_PART = "https://google.com"
const val URL_ERROR = "file:///android_asset/view-error.html"
const val URL_SECOND_PART = "/appsplus"
const val URL_APPS_FLEIER_PART = "?utm_id="
const val URL_GOOGLE_ADSS_PART = "&gaid="
const val URL_YANDEX_DEV_ID = "&_device_id="


private const val PREFERENCES = "preferences"
private const val KEY_GOOGLE_ADSS = "key_adss"
private const val KEY_YANDEX_ID = "key_yan_id"
private const val KEY_APPS_FLEIER = "key_apps_fleier"
private const val KEY_MESSAGE_HAS_BEEN_SHOWN = "key_message_has_been_shown"

const val UXCAM_APP_KEY = "Default"
const val UXCAM_APP_KEY_DEBUG = "Default"

const val ID_APPS_FLEIER = "Default"
const val ID_YANDEX = "Default"



/**---  SharedPreferences  ----*/

class Preferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    private val sharedPreferencesEditor: SharedPreferences.Editor = sharedPreferences.edit()

    /**--- AppsFlyerLib ----*/
    val appsFleier: String
        get() = sharedPreferences.getString(KEY_APPS_FLEIER, "") ?: ""

    fun setAppsFleier(appsFleier: String) {
        sharedPreferencesEditor
            .putString(KEY_APPS_FLEIER, appsFleier)
            .apply()
    }

    /**--- Google ADS ----*/
    val googleAdss: String
        get() = sharedPreferences.getString(KEY_GOOGLE_ADSS, "") ?: ""

    fun setGoogleAdss(googleAdss: String) {
        sharedPreferencesEditor
            .putString(KEY_GOOGLE_ADSS, googleAdss)
            .apply()
    }

    /**--- Yand Id ----*/
    val getYandId: String
        get() = sharedPreferences.getString(KEY_YANDEX_ID, "").toString()

    fun setYandId(yanId: String) {
        sharedPreferencesEditor
            .putString(KEY_YANDEX_ID, yanId)
            .apply()
    }

    /**--- MAIN Start ----*/
    val messageHasBeenShown: Int
        get() = sharedPreferences.getInt(KEY_MESSAGE_HAS_BEEN_SHOWN, 1)

    fun setMessageHasBeenShown(messageHasBeenShown: Int) {
        sharedPreferencesEditor
            .putInt(KEY_MESSAGE_HAS_BEEN_SHOWN, messageHasBeenShown)
            .apply()
    }


}
    class DialogBox {

        fun dialogBox(msg: String, okButton: String, activity: Context) {
            val preferences = Preferences(activity.applicationContext)
            val alertDialogBuilder = AlertDialog.Builder(activity)
            val title = TextView(activity)

            alertDialogBuilder.apply {
                setCancelable(false)
                setMessage(msg)
                if (okButton !== "") {
                    setPositiveButton(okButton) { arg0, arg1 ->
                        preferences.setMessageHasBeenShown(2)
                    }
                }
            }

            val alertDialog = alertDialogBuilder.create()
            title.apply {
                text = "Nachricht"
                setBackgroundResource(R.color.purple_500)
                setPadding(10, 10, 10, 10)
                gravity = Gravity.CENTER
                setTextColor(Color.BLACK)
                textSize = 20f
            }
            alertDialog.setCustomTitle(title)
            alertDialog.show()
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                setTextColor(Color.BLACK)
            }

        }
    }
