package com.example.caloriestracker.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriestracker.*

class MealAdapter (private val context: Context, private val list: List<ItemModelMeal>) : RecyclerView.Adapter<MealAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_view_layout_meal,
                    parent, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViewModel = list[position]
        holder.tvName.text = itemViewModel.name
        holder.tvCalories.text = String.format("%d kcal/100g", itemViewModel.calories)
        holder.tvDescription.text = itemViewModel.description

        holder.ivModify.setOnClickListener{
            if(context is MealsActivity) {
                context.updateMealDialog(itemViewModel)
            }
        }

        holder.ivDelete.setOnClickListener{
            if(context is MealsActivity) {
                context.deleteMealDialog(itemViewModel)
            }
        }

        holder.llMain.setOnClickListener {
            if(context is CaloriesActivity) {
                context.addCaloriesDialog(itemViewModel)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCalories: TextView = view.findViewById(R.id.tvCalories)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val ivModify: ImageView = view.findViewById(R.id.ivModify)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
        val llMain: LinearLayout = view.findViewById(R.id.llMain)
    }
}