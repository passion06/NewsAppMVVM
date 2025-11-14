package com.example.newsappmvvm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsappmvvm.data.db.SavedNews
import com.example.newsappmvvm.data.model.News
import com.example.newsappmvvm.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(var repository: NewsRepository): ViewModel() {
private val _newsItems = MutableLiveData<List<News>>()
    val newsItems: LiveData<List<News>> = _newsItems

    init {
       viewModelScope.launch {
           repository.getNewsHeadlines().collect { newsList->
               _newsItems.value = newsList
               Log.d("RetrofitURL", "Got ${newsList.size} articles")
           }

       }
    }
    fun saveArticle(news: News) {
        viewModelScope.launch {
            val savedNews = SavedNews(
                title = news.title,
                description = news.description,
                url = news.url,
                urlToImage = news.urlToImage
            )
            repository.saveNewsArticle(savedNews)
        }
    }

    fun fetchSavedArticles(): LiveData<List<SavedNews>> {
        val savedNewsLiveData = MutableLiveData<List<SavedNews>>()
        viewModelScope.launch {
            val savedArticles = repository.getSavedNews()
            savedNewsLiveData.postValue(savedArticles)
        }
        return savedNewsLiveData
    }

    fun removeSavedArticle(news: SavedNews) {
        viewModelScope.launch {
            repository.deleteNewsArticle(news.url)
        }
    }
}