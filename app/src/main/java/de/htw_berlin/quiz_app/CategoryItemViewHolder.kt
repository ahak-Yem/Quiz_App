package de.htw_berlin.quiz_app

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//Set the activity layout elements to some variables
class CategoryItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    // Declaration of all views present in item_category
    val categoryIcon: ImageView = itemView.findViewById(R.id.categoryIcon)
    val categoryName: TextView = itemView.findViewById(R.id.categoryName)
}