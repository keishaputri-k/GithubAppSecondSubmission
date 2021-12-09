package com.kei.githubappsecondsubmission.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kei.githubappsecondsubmission.databinding.UserItemBinding
import com.kei.githubappsecondsubmission.domain.data.model.UsersItem

class MainAdapter(private val listUser : List<UsersItem?>?) :RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var onClickCallback : OnItemClickCallback? = null

    fun setItemClickCallback (onItemClickCallback: OnItemClickCallback){
        this.onClickCallback = onItemClickCallback
    }



    inner class MainViewHolder(var itemUserBinding: UserItemBinding) : RecyclerView
    .ViewHolder(itemUserBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemUserBinding = UserItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return MainViewHolder(itemUserBinding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val users = listUser?.get(position)
        holder.itemUserBinding.apply {
            tvNameItem.text = users?.login
            Glide.with(holder.itemView.context).load(users?.avatarUrl).into(ciUserItem)
            holder.itemView.setOnClickListener{onClickCallback?.onItemClicked(users)}
        }

    }

    override fun getItemCount(): Int = listUser?.size ?: 0
}

interface OnItemClickCallback {
    fun onItemClicked(user: UsersItem?)

}