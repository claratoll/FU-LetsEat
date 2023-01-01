package com.example.letseat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import android.net.Uri as Uri

class MealRecyclerAdapter (val context: Context, val meals : List <Meal>): RecyclerView.Adapter<MealRecyclerAdapter.ViewHolder>() {

    val layoutInflater = LayoutInflater.from(context)


    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.meal_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = meals[position]
        holder.mealNameView.text = meal.mealName.toString()

       // val mealImage = meal.image?.toUri()

  //      Log.v("!!!", "$mealImage")

                //    holder.imageMealView.setImageURI(null)


    //   holder.imageMealView.setImageURI(meal.image?.toUri())

        holder.mealPosition = position
    }

    override fun getItemCount() = meals.size

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val mealNameView = itemView.findViewById<TextView>(R.id.mealNameView)
        val imageMealView = itemView.findViewById<ImageView>(R.id.mealImageView)
        var mealPosition = 0

    }
}