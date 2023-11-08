package database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.chapter_9_proj.Crime

// @Database indicate to room this is our database of version 1
// entities list of classes (tables) to represent in the database
// abstract ... cannot make an instance of CrimeDatabase directly
@Database (entities = [Crime::class], version=1)
// tell room which type converters to use (we use for DATE)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {
    // register the DAO... DAO is an interface and ROOM will handle generating the concrete version
    abstract fun crimeDao(): CrimeDao
}