package com.example.newsappmvvm.data.model

import com.google.gson.annotations.SerializedName


data class News(val title:String, val description: String, val url:String, @SerializedName("publishedAt") val date: String)

data class Article(val source:Source, val author: String, val title: String, val description: String, val url: String, val urlToImage: String, @SerializedName("publishedAt") val publishedDate: String, val content: String)

data class NewsResponse(val status: String, val totalResults: Int, val articles: List<Article>)

data class Source(val id: String, val journal: String)

