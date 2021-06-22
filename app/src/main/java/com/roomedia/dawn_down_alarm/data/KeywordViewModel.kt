package com.roomedia.dawn_down_alarm.data

import com.roomedia.dawn_down_alarm.domain.CommonDao
import com.roomedia.dawn_down_alarm.domain.KeywordDao
import com.roomedia.dawn_down_alarm.entity.Keyword
import kotlinx.coroutines.launch

class KeywordViewModel(_dao: CommonDao<*>) : CommonViewModel<Keyword>() {

    override val dao: KeywordDao = _dao as KeywordDao

    fun replace(packageName: String, keywords: Set<Keyword>) {
        viewModelScope.launch {
            dao.replace(packageName, keywords)
        }
    }
}