package de.htw_berlin.quiz_app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

//This class adapt the data from firestore for each item to be displayed in the ViewHolder
class CategoryAdapter(private val itemClickListener: OnItemClickListener):RecyclerView.Adapter<CategoryItemViewHolder>() {

    //The categories data from firestore to be adapted with setter to update the data
    var data = listOf<Category>()
        set(value) {
            field = value
            notifyDataSetChanged() //Notify the adapter about any change
            //TODO:Change this method later to optimize code
        }

    //Returns how many data we have in our list
    override fun getItemCount(): Int {
        return data.size
    }

    //Binds the data from the data list to the corresponding views in the view holder.
    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {

        //Used to call the item before listing it on the screen
        val item=data[position]

        //Bind Firestore fetched data to be used in the ViewHolder and set them
        val iconResId = holder.itemView.context.resources.getIdentifier(item.iconPath.substringAfterLast("/") , "drawable", holder.itemView.context.packageName)
        holder.categoryIcon.setImageResource(iconResId)
        holder.categoryName.text = item.name

        /*When an item is clicked, it triggers the onItemClick method of the
        itemClickListener and passes the corresponding Category object.*/
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(item)
        }
    }

    /*Inflates the layout for each item(data) in the adapter,
    and creates a CategoryItemViewHolder by inflating the view*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        //Create the inflater from the context of it's parent
        val layoutInflater = LayoutInflater.from(parent.context)

        //Create the view by inflating the parent layout with the item_category layout
        val view = layoutInflater.inflate(R.layout.item_category, parent,false)

        //Return the ViewHolder Object created using the inflated view passed in the constructor
        return CategoryItemViewHolder(view)
    }

    interface OnItemClickListener {
        fun onItemClick(category: Category)
    }
}