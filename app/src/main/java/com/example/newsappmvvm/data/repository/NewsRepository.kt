package com.example.newsappmvvm.data.repository

import android.util.Log
import android.util.Log.e
import com.example.newsappmvvm.data.model.News
import com.example.newsappmvvm.data.model.NewsResponse
import com.example.newsappmvvm.data.network.NewsAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class NewsRepository @Inject constructor(private val api: NewsAPI) {
    suspend fun getNewsHeadlines(): Flow<List<News>> = flow {
        try {
            Log.d("RepositoryCheck", "Repo called")
            val response: NewsResponse = api.getNewsHeadlines("e04b55d1a62740b78e7e5dc103bcadca")
            val newsList = response.articles.map { it ->
                News(
                    title = it.title,
                    description = it.description,
                    url = it.url,
                    date = it.publishedDate
                )
            }
            emit(newsList)
        } catch(e:Exception){
            Log.e("RepositoryCheck", "API call failed", e)
            emit(emptyList())
        }
    }.catch {
        Log.e("RepositoryCheck", "Exception", it)
        emit(emptyList())
    }

}