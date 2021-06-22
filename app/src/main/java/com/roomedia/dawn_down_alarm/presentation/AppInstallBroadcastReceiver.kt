package com.roomedia.dawn_down_alarm.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.roomedia.dawn_down_alarm.data.AppListViewModel
import java.lang.ref.WeakReference

class AppInstallBroadcastReceiver(private val viewModelRef: WeakReference<AppListViewModel>) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_PACKAGE_ADDED -> onAdded(intent)
            Intent.ACTION_PACKAGE_REMOVED -> onRemoved(intent)
            else -> throw IllegalArgumentException("Unknown Intent Action")
        }
    }

    private fun onAdded(intent: Intent) {
        intent.data?.schemeSpecificPart?.let { packageName ->
            viewModelRef.get()?.insert(packageName)
        }
    }

    private fun onRemoved(intent: Intent) {
        intent.data?.schemeSpecificPart?.let { packageName ->
            viewModelRef.get()?.delete(packageName)
        }
    }
}