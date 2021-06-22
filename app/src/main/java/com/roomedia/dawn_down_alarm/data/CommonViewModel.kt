@file:Suppress("UNCHECKED_CAST")

package com.roomedia.dawn_down_alarm.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.roomedia.dawn_down_alarm.domain.AppDao
import com.roomedia.dawn_down_alarm.domain.CommonDao
import com.roomedia.dawn_down_alarm.domain.KeywordDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

abstract class CommonViewModel<T> : ViewModel() {

    abstract val dao : CommonDao<T>
    protected val viewModelScope = CoroutineScope(Dispatchers.Default + Job())

    fun insert(entity: T) {
        viewModelScope.launch {
            dao.insert(entity)
        }
    }

    fun update(entity: T) {
        viewModelScope.launch {
            dao.update(entity)
        }
    }

    fun delete(entities: Collection<T>) {
        viewModelScope.launch {
            dao.delete(entities)
        }
    }
}

class CommonViewModelFactory(private val dao: CommonDao<*>) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (dao) {
            is KeywordDao -> KeywordViewModel(dao)
            is AppDao -> AppListViewModel(dao)
            else -> throw IllegalArgumentException("no viewModel for dao: ${dao::class.simpleName}")
        } as T
    }
}