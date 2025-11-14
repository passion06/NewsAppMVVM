package com.example.newsappmvvm.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NewsDao {
    @Insert
    suspend fun insertNewsArticle(savedNews: SavedNews)
    @Delete
    suspend fun deleteNewsArticle(savedNews: SavedNews)
    @Query("select * from savedNews")
    suspend fun getAllArticles(): List<SavedNews>
}