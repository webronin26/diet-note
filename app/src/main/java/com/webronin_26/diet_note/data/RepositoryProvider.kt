package com.webronin_26.diet_note.data

import android.content.Context
import androidx.room.Room
import com.webronin_26.diet_note.data.source.DietNoteDatabase
import com.webronin_26.diet_note.data.source.RecordsRepository

object RepositoryProvider {

    private var database: DietNoteDatabase? = null

    @Volatile
    var recordsRepository: RecordsRepository? = null

    fun provideRecordsRepository(context: Context): RecordsRepository {
        synchronized(this) {
            return recordsRepository ?: recordsRepository ?: createRecordsRepository(context)
        }
    }

    private fun createRecordsRepository(context: Context): RecordsRepository {
        val database = database?: createDataBase(context)
        return RecordsRepository(database.dietNoteDao())
    }

    private fun createDataBase(context: Context): DietNoteDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            DietNoteDatabase::class.java , "records.db"
        ).build()
        database = result
        return result
    }
}