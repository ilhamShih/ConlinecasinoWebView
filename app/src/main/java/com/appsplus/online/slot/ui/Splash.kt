package com.appsplus.online.slot.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.appsplus.online.slot.R
import com.appsplus.online.slot.config.Preferences
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splmain)
        var repeatableJob: Job? = null
        val preferences = Preferences(this)

        var value = 1
        /**--------- ждём AppMetricaDeviceIDListener эта либа отключёна
         * Оставлена для демонстрации */
        repeatableJob = lifecycleScope.launchWhenStarted {
            while (isActive) {
                delay(3_000)
                if (preferences.getYandId.isEmpty()) {
                    ++value
                    if (value > 5) {
                        nextActivity()
                        repeatableJob?.cancel()
                    }
                } else {
                    nextActivity()
                    repeatableJob?.cancel()
                }
            }
        }
        repeatableJob.start()

    }

    fun nextActivity() {
        Handler(mainLooper).postDelayed(
            { startActivity(Intent(this@Splash, BaseView::class.java))
                finish()
            }, 1000
        )
    }
}
