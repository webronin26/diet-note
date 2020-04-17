package com.webronin_26.diet_note

import android.app.Application
import com.webronin_26.diet_note.data.RepositoryProvider
import com.webronin_26.diet_note.data.source.RecordsRepository

class DietNoteApplication: Application() {

    val recordsRepository: RecordsRepository
        get() = RepositoryProvider.provideRecordsRepository(this)
}