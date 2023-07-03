package de.htw_berlin.quiz_app


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

//This is the class that handles the logic of our category selection activity layout
class CategorySelectionActivity:AppCompatActivity() {

    /*Fields to be implemented later in the class to hold the activity RecyclerView,
    the data adapter between the firestore and the ViewHolder, and the firestore db instance*/
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryDB:CategoryDB

    /*Runs when this activity is called and set the content of our activity layout*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_selection)

        //Initialize the view field with the RecyclerView layout instance
        // and the view's layout manager with LinearLayoutManager to be used in our logic
        recyclerView = findViewById(R.id.categoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Init the DB field with a CategoryDB object
        categoryDB=CategoryDB()

        /*Calling the getCategories() of the DB object to fetch the
        categories from our firestore instance*/
        categoryDB.getCategories(onSuccess = { categoryList ->
            // Update the adapter data with the categories from firestore
            categoryAdapter.data = categoryList
        },
            onFailure = { exception ->
                Log.println(Log.ERROR,"Read Categories",exception.toString())
            })

        //Initialize the adapter field with an adapter instance to be used in our logic.
        //By doing this the method in the adapter will be called.
        categoryAdapter = CategoryAdapter(object : CategoryAdapter.OnItemClickListener {
            override fun onItemClick(category: Category) {
                navigateToNextScreen(category)
            }
        })

        //setting the adapter to handle the RecyclerView content
        recyclerView.adapter = categoryAdapter

    }

    //A function to navigate to next Screen
    private fun navigateToNextScreen(category: Category) {
        /*TODO: Heltonn, wenn du NormalModus fertig hast, nutze das hier. Unkommentieren
            und statt NextActivity sollst du dein neues Activity aufrufen.*/
        //val intent = Intent(this, NextActivity::class.java)
        //intent.putExtra("category", category) /*(Key:"category", Value:ausgewählte category)*/
        //startActivity(intent) /*Startet nächstes Activity*/
    }
}
