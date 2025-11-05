package com.example.newsappmvvm.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class News(val title:String, val description: String? = null , val url:String, val urlToImage:String?=null, @SerialName("publishedAt") val date: String?=null, val content:String)

data class Article(val source:Source, val author: String, val title: String, val description: String, val url: String, val urlToImage: String, @SerialName("publishedAt") val publishedDate: String?, val content: String)

data class NewsResponse(val status: String, val totalResults: Int, val articles: List<Article>)

data class Source(val id: String, val journal: String)

