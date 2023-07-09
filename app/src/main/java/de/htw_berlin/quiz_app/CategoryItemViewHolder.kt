package de.htw_berlin.quiz_app

import androidx.recyclerview.widget.RecyclerView
import de.htw_berlin.quiz_app.databinding.ItemCategoryBinding

//A ViewHolder that takes the binding object in the constructor & pass its root to the ViewHolder of the RecyclerView class
class CategoryItemViewHolder (private val binding:ItemCategoryBinding):
    RecyclerView.ViewHolder(binding.root){

    //binds the category object to the category variable in the item_category layout
    fun bind(category: Category){
        binding.category=category
        binding.executePendingBindings()
    }
}