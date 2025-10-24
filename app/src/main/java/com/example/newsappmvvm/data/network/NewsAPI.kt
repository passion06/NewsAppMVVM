package com.example.newsappmvvm.data.network

import com.example.newsappmvvm.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("everything?domains=wsj.com")
    suspend fun getNewsHeadlines(@Query("apiKey") apiKey:String): NewsResponse
}