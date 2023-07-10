package de.htw_berlin.quiz_app

import androidx.recyclerview.widget.RecyclerView
import de.htw_berlin.quiz_app.databinding.ItemCategoryBinding

//A ViewHolder that takes the binding object in the constructor & pass its root to the ViewHolder of the RecyclerView class
class CategoryItemViewHolder (private val binding:ItemCategoryBinding):
    RecyclerView.ViewHolder(binding.root){

    //Every icon name in the DB should be mapped like this
    val iconMapping = mapOf(
        "geography_category_pictogram" to R.drawable.geography_category_pictogram,
        "maths_category_pictogram" to R.drawable.maths_category_pictogram,
        "music_category_pictogram" to R.drawable.music_category_pictogram,
        "programming_category_pictogram" to R.drawable.programming_category_pictogram,
        "sport_category_pictogram" to R.drawable.sport_category_pictogram
    )

    //binds the category object to the category variable in the item_category layout
    fun bind(category: Category){
        binding.category=category
        bindIcon(category)
        binding.executePendingBindings()
    }
    fun bindIcon(category: Category) {
        val iconResourceId = iconMapping[category.iconPath] ?: R.drawable.default_category
        binding.categoryIcon.setImageResource(iconResourceId)
    }

}