package database

import androidx.room.Dao
import androidx.room.Query
import com.example.chapter_9_proj.Crime
import kotlinx.coroutines.flow.Flow
import java.util.UUID

// DATA ACCESS OBJECT
// Allows us to access and edit data in the ROOM DB
@Dao
interface CrimeDao {
    // cannot create an object of type CrimeDao -- interface

    @Query("SELECT * FROM crime")
    // suspend fun getCrimes() : List<Crime>

    // Flow is itself a coroutine and is a suspending fxn, therefore remove suspend
    // Emit a flow of Crimes
    fun getCrimes(): Flow<List<Crime>>

    @Query("SELECT * FROM crime WHERE id = (:id)")
    suspend fun getCrime(id : UUID): Crime
}