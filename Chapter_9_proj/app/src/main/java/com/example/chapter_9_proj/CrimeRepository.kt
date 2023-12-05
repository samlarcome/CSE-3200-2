package com.example.chapter_9_proj

import android.content.Context
import androidx.room.Room
import database.CrimeDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

// reference to db
private const val DATABASE_NAME = "crime-database"

// encapsulates the logic for accessing data from a single source (DB) OR a set of sources
// determines how to fetch and store a particular set of data
// *** UI will request all the data from the repository
    // UI doesn't care how the data is actually stored or fetched
class CrimeRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {
    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).createFromAsset(DATABASE_NAME).build()
    // prepopulate data from our given database file

    // *** FLOW -- No More SUSPEND
    // Pass flow of Crimes along to VM
    fun getCrimes(): Flow<List<Crime>> = database.crimeDao().getCrimes()

    suspend fun getCrime(id: UUID): Crime = database.crimeDao().getCrime(id)

    fun updateCrime(crime: Crime) {
        coroutineScope.launch {
            database.crimeDao().updateCrime(crime)
        }
    }

    companion object{
        // CrimeRepository needs to be a SINGLETON
        private var INSTANCE: CrimeRepository? = null
        fun initialize(context: Context){
            // create instance first time, do nothing if try and do it again
            if (INSTANCE == null) { INSTANCE = CrimeRepository(context) }
        }

        fun get(): CrimeRepository{
            return INSTANCE ?: throw java.lang.IllegalMonitorStateException("CrimeRepository must be initialized")
        }
    }
}