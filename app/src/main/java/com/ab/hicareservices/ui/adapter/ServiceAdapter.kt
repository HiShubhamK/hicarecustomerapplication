package com.ab.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.databinding.LayoutServiceAdapterBinding


class ServiceAdapter : RecyclerView.Adapter<ServiceAdapter.MainViewHolder>() {

//    var recipies = mutableListOf<Recipes>()
//    fun setRecipeList(movies: List<Recipes>?) {
//        if (movies != null) {
//            this.recipies = movies.toMutableList()
//        }
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutServiceAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
//        val recipe = recipies[position]
//        holder.binding.tvTitle.text = recipe.title
//        holder.binding.tvPublisher.text = recipe.publisher
    }

    override fun getItemCount(): Int {
        return 5
    }


    class MainViewHolder(val binding: LayoutServiceAdapterBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
