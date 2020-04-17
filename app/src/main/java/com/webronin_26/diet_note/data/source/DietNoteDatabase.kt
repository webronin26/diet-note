package com.webronin_26.diet_note.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.webronin_26.diet_note.data.Record

@Database(entities = [Record::class], version = 1)
@TypeConverters(Converters::class)
abstract class DietNoteDatabase: RoomDatabase() {

    abstract fun dietNoteDao(): RecordsDao
}