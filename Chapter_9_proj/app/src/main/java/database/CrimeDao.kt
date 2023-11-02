package database

import androidx.room.Dao
import androidx.room.Query
import com.example.chapter_9_proj.Crime
import java.util.UUID

@Dao
interface CrimeDao {
    // cannot create an object of type CrimeDao -- interface

    @Query("SELECT * FROM crime")
    suspend fun getCrimes(): List<Crime>

    @Query("SELECT * FROM crime WHERE id = (:id)")
    suspend fun getCrime(id : UUID): Crime
}