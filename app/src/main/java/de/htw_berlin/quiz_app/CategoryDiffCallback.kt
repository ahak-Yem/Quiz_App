package de.htw_berlin.quiz_app

import androidx.recyclerview.widget.DiffUtil

//TODO:Use later to optimize code, follow pdf s.17
class CategoryDiffCallback:DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}