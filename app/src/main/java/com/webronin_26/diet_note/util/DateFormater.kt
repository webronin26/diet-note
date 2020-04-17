package com.webronin_26.diet_note.util

import java.text.SimpleDateFormat
import java.util.*

class DateFormater {

    fun getCurrentDate(): String {
        return SimpleDateFormat("yyyyMMdd" , Locale.getDefault()).format(Date())
    }

    fun formate(date: Date): String {
        return SimpleDateFormat("yyyyMMdd" , Locale.getDefault()).format(date)
    }
}