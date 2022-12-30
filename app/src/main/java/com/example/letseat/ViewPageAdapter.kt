package com.example.letseat


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.google.firebase.database.ValueEventListener
import java.util.*

class ViewPagerAdapter(val context: Context, private val mealList: List <Meal>) : PagerAdapter() {

    override fun getCount(): Int {


        return mealList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // inflate layout

      /*  val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater*/

        val view = LayoutInflater.from(context).inflate(R.layout.image_slider_item, container, false)




        //get data
        val meal = mealList[position]
        Log.v("!!!", "helo")
        Log.v("!!!", meal.mealName.toString())


        val title = meal.mealName
        val image = meal.image


        // on below line we are initializing
        // our image view with the id.
        val imageView: ImageView = view.findViewById<View>(R.id.MealImageView) as ImageView


      //  imageView.setImageResource(meal.image)



        // on below line we are setting
        // image resource for image view.
        //imageView.setImageResource(imageList.get(position))

        // on the below line we are adding this
        // item view to the container.
        Objects.requireNonNull(container).addView(view)

      /*  if(view.getParent() != null){
            ((ViewGroup)view.getParent()).removeView(view)
        }

        container.addView(view, position)*/

        // on below line we are simply
        // returning our item view.
        return view
    }

    // on below line we are creating a destroy item method.
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // on below line we are removing view
        container.removeView(`object` as View)
    }
}