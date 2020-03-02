package com.example.sonarexperiment

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.TimeUnit

import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object APIClient {

    var retrofit: Retrofit? = null
    var totalTime = 0L
    var queuedTime = 0L
    private val mutex = Mutex()

    private fun getRetrofitClient(maxRequestsPerHost: Int): Retrofit {
        val dispatcher = Dispatcher()
        if (maxRequestsPerHost > 64) {
            dispatcher.maxRequests = maxRequestsPerHost
        }
        dispatcher.maxRequestsPerHost = maxRequestsPerHost

        val interceptor = RestCallTimeInterceptor()
        val overCallIntercaptor = RestOverCallTimeInterceptor()
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(overCallIntercaptor).addNetworkInterceptor(interceptor)
//            .addInterceptor(loggingInterceptor)
            .connectionPool(ConnectionPool(5, 60, TimeUnit.SECONDS))
            .dispatcher(dispatcher)
            .writeTimeout(45000, TimeUnit.MILLISECONDS)
            .readTimeout(45000, TimeUnit.MILLISECONDS)
            .connectTimeout(45000, TimeUnit.MILLISECONDS).build()


        return Retrofit.Builder()
            .baseUrl("https://reqres.in")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun getClient(maxRequestsPerHost: Int) = retrofit ?: runBlocking { mutex.withLock {
        retrofit ?: getRetrofitClient(maxRequestsPerHost).also { retrofit = it }
    } }
}