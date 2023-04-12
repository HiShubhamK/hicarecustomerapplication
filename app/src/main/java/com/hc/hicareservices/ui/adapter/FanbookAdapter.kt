package com.hc.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hc.hicareservices.databinding.LayoutFanbookAdapterBinding

class FanbookAdapter : RecyclerView.Adapter<FanbookAdapter.MainViewHolder>() {

//    var recipies = mutableListOf<Recipes>()
//    fun setRecipeList(movies: List<Recipes>?) {
//        if (movies != null) {
//            this.recipies = movies.toMutableList()
//        }
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutFanbookAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
//        val recipe = recipies[position]
//        holder.binding.tvTitle.text = recipe.title
//        holder.binding.tvPublisher.text = recipe.publisher
    }

    override fun getItemCount(): Int {
        return 3
    }


    class MainViewHolder(val binding: LayoutFanbookAdapterBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
