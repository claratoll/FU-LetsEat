package com.example.letseat

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RestaurantsRecyclerAdapter (val context : Context,val restaurants : List <Restaurant>) : RecyclerView.Adapter<RestaurantsRecyclerAdapter.ViewHolder>(){

    val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.restaurant_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val restaurant = restaurants[position]
        holder.restaurantNameView.text = restaurant.restaurantName.toString()
        holder.infoTextView.text = restaurant.address.toString()
        holder.resPoints.text = "The restaurant has ${restaurant?.points.toString()} points"

        holder.restaurantPosition = position


        //If user presses the restaurant name it will go to detailed information about the restaurant
        //If user presses the address it will go to the map

        holder.restaurantNameView.setOnClickListener{
            val intent = Intent(context, DisplayOneRestaurantActivity::class.java)
            Log.v("!!!" , "restaurantPosition ${restaurant.documentId}")
            intent.putExtra("documentID", restaurant.documentId)
            context.startActivity(intent)
        }

        holder.infoTextView.setOnClickListener{
            val intent = Intent(context, MapsActivity::class.java)
            Log.v("!!!" , "restaurantPosition ${restaurant.documentId}")
          //  intent.putExtra("documentID", restaurant.documentId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = restaurants.size

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val restaurantNameView = itemView.findViewById<TextView>(R.id.RestaurantNameView)
        val infoTextView = itemView.findViewById<TextView>(R.id.infoTextView)
        val resPoints = itemView.findViewById<TextView>(R.id.pointsDisplayTextView)
        var restaurantPosition = 0


    }

}
