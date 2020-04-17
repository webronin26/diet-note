package com.webronin_26.diet_note.data.source

import androidx.room.TypeConverter
import com.webronin_26.diet_note.data.RecordType

class Converters {

    @TypeConverter
    fun recordTypeToString(record: RecordType?): String? {
        return record?.name
    }

    @TypeConverter
    fun stringTorecordType(str: String?): RecordType? {
        return str?.let { RecordType.valueOf(str) }
    }
}