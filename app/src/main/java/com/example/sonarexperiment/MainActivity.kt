package com.example.sonarexperiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sonarexperiment.adapter.RecyclerViewAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {

    private val networkBenchmarkTestEnable = false
    var resultReceived = 0
    var startTime = 0L
    var maxRequestsPerHost = 0
    var workCompletionLiveData = MutableLiveData<Int>()

    private val EXPERIMENT_COUNT = 1000
    private val MAX_REQUEST_HOST_COUNT = 100
    private val HOST_INCREMENT_COUNT = 5


    val recyclerViewHybridTest = true
    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = "{\"service_code\":\"WDI\"}"
        val gson = Gson()
        val fromJson = gson.fromJson(data, XYZ::class.java)

        println("fromJson : ${fromJson.service_code}")

        if(networkBenchmarkTestEnable) {
            startWork(0)

            workCompletionLiveData.observeForever {
                startWork(it)
            }
        }

        if(recyclerViewHybridTest){
            recyclerViewSetUp()
        }
    }

    private fun recyclerViewSetUp() {
        recyclerView =  findViewById(R.id.recycler_view)
        recyclerView!!.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView!!.adapter = RecyclerViewAdapter(getRecyclerViewData())
    }

    private fun getRecyclerViewData() : ArrayList<String>{
        val data = ArrayList<String>()

        for (i in 1..1000){
            data.add("$i")
        }

        return data
    }

    private fun startWork(value: Int){
        if(maxRequestsPerHost == MAX_REQUEST_HOST_COUNT){
            return
        }

        val maxRequestsPerHost = value + HOST_INCREMENT_COUNT
        this.maxRequestsPerHost = maxRequestsPerHost
        Log.d("DEBUG LOG LOG ", "reset is kicked int $maxRequestsPerHost")
        this.startTime = 0L
        resultReceived = 0
        APIClient.totalTime = 0
        APIClient.queuedTime = 0
        APIClient.retrofit = null
        test(maxRequestsPerHost)
    }

    private fun test(maxRequestsPerHost: Int) {
        var init = false
        for (i in 1..EXPERIMENT_COUNT) {
            if(i > 1) {
                if(!init) {
//                    Log.d("DEBUG LOG LOG ","start time getting init for $maxRequestsPerHost")
                    startTime = System.currentTimeMillis()
                }
                init = true
            }
            makeApiCall(maxRequestsPerHost)
            runBlocking {
                if(i == 1) {
                    delay(2000)
                }
            }
        }
    }

    private fun makeApiCall(maxRequestsPerHost: Int) {
        val requestStartTime = System.currentTimeMillis()
        val apiInterface = APIClient.getClient(maxRequestsPerHost).create(APIInterface::class.java)

        val call = apiInterface.doGetListResources()

        call.enqueue(object: retrofit2.Callback<MultipleResource> {

            override fun onFailure(call: Call<MultipleResource>, t: Throwable) {
//                Log.d("TAG ", "onFailure ${t.message}")
                call.cancel()
                resultReceived++
            }

            override fun onResponse(
                call: Call<MultipleResource>,
                response: Response<MultipleResource>
            ) {
                resultReceived++
                if(resultReceived == EXPERIMENT_COUNT){
                    Log.d("NETWORK PERF ", "Network Time ${APIClient.totalTime} Queue Time: ${APIClient.queuedTime}  maxRequestsPerHost: $maxRequestsPerHost total timeTake EoE : ${System.currentTimeMillis() - startTime}")
                    workCompletionLiveData.postValue(maxRequestsPerHost)
                }

//                Log.d("NETWORK PERF ", "maxRequestsPerHost: $maxRequestsPerHost requestStartTime ${System.currentTimeMillis() - requestStartTime}")

            }
        })
    }
}


class XYZ {
    var service_code: String? = null
}
