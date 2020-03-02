package com.example.sonarexperiment.adapter

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sonarexperiment.R
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.common.LifecycleState
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.facebook.react.shell.MainReactPackage
import java.util.*


open class RecyclerViewAdapter(val list: ArrayList<String>, val activity: Activity) : RecyclerView.Adapter<RecyclerViewHolder>() {

    companion object {
        const val NATIE_VIEW = 1
        const val REACT_VIEW = 2
    }

    var currentIndex = 1
    var reactInstanceManager: ReactInstanceManager? = null

    fun createOrGetReactInstanceManager() {
        if(reactInstanceManager == null) {
            val builder = ReactInstanceManager
                .builder()
                .setApplication(activity.application)
                .addPackage(MainReactPackage())
                .setUseDeveloperSupport(true)
//            .setNativeModuleCallExceptionHandler(reactNativeExceptionHandler)
                .setInitialLifecycleState(LifecycleState.BEFORE_RESUME)
                .setJSBundleFile("assets://index.android.bundle")
                .setJSMainModulePath("index.android")
                .setCurrentActivity(activity)
            reactInstanceManager = builder.build()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        var view : View
        view = if(viewType == NATIE_VIEW){
            val inflater = LayoutInflater.from(parent.context)
            inflater.inflate(R.layout.list_item, parent, false)
        } else {

            Log.d("React", "Creating React View Now")
            createOrGetReactInstanceManager()
            val rootView = ReactRootView(activity)
            reactInstanceManager?.attachRootView(rootView)
            val bundle = Bundle()

            bundle.putInt("viewIndex", currentIndex)
            rootView.startReactApplication(reactInstanceManager, "MyView", bundle)
            rootView.tag = currentIndex
            currentIndex++

            rootView
        }

        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 4 == 0) {
            REACT_VIEW
        } else NATIE_VIEW
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bindData(list[position], getItemViewType(position))
    }

}

class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindData(data: String, itemViewType: Int) {
        if(itemViewType == RecyclerViewAdapter.NATIE_VIEW){
            bindNativeData(itemView, data)
        }else{
            bindReactData(itemView, data)
        }
    }

    private fun bindReactData(itemView: View, data: String) {
        val rootView = itemView as? ReactRootView
        val viewIndex = rootView?.tag as? Int

        var params = WritableNativeArray()
        params.pushInt(viewIndex!!)
        params.pushString(data)

        rootView?.reactInstanceManager?.currentReactContext?.getJSModule(RCTDeviceEventEmitter::class.java)?.emit("setData", params)
    }

    private fun bindNativeData(itemView: View, data: String) {
        val text = itemView.findViewById<TextView>(R.id.text)
        text.text = "Item Value: $data"
    }

}