package br.com.zup.roomwordsample

import android.app.Application
import br.com.zup.roomwordsample.data.WordRoomDatabase
import br.com.zup.roomwordsample.domain.WordRepository

class WordApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { WordRoomDatabase.getDatabase(this) }
    val repository by lazy { WordRepository(database.wordDao()) }
}