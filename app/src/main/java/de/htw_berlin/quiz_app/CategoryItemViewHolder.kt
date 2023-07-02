package de.htw_berlin.quiz_app

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//Takes the view item passed in the constructor and fetch view elements to prepare them to get the data
class CategoryItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    // Declaration of all views present in item_category
    val categoryIcon: ImageView = itemView.findViewById(R.id.categoryIcon)
    val categoryName: TextView = itemView.findViewById(R.id.categoryName)
}