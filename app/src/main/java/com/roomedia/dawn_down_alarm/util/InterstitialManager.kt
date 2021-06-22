package com.roomedia.dawn_down_alarm.util

import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.roomedia.dawn_down_alarm.BuildConfig
import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.presentation.view.activity.AlarmActivity

class InterstitialManager(private val activity: AlarmActivity) {

    private var interstitialAd: InterstitialAd? = null
    private lateinit var loadCallback: InterstitialAdLoadCallback

    fun fetchAd() {
        loadCallback = object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
            }

            override fun onAdFailedToLoad(err: LoadAdError) {
                interstitialAd = null
            }
        }
        InterstitialAd.load(activity, activity.getString(AD_UNIT_ID), getAdRequest(), loadCallback)
    }

    private fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }

    private fun isAdAvailable(): Boolean {
        return interstitialAd != null
    }

    fun showAdIfAvailable() {
        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.")

            val fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdShowedFullScreenContent() {
                    isShowingAd = true
                }

                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    isShowingAd = false
                    fetchAd()
                }
            }

            interstitialAd!!.fullScreenContentCallback = fullScreenContentCallback
            interstitialAd!!.show(activity)
        } else {
            Log.d(LOG_TAG, "Can not show ad.")
            fetchAd()
        }
    }

    companion object {
        private const val LOG_TAG = "InterstitialManager"
        private val AD_UNIT_ID = if (BuildConfig.DEBUG) {
            R.string.debug_interstitial_unit_id
        } else {
            R.string.release_interstitial_unit_id
        }
        var isShowingAd = false
    }
}