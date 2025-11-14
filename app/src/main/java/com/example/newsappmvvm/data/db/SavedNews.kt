package com.example.newsappmvvm.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savedNews")
data class SavedNews(
    @PrimaryKey(true) val id:Int = 0,
    val title: String,
    val description: String? = null,
    val url: String,
    val urlToImage: String? = null
)
