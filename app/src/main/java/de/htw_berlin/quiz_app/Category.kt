package de.htw_berlin.quiz_app
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
//Data model that looks like the data structure of our Categories collection on firestore
@Parcelize
data class Category(val id:String,val name:String,val iconPath:String) : Parcelable
