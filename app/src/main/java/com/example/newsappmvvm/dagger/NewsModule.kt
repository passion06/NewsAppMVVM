package com.example.newsappmvvm.dagger

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.newsappmvvm.data.db.NewsDao
import com.example.newsappmvvm.data.db.NewsDatabase
import com.example.newsappmvvm.data.network.NewsAPI
import com.example.newsappmvvm.data.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NewsModule {

    @Provides
    @Singleton
    fun provideNewsDao(db: NewsDatabase) = db.newsDao()

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            "news_database"
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor { message ->
            Log.d("RetrofitLogger", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    @Provides
    @Singleton
    fun provideNewsAPI(retrofit: Retrofit):NewsAPI {
        return retrofit.create(NewsAPI::class.java)
    }
    @Provides
    @Singleton
    fun provideNewsRepository(api: NewsAPI, newsDao: NewsDao): NewsRepository {
        return NewsRepository(api, newsDao)
    }
}