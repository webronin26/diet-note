package com.webronin_26.diet_note.data.source

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.webronin_26.diet_note.data.Record
import com.webronin_26.diet_note.data.RecordType
import com.webronin_26.diet_note.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecordsRepository(
    private val recordsDao: RecordsDao
) {

    fun observeRecord(date: String): LiveData<PagedList<Record>> {
        val liveRecords = recordsDao.getLiveRecordsByDateAndType(date, RecordType.PER)
        val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setPrefetchDistance(20)
            .build()
        return LivePagedListBuilder(liveRecords , config).build()
    }

    suspend fun getRecord(date: String): Result<Record> = withContext(Dispatchers.IO) {
        try {
            val record = recordsDao.getRecordByDateAndType(date,RecordType.DAY)
            if (record != null) {
                return@withContext Result.Success(record)
            } else {
                return@withContext Result.Error(Exception("not found"))
            }
        } catch ( e: Exception){
            return@withContext Result.Error(e)
        }
    }

    suspend fun saveRecord(record: Record) = withContext(Dispatchers.IO) {
        recordsDao.insertRecord(record)
    }

    suspend fun updateTotalRecord(record: Record) = withContext(Dispatchers.IO) {
        recordsDao.updateRecord(
            calories = record.calories,
            carbohydrate = record.carbohydrate,
            protein = record.protein,
            fat = record.fat ,
            id = record.id )
    }

    suspend fun deleteRecord(id: String) = withContext(Dispatchers.IO) {
        recordsDao.deleteRecordByID(id)
    }
}