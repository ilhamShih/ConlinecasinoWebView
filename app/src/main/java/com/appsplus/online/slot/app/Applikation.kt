package com.appsplus.online.slot.app

import android.app.Application
import android.os.Build
import android.webkit.WebView
import com.appsflyer.AppsFlyerLib
import com.appsplus.online.slot.config.ID_APPS_FLEIER
import com.appsplus.online.slot.config.Preferences
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.identifier.AdvertisingIdClient


class Applikation : Application() {
    lateinit var preferences: Preferences
    override fun onCreate() {
        super.onCreate()
        preferences = Preferences(this)
        /**-------------------- Это нужно было для YandexMetrica */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (packageName != getProcessName()) {
                WebView.setDataDirectorySuffix(getProcessName())
            }
        }
        /**--------------------YandexMetrica Отключёна */
/**     val config: YandexMetricaConfig = YandexMetricaConfig.newConfigBuilder(ID_YANDEX).build()
        YandexMetrica.activate(this, config)
        YandexMetrica.enableActivityAutoTracking(this)
        YandexMetricaPush.init(this)

        if (preferences.getYandId.isEmpty()) {
            YandexMetrica.requestAppMetricaDeviceID(deviceIDListener())
        }*/
        /**--------иним MobileAds */
        mobileAds()
        /**--------иним AppsFlyerLib */
        appsFlyerLib()

    }


    private fun mobileAds() {
        MobileAds.initialize(this)
        object : Thread() {
            override fun run() {
                try {
                    if (preferences.googleAdss.isBlank()) {
                        try {
                            val gAd = AdvertisingIdClient.getAdvertisingIdInfo(this@Applikation).id
                            preferences.setGoogleAdss(gAd.toString())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }.start()


    }

    private fun appsFlyerLib() {
        if (preferences.appsFleier.isBlank()) {
            AppsFlyerLib.getInstance().init(ID_APPS_FLEIER, null, this)
            AppsFlyerLib.getInstance().start(this)
            val pinAppsFlyer = AppsFlyerLib.getInstance().getAppsFlyerUID(this)
            preferences.setAppsFleier(pinAppsFlyer.toString())

        }
    }

    /**--------------------YandexMetrica Отключёна */
   /** fun deviceIDListener(): AppMetricaDeviceIDListener {
        return object : AppMetricaDeviceIDListener {
            override fun onLoaded(p0: String?) {
                preferences.setYandId(p0.toString())
            }

            override fun onError(p0: AppMetricaDeviceIDListener.Reason) {
                preferences.setYandId("")
            }

        }
    }*/

}