package com.webronin_26.diet_note.data.source

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.webronin_26.diet_note.data.Record
import com.webronin_26.diet_note.data.RecordType

@Dao
interface RecordsDao {

    @Query("SELECT * FROM records WHERE time = :time AND type = :type")
    fun getLiveRecordsByDateAndType(time: String, type: RecordType): DataSource.Factory<Int, Record>

    @Query("SELECT * FROM records WHERE time = :time AND type = :type")
    suspend fun getRecordByDateAndType(time: String, type: RecordType): Record?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: Record)

    @Query("UPDATE records SET calories = :calories, protein = :protein , carbohydrate = :carbohydrate, fat = :fat WHERE id = :id")
    suspend fun updateRecord(calories: Int, protein: Int, carbohydrate: Int, fat: Int, id: String)

    @Query("DELETE FROM records WHERE id = :id")
    suspend fun deleteRecordByID(id: String)
}
