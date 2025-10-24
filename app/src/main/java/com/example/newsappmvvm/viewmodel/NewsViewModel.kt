package com.example.newsappmvvm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    fun saveArticle() {
        viewModelScope.launch {
            //insert into DB
        }
    }
}