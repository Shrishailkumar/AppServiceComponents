package com.android.appcomponents.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.appcomponents.R
import com.android.appcomponents.model.Item
import com.elasticsearch.ElasticSearchItemView
import kotlinx.android.synthetic.main.inflate_search_item.view.*
import org.json.JSONObject

class RepoItemAdapter(val jsonObj: JSONObject) :
    RecyclerView.Adapter<RepoItemAdapter.MyViewHolder>() {

    lateinit var repoListener: OnRepoClickListener
    var repos: MutableList<Item> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.inflate_search_item, parent, false)
        return MyViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    fun setClickListener(repoListener: OnRepoClickListener) {
        this.repoListener = repoListener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemInfo = repos[position]
//
//        Glide.with(holder.itemView)
//            .load(itemInfo.owner?.avatarUrl)
//            .centerCrop()
//            .placeholder(R.mipmap.ic_launcher_round)
//            .into(holder.itemView.ivOwner)
//
//        holder.itemView.tvRepoTitle.text = itemInfo.name
//        holder.itemView.tvRepoDesc.text = itemInfo.description
//        holder.itemView.tvRepoWatcher.text = itemInfo.watchers.toString()
//
////        holder.itemView.setOnClickListener {
////            repoListener.onRepoClick(itemInfo)
////        }

        var view: View = ElasticSearchItemView(jsonObj).addView(holder.itemView.context)
        holder.itemView.clHead.removeAllViews()
        holder.itemView.clHead.addView(view)

    }

    fun clear() {
        repos.clear()
        notifyDataSetChanged()
    }

    fun addList(repoList: MutableList<Item>) {
        repos.addAll(repoList)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}

interface OnRepoClickListener {
    fun onRepoClick(repo: Item)
}