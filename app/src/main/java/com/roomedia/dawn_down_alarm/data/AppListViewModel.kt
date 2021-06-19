package com.roomedia.dawn_down_alarm.data

import com.roomedia.dawn_down_alarm.domain.AppDao
import com.roomedia.dawn_down_alarm.domain.CommonDao
import com.roomedia.dawn_down_alarm.entity.App
import kotlinx.coroutines.launch

class AppListViewModel(_dao: CommonDao<*>) : CommonViewModel<App>() {

    override val dao: AppDao = _dao as AppDao
    val dataset get() = dao.getAppAndKeywords()

    fun insert(packageName: String) {
        viewModelScope.launch {
            dao.insert(packageName)
        }
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