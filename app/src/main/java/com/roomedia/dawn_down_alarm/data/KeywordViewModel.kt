package com.roomedia.dawn_down_alarm.data

import androidx.lifecycle.*
import com.roomedia.dawn_down_alarm.domain.CommonDao
import com.roomedia.dawn_down_alarm.domain.KeywordDao
import com.roomedia.dawn_down_alarm.entity.Keyword

class KeywordViewModel(_dao: CommonDao<*>) : CommonViewModel<Keyword>() {

    override val dao: KeywordDao = _dao as KeywordDao

    fun get(packageName: String): LiveData<List<Keyword>> {
        return dao.get(packageName)
    }
}