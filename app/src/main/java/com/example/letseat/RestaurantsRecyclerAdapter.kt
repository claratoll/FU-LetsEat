package com.example.letseat

import android.content.Context
import android.content.Intent
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
        holder.restaurantNameView.text = restaurant.restaurantName
        holder.infoTextView.text = restaurant.points.toString()

        /*val student = students[position]

        holder.nameTextView.text = student.name
        holder.classTextView.text = student.className
        holder.presentButton.isChecked = student.present
        holder.studentPosition = position*/

    }

    override fun getItemCount() = restaurants.size




   /* fun removeStudent(position: Int){
        DataManager.students.removeAt(position)
        notifyDataSetChanged()
    }*/

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val restaurantNameView = itemView.findViewById<TextView>(R.id.RestaurantNameView)
        val infoTextView = itemView.findViewById<TextView>(R.id.infoTextView)
        var restaurantPosition = 0


      /*  init {
            itemView.setOnClickListener{
                val intent = Intent(context, CreateAndEditStudentActivity::class.java)
                intent.putExtra(STUDENT_POSITION_KEY, studentPosition)
                context.startActivity(intent)
            }

            presentButton.setOnClickListener {
                DataManager.students[studentPosition].present = presentButton.isChecked
            }

            deleteButton.setOnClickListener{
                removeStudent(studentPosition)
            }*/


    }




}
