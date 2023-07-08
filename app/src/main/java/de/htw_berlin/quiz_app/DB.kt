package de.htw_berlin.quiz_app

import com.google.firebase.firestore.FirebaseFirestore

//This class handles communication with DB to fetch Category Collection
class DB {

    //A private attribute field with datatype(FirebaseFirestore) that saves our firestore instance
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    //A function to get the data from firestore, saves it in a list and pass it
    //using the onSuccess(list) function if failed then exception.
    //We will pass the two function that are gonna be called in the parameter when we call this fun
    fun getCategories(onSuccess: (List<Category>) -> Unit, onFailure: (Exception) -> Unit) {

        //Name of our Collection on firestore
        val categoriesCollection = firestore.collection("Categories")

        //Trying to get all the documents
        //If successful then we will play with the queryResult=collection
        categoriesCollection.get().addOnSuccessListener { queryResult ->
            val categoryList = mutableListOf<Category>()

            //foreach document in our collection
            for (document in queryResult.documents) {

                //Get the document id and the 2 fields from the document.
                val categoryId = document.id
                val categoryName = document.getString("Name") ?: ""
                val categoryIconUrl = document.getString("IconPath") ?: ""

                //Save the document data as an object of the data class "Category" see Category.kt
                categoryList.add(Category(categoryId, categoryName, categoryIconUrl))
            }
            onSuccess(categoryList)
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }
}
