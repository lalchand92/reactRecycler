package com.example.sonarexperiment

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class RestCallTimeInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val startNs = System.currentTimeMillis()
        val response: Response = chain.proceed(chain.request())
        val completionTime = System.currentTimeMillis() - startNs
        APIClient.totalTime += completionTime

        val builder = response.newBuilder()
        builder.addHeader("NETWORK_CALL_TIME", completionTime.toString())

//        Log.d("NETWORK PERF ", "TOTAL TIME $completionTime & totalTime: ${APIClient.totalTime}")
        return builder.build()
    }

}