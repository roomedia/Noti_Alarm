package com.roomedia.dawn_down_alarm.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.roomedia.dawn_down_alarm.model.AppDatabase
import com.roomedia.dawn_down_alarm.model.Keyword
import com.roomedia.dawn_down_alarm.model.KeywordDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class KeywordViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getAppDataBase(application.applicationContext)!!
    private val keywordDao: KeywordDao by lazy {
        db.keywordDao()
    }
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Default + viewModelJob)

    private val keywords: LiveData<List<Keyword>> = keywordDao.getAll()

    fun insert(keyword: Keyword) {
        viewModelScope.launch {
            keywordDao.insert(keyword)
        }
    }

    fun update(keyword: Keyword) {
        viewModelScope.launch {
            keywordDao.update(keyword)
        }
    }

    fun delete(keyword: Keyword) {
        viewModelScope.launch {
            keywordDao.delete(keyword)
        }
    }

    fun get(packageName: String): LiveData<List<Keyword>> {
        return keywordDao.get(packageName)
    }

    fun getAll(): LiveData<List<Keyword>> {
        return keywords
    }
}