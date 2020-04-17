package com.webronin_26.diet_note.records

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.webronin_26.diet_note.data.Record
import com.webronin_26.diet_note.data.Result
import com.webronin_26.diet_note.data.source.RecordsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class RecordsViewModel: ViewModel(), LifecycleObserver {

    private val _totalID = MutableLiveData<String>()

    private var repository: RecordsRepository? = null

    private var refreshJob: Job? = null

    private var _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    var records: LiveData<PagedList<Record>>? = null

    // 總熱量
    private val _totalCalories = MutableLiveData<Int>()
    val totalCalories: LiveData<Int> = _totalCalories

    // 碳水化合物
    private val _carbohydrate = MutableLiveData<Int>()
    val carbohydrate: LiveData<Int> = _carbohydrate

    // 蛋白質
    private val _protein = MutableLiveData<Int>()
    val protein: LiveData<Int> = _protein

    // 油脂
    private val _fat = MutableLiveData<Int>()
    val fat: LiveData<Int> = _fat

    val titleDate: LiveData<String> = date.switchMap { date ->
        liveData {
            when(date.length) {
                0 -> emit("")
                else -> try {
                    emit(_date.value!!.substring(0,4) + " / " + _date.value!!.substring(4,6) + " / " + _date.value!!.substring(6,8))
                } catch (e: Exception) {
                    emit("")
                }
            }
        }
    }

    fun setDate(dateString: String) { _date.value = dateString }

    fun setRespository(repository: RecordsRepository) {
        this.repository = repository
        records = repository.observeRecord(date.value!!)
    }

    fun deleteRecord(record: Record) = viewModelScope.launch {

        record.let {
            repository?.deleteRecord(it.id)

            val newTotalRecord = Record()
            newTotalRecord.id = _totalID.value!!
            newTotalRecord.calories = _totalCalories.value!!.minus(it.calories)
            if (_carbohydrate.value != 0 ) { newTotalRecord.carbohydrate = _carbohydrate.value!!.minus(it.carbohydrate) }
            if (_protein.value != 0 ) { newTotalRecord.protein = _protein.value!!.minus(it.protein) }
            if (_fat.value != 0 ) { newTotalRecord.fat = _fat.value!!.minus(it.fat) }

            repository?.updateTotalRecord(newTotalRecord)
        }

        refreshData()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun refreshData() {
        refreshJob = viewModelScope.launch {
            repository?.getRecord(date.value!!).let { result ->
                if(result is Result.Success) {
                    setUI(result.data)
                } else {
                    setNullUI()
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun stopRefreshData() {
        refreshJob?.cancel()
    }

    private fun setUI(record: Record) {
        if (_totalID.value != record.id) { _totalID.value = record.id }
        if (_totalCalories.value != record.calories) { _totalCalories.value = record.calories }
        if (_carbohydrate.value != record.carbohydrate) { _carbohydrate.value = record.carbohydrate }
        if (_protein.value != record.protein) { _protein.value = record.protein }
        if (_fat.value != record.fat) { _fat.value = record.fat }
    }

    private fun setNullUI() {
        _totalID.value = ""
        _totalCalories.value = 0
        _carbohydrate.value = 0
        _protein.value = 0
        _fat.value = 0
    }
}
