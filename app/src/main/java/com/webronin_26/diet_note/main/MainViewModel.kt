package com.webronin_26.diet_note.main

import androidx.lifecycle.*
import com.webronin_26.diet_note.data.Record
import com.webronin_26.diet_note.data.Result
import com.webronin_26.diet_note.data.source.RecordsRepository
import com.webronin_26.diet_note.util.DateFormater
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel: ViewModel(), LifecycleObserver {

    private var repository: RecordsRepository? = null

    private var refreshJob: Job? = null

    // 總熱量
    private val _totalCalories = MutableLiveData<Int>()
    val totalCalories: LiveData<Int> = _totalCalories

    // 碳水化合物
    private val _carbohydrate = MutableLiveData<Int>()
    val carbohydrate: LiveData<Int> = _carbohydrate

    val carbohydratePercent: LiveData<Int> = carbohydrate.switchMap { value ->
        liveData {
            if (totalCalories.value != 0 ){
                when(value) {
                    0  -> emit(0)
                    else -> emit((value * 100).div(totalCalories.value!!))
                }
            } else {
                emit(0)
            }
        }
    }

    // 蛋白質
    private val _protein = MutableLiveData<Int>()
    val protein: LiveData<Int> = _protein

    val proteinPercent: LiveData<Int> = protein.switchMap { value ->
        liveData {
            if (totalCalories.value != 0 ){
                when(value) {
                    0  -> emit(0)
                    else -> emit((value * 100).div(totalCalories.value!!))
                }
            } else {
                emit(0)
            }
        }
    }

    // 油脂
    private val _fat = MutableLiveData<Int>()
    val fat: LiveData<Int> = _fat

    val fatPercent: LiveData<Int> = fat.switchMap { value ->
        liveData {
            if (totalCalories.value != 0 ){
                when(value) {
                    0  -> emit(0)
                    else -> emit((value * 100).div(totalCalories.value!!))
                }
            } else {
                emit(0)
            }
        }
    }

    val maxCalories =  MutableLiveData<Int>()

    init { maxCalories.value = 3000 }
    fun setMaxCalories( value: Int ) { maxCalories.value = value }

    fun setRepository(repository: RecordsRepository) { this.repository = repository }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun refresh() {
        loadData(DateFormater().getCurrentDate())
    }

    private fun loadData(currentTime: String) {
        refreshJob = viewModelScope.launch {
            repository?.getRecord(currentTime).let { result ->
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
        _totalCalories.value = record.calories
        _carbohydrate.value = record.carbohydrate
        _protein.value = record.protein
        _fat.value = record.fat
    }

    private fun setNullUI() {
        _totalCalories.value = 0
        _carbohydrate.value = 0
        _protein.value = 0
        _fat.value = 0
    }
}