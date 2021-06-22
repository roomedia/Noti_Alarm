package com.roomedia.dawn_down_alarm.presentation

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.roomedia.dawn_down_alarm.domain.AppDatabase
import com.roomedia.dawn_down_alarm.util.AppOpenManager

class AlarmApplication : Application() {

    private val database by lazy { AppDatabase.getInstance() }
    val appDao by lazy { database.appDao() }
    val keywordDao by lazy { database.keywordDao() }

    override fun onCreate() {
        super.onCreate()
        _instance = this
        MobileAds.initialize(this) {}
        AppOpenManager(this)
    }

    companion object {
        private lateinit var _instance: AlarmApplication
        val instance get() = _instance
    }
}
