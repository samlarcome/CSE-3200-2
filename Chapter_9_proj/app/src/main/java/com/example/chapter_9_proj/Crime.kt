package com.example.chapter_9_proj

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

// ENTITY indicates a table in the DB
@Entity
data class Crime(
    // Primary key is a column that holds a unique entry
    @PrimaryKey val id: UUID,
    val title : String,
    val date : Date,
    val isSolved : Boolean
)
