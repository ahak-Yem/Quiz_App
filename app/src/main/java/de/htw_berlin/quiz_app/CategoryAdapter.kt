package de.htw_berlin.quiz_app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.htw_berlin.quiz_app.databinding.ItemCategoryBinding

//This class adapt the data from firestore for each item to be passed to the layout
class CategoryAdapter(private val itemClickListener: OnItemClickListener):
    RecyclerView.Adapter<CategoryItemViewHolder>() {

    //The categories data from firestore to be adapted with setter to update the data
    var categoryList = listOf<Category>()
        set(value) {
            field = value
            notifyDataSetChanged() //Notify the adapter about any change
        }

    //Inflates the layout to create a ViewHolder of the layout views using a binding object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)  //Define the layout to be inflated
        val binding=ItemCategoryBinding.inflate(inflater,parent,false)//Inflate the layout to create a binding object
        val categoryViewHolder=CategoryItemViewHolder(binding)//Create a ViewHolder with the binding object that provide access to the views in our layout
        return categoryViewHolder//returns the ViewHolder instance
    }

    //Binds the data from the category list to the corresponding views in the ViewHolder.
    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {

        holder.bind(categoryList[position])//calls the bind function of the CategoryViewHolder class
        // and passing the item that should be bind there
        /*When an item is clicked, it triggers the onItemClick method of the
        itemClickListener and passes the corresponding Category object.*/
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(categoryList[position])
        }
    }
    //Returns how many data we have in our list
    override fun getItemCount(): Int {
        return categoryList.size
    }

    interface OnItemClickListener {
        fun onItemClick(category: Category)
    }
}