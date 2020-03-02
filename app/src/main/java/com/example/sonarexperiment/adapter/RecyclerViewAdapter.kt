package com.example.sonarexperiment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sonarexperiment.R

open class RecyclerViewAdapter(val list: ArrayList<String>) : RecyclerView.Adapter<RecyclerViewHolder>() {

    companion object {
        const val NATIE_VIEW = 1
        const val REACT_VIEW = 2
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        var view : View
        view = if(viewType == NATIE_VIEW){
            val inflater = LayoutInflater.from(parent.context)
            inflater.inflate(R.layout.list_item, parent, false)
        }else{
            // TODO: add react view
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun bindNativeData(itemView: View, data: String) {
        val text = itemView.findViewById<TextView>(R.id.text)
        text.text = "Item Value: $data"
    }

}