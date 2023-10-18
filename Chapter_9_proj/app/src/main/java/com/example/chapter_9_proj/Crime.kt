package com.example.chapter_9_proj

import java.util.Date
import java.util.UUID

data class Crime(
    val id: UUID,
    val title : String,
    val date : Date,
    val isSolved : Boolean
)
