package com.roomedia.dawn_down_alarm.data

import android.content.pm.ApplicationInfo
import android.content.pm.ApplicationInfo.FLAG_SYSTEM
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_META_DATA
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.roomedia.dawn_down_alarm.entity.App
import com.roomedia.dawn_down_alarm.presentation.AlarmApplication
import java.util.*

class AppListViewModel : ViewModel() {

    private val packageManager = AlarmApplication.instance.packageManager
    private val _dataset = packageManager.getInstalledApplications(GET_META_DATA)
        .filter { it.isInstalledByUser() }
        .map { it.toApp(packageManager) }
        .sortedBy { it.appName.toLowerCase(Locale.ROOT) }
        .toMutableList()
    val dataset = MutableLiveData<List<App>>(_dataset)

    fun insert(packageName: String) {
        val app = packageManager.getApplicationInfo(packageName, GET_META_DATA).run {
            if (!isInstalledByUser()) return
            toApp(packageManager)
        }
        _dataset.apply {
            var index = binarySearch(app)
            if (index < 0) index = -(index + 1)
            add(index, app)
        }
        dataset.value = _dataset.toList()
    }

    fun delete(packageName: String) {
        _dataset.removeAll { it.packageName.contentEquals(packageName) }
        dataset.value = _dataset.toList()
    }

    private fun ApplicationInfo.isInstalledByUser(): Boolean {
        return flags and FLAG_SYSTEM == 0
    }

    private fun ApplicationInfo.toApp(packageManager: PackageManager): App {
        return App(packageName, loadLabel(packageManager).toString(), loadIcon(packageManager))
    }
}