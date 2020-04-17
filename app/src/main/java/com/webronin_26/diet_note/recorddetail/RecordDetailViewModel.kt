package com.webronin_26.diet_note.recorddetail

import android.text.Editable
import androidx.lifecycle.*
import com.webronin_26.diet_note.data.Record
import com.webronin_26.diet_note.data.RecordType
import com.webronin_26.diet_note.data.Result
import com.webronin_26.diet_note.data.source.RecordsRepository
import kotlinx.coroutines.launch

class RecordDetailViewModel: ViewModel(), LifecycleObserver {

    private var repository: RecordsRepository? = null

    private val _mealName = MutableLiveData<String>()

    private val _mealCalories =  MutableLiveData<Int>()

    private val _carbohydrateProcess = MutableLiveData<Int>()

    private val _proteinProcess = MutableLiveData<Int>()

    private val _fatProcess = MutableLiveData<Int>()

    private var date: String? = null

    fun setRespository(repository: RecordsRepository) { this.repository = repository }

    fun setDate(currentDate: String) { date = currentDate }

    fun checkCurrentInput(
        name: Editable? ,
        calories: Editable?,
        carbohydrate: Int?,
        protein: Int?,
        fat: Int?
    ): String? {

        if (name.isNullOrEmpty()) { return "請輸入餐點名稱" }
        if (name.toString().replace("\\s".toRegex(), "").length > 20 ) { return "名稱請在 20 字以下" }
        _mealName.value = name.replace("\\s".toRegex(), "")

        if (calories.isNullOrEmpty()) { return "請輸入餐點熱量" }
        try {
            calories.toString().toInt()
        } catch (e: Exception) {
            return "熱量請輸入「數字」類型"
        }

        if (calories.toString().toInt() > 100000 ) { return "餐點熱量請在 10 萬以下" }
        if (calories.toString().toInt() < 0 ) { return "餐點熱量不為負數" }
        _mealCalories.value = calories.toString().toInt()

        _carbohydrateProcess.value = carbohydrate
        _proteinProcess.value = protein
        _fatProcess.value = fat

        return null
    }

    fun saveRecord() {

        val record = Record().apply {
            name = _mealName.value
            calories = _mealCalories.value!!.toInt()
            if (_carbohydrateProcess.value != 0 ) { carbohydrate = _carbohydrateProcess.value!! }
            if (_proteinProcess.value != 0) { protein = _proteinProcess.value!! }
            if (_fatProcess.value != 0) { fat = _fatProcess.value!! }
            type = RecordType.PER
            time = date
        }

        viewModelScope.launch {
            repository?.saveRecord(record)
        }
    }

    fun saveTotalRecord() {

        viewModelScope.launch {
            repository?.getRecord(date!!).let { result ->
                if(result is Result.Success) {

                    val record = result.data
                    record.apply {
                        calories += _mealCalories.value!!
                        if (_carbohydrateProcess.value != 0) { carbohydrate += _carbohydrateProcess.value!! }
                        if (_proteinProcess.value != 0) { protein += _proteinProcess.value!! }
                        if (_fatProcess.value != 0) { fat += _fatProcess.value!! }
                    }

                    repository?.updateTotalRecord(record)

                } else {

                    val record = Record().apply {
                        calories = _mealCalories.value!!.toInt()
                        if (_carbohydrateProcess.value != 0 ) { carbohydrate = _carbohydrateProcess.value!! }
                        if (_proteinProcess.value != 0) { protein = _proteinProcess.value!! }
                        if (_fatProcess.value != 0) { fat = _fatProcess.value!! }
                        type = RecordType.DAY
                        time = date
                    }

                    repository?.saveRecord(record)
                }
            }
        }
    }
}