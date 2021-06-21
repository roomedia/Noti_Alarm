@file:Suppress("SameParameterValue")

package com.roomedia.dawn_down_alarm.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.roomedia.dawn_down_alarm.BuildConfig
import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.presentation.AlarmApplication
import java.util.*

class AppOpenManager(private val application: AlarmApplication) : LifecycleObserver, Application.ActivityLifecycleCallbacks {

    private var appOpenAd: AppOpenAd? = null
    private lateinit var loadCallback: AppOpenAdLoadCallback
    private var currentActivity: Activity? = null
    private var loadTime = 0L

    init {
        application.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        showAdIfAvailable()
        Log.d(LOG_TAG, "onStart")
    }

    fun fetchAd() {
        if (isAdAvailable()) return
        loadCallback = object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                appOpenAd = ad
                loadTime = Date().time
            }

            override fun onAdFailedToLoad(err: LoadAdError) {
                super.onAdFailedToLoad(err)
            }
        }
        AppOpenAd.load(application, application.getString(AD_UNIT_ID),
            getAdRequest(), AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback)
    }

    private fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour = 3600000L
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    private fun isAdAvailable(): Boolean {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
    }

    private fun showAdIfAvailable() {
        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.")

            val fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdShowedFullScreenContent() {
                    isShowingAd = true
                }

                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    isShowingAd = false
                    fetchAd()
                }
            }

            appOpenAd!!.fullScreenContentCallback = fullScreenContentCallback
            appOpenAd!!.show(currentActivity!!)
        } else {
            Log.d(LOG_TAG, "Can not show ad.")
            fetchAd()
        }
    }

    companion object {
        private const val LOG_TAG = "AppOpenManager"
        private val AD_UNIT_ID = if (BuildConfig.DEBUG) {
            R.string.debug_app_open_unit_id
        } else {
            R.string.release_app_open_unit_id
        }
        var isShowingAd = false
    }
}