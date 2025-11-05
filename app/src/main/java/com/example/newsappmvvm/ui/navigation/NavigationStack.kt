package com.example.newsappmvvm.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.newsappmvvm.data.model.News
import com.example.newsappmvvm.ui.LoadNews
import com.example.newsappmvvm.ui.NewsItemDetail
import com.example.newsappmvvm.viewmodel.NewsViewModel
import com.google.gson.Gson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Composable
fun NewsNavGraph(viewModel: NewsViewModel){
    val navController = rememberNavController()
    NavHost(navController, startDestination = "news_list") {
        composable("news_list") {
            LoadNews(viewModel) { news ->
                val json = Uri.encode(Json.encodeToString(news))
                navController.navigate("news_detail/$json")
            }
        }

        // Detail screen
        composable(
            route = "news_detail/{news}",
            arguments = listOf(navArgument("news") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("news") ?: ""
            val news = Json.decodeFromString<News>(json)
            NewsItemDetail(viewModel, news)
        }
    }
}