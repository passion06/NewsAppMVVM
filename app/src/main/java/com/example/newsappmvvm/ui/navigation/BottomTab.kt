package com.example.newsappmvvm.ui.navigation

import com.example.newsappmvvm.R

sealed class BottomTab(val title: String, val icon: Int) {
    object TodayNews: BottomTab("Today's News", R.drawable.outline_newspaper_24)
    object SavedNews: BottomTab("Saved News", R.drawable.outline_bookmark_added_24)
}