package com.example.sonarexperiment

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class RestOverCallTimeInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val startNs = System.currentTimeMillis()
        val response: Response = chain.proceed(chain.request())
        val completionTime = System.currentTimeMillis() - startNs

        val networkCallTime = response.header("NETWORK_CALL_TIME")!!.toLongOrNull()
        val queuedTime = completionTime - networkCallTime!!
        APIClient.queuedTime += queuedTime
//        Log.d("NETWORK PERF ", "Network time: $networkCallTime TOTAL TIME $completionTime & QueueTime: ${ queuedTime }")
        return response
    }

}