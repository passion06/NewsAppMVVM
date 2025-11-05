package com.example.newsappmvvm.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.newsappmvvm.R
import com.example.newsappmvvm.data.model.News
import com.example.newsappmvvm.ui.navigation.BottomTab
import com.example.newsappmvvm.view.ui.theme.NewsAppMVVMTheme
import com.example.newsappmvvm.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.newsappmvvm.ui.navigation.NewsNavGraph
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: NewsViewModel = hiltViewModel()
            NewsAppMVVMTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NewsNavGraph(viewModel)
                }
            }
        }
    }
}

@Composable
fun LoadNews(viewModel: NewsViewModel, onNavigateToNewsDetail: (News) -> Unit) {
    var selectedTab by remember { mutableStateOf<BottomTab>(BottomTab.TodayNews) }
    val tabs = listOf(BottomTab.TodayNews, BottomTab.SavedNews)
    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == BottomTab.TodayNews,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                painter = painterResource(tab.icon),
                                contentDescription = tab.title
                            )
                        },
                        label = { Text(tab.title) })
                }
            }
        }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                BottomTab.TodayNews -> TodayNews(viewModel, onNavigateToNewsDetail)
                BottomTab.SavedNews -> SavedNews()
            }
        }
    }
}

@Composable
fun AddToSaved() {
    Text("Saved this news")
}

@Composable
fun TodayNews(newsViewModel: NewsViewModel, onNavigateToNewsDetail: (News) -> Unit) {
    val newsArticles by newsViewModel.newsItems.observeAsState(emptyList())

    Text(
        "Today's News",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(all = 20.dp)
            .fillMaxWidth()
    )
    LazyColumn(modifier = Modifier.padding(start = 5.dp, top = 70.dp)) {
        items(items = newsArticles) { newsArticle ->
            NewsItem(newsArticle, onNavigateToNewsDetail)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsItem(newsArticle: News, onNavigateToNewsDetail: (News) -> Unit) {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp)
            .clickable {
                onNavigateToNewsDetail(newsArticle)
            }
    ) {
        Text(newsArticle.title, style = MaterialTheme.typography.titleMedium)
//        GlideImage(
//            model = { newsArticle.urlToImage },
//            contentDescription = newsArticle.title,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp),
//            contentScale = ContentScale.Crop
//        )
        AsyncImage(
            model = newsArticle.urlToImage,
            contentDescription = newsArticle.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            newsArticle.url,
            color = Color(0xFF1A73E8),
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { uriHandler.openUri(newsArticle.url) })
        Text(newsArticle.description?:"", style = MaterialTheme.typography.bodyMedium)
        Divider(thickness = 1.dp, modifier = Modifier.padding(top = 15.dp))
    }
}

@Composable
fun NewsItemDetail(newsViewModel: NewsViewModel, newsArticle: News) {
    val uriHandler = LocalUriHandler.current
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
                       },
        floatingActionButton =
            {
                FloatingActionButton(
                    onClick = {
                    newsViewModel.saveArticle()
                    coroutineScope.launch { snackBarHostState.showSnackbar("Article Saved") }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.heart),
                    contentDescription = "favorite",
                    modifier = Modifier.size(18.dp)
                )
            }
        }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            Text(
                newsArticle.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(30.dp))
            AsyncImage(
                model = newsArticle.urlToImage,
                contentDescription = newsArticle.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.padding(15.dp))
            Text(
                newsArticle.url,
                color = Color(0xFF1A73E8),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { uriHandler.openUri(newsArticle.url) })
            Spacer(modifier = Modifier.padding(15.dp))
            Text(
                newsArticle.description ?: "",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp
            )
            Text(newsArticle.content, style = MaterialTheme.typography.bodyMedium, fontSize = 15.sp)
        }
    }

}

@Composable
fun SavedNews() {
    Text(text = "Saved News")
}