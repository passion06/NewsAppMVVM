package com.example.newsappmvvm.data.repository

import android.util.Log
import com.example.newsappmvvm.data.db.NewsDao
import com.example.newsappmvvm.data.db.SavedNews
import com.example.newsappmvvm.data.model.News
import com.example.newsappmvvm.data.model.NewsResponse
import com.example.newsappmvvm.data.network.NewsAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class NewsRepository @Inject constructor(
    private val api: NewsAPI,
    private val newsDao: NewsDao
) {
    suspend fun getNewsHeadlines(): Flow<List<News>> = flow {
        try {
            Log.d("RepositoryCheck", "Repo called")
            val response: NewsResponse = api.getNewsHeadlines("e04b55d1a62740b78e7e5dc103bcadca")
            val newsList = response.articles.map { it ->
                News(
                    title = it.title,
                    description = it.description,
                    url = it.url,
                    urlToImage = it.urlToImage,
                    date = it.publishedDate ?: "unknownDate",
                    content = it.content
                )
            }
            emit(newsList)
        } catch (e: Exception) {
            Log.e("RepositoryCheck", "API call failed", e)
            emit(emptyList())
        }
    }.catch {
        Log.e("RepositoryCheck", "Exception", it)
        emit(emptyList())
    }

    suspend fun getSavedNews(): List<SavedNews> {
        // Retrieve saved news articles from local database
        val newsList: List<SavedNews> = newsDao.getAllArticles()
        return newsList
    }

    suspend fun saveNewsArticle(article: SavedNews) {
        // Save the news article to local database
        newsDao.insertNewsArticle(article)
    }

    suspend fun deleteNewsArticle(url: String) {
        // Delete the news article from local database
        newsDao.deleteNewsArticle(url)
    }

}