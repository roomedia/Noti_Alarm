package com.roomedia.dawn_down_alarm.data

import com.roomedia.dawn_down_alarm.domain.AppDao
import com.roomedia.dawn_down_alarm.domain.CommonDao
import com.roomedia.dawn_down_alarm.entity.App
import com.roomedia.dawn_down_alarm.presentation.AlarmApplication
import com.roomedia.dawn_down_alarm.util.toApp
import kotlinx.coroutines.launch

class AppListViewModel(_dao: CommonDao<*>) : CommonViewModel<App>() {

    override val dao: AppDao = _dao as AppDao
    val dataset get() = dao.getAllAppAndKeywords()

    fun insert(packageName: String) {
        val app = AlarmApplication.instance.packageManager
            .getApplicationInfo(packageName, 0).toApp()
        insert(app)
    }

    fun updateEnabled(app: App) {
        app.isEnabled = !app.isEnabled
        update(app)
    }

    fun delete(packageName: String) {
        viewModelScope.launch {
            dao.delete(packageName)
        }
    }
}