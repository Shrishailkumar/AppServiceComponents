package com.android.appcomponents.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.appcomponents.R
import com.elasticsearch.ElasticSearchItemView
import org.json.JSONArray
import org.json.JSONObject

class ElasticSearchAdapter : RecyclerView.Adapter<ElasticSearchAdapter.MyViewHolder> {

    var jlist = JSONArray()
    var context: Context ?= null


    constructor( context: Context){
       this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.inflate_search_item, parent, false)
        return MyViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return jlist.length()
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
         var objeck = jlist.getJSONObject(position)
          holder.bind(objeck,context!!)
    }

    fun addList(list: JSONArray) {
        this.jlist = list
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var clHead : ConstraintLayout = itemView.findViewById(R.id.clHead)
        fun bind(value: JSONObject, mcontext: Context) {
            var view: View = ElasticSearchItemView(value!!).addView(mcontext)
            clHead.removeAllViews()
            clHead.addView(view)
        }
    }

}
