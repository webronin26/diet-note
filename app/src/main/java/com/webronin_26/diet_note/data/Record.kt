package com.webronin_26.diet_note.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "records")
data class Record constructor(
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString() ,
    @ColumnInfo(name = "name") var name: String? = null,
    @ColumnInfo(name = "time") var time: String? = null,
    @ColumnInfo(name = "calories") var calories: Int = 0,
    @ColumnInfo(name = "protein") var protein: Int = 0,
    @ColumnInfo(name = "carbohydrate") var carbohydrate: Int = 0,
    @ColumnInfo(name = "fat") var fat: Int = 0,
    @ColumnInfo(name = "type") var type: RecordType? = null
)

enum class RecordType {
    // 代表是一天的總和
    DAY,
    // 代表是一筆的資料
    PER
}