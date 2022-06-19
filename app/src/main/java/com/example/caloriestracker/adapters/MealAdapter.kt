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
import androidx.recyclerview.widget.RecyclerView.*
import com.example.caloriestracker.*

class MealAdapter (private val context: Context, private val list: List<ItemModelMeal>, val viewType: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_REGULAR = 1
        const val VIEW_TYPE_CLEAN = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == VIEW_TYPE_REGULAR) {
           val viewRegular = LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_view_layout_meal,
                    parent, false
                )
            return ViewHolder(viewRegular)
        } else {
            val viewClean = LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_view_layout_meal_clean,
                    parent, false
                )
            return ViewHolderClean(viewClean)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewModel = list[position]

        if(holder is ViewHolder){
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
        }

        if(holder is ViewHolderClean) {
            holder.tvName.text = itemViewModel.name
            holder.tvCalories.text = String.format("%d kcal/100g", itemViewModel.calories)
            holder.tvDescription.text = itemViewModel.description

            holder.llMain.setOnClickListener {
                if(context is CaloriesActivity) {
                    context.addCaloriesDialog(itemViewModel)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCalories: TextView = view.findViewById(R.id.tvCalories)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val ivModify: ImageView = view.findViewById(R.id.ivModify)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
        val llMain: LinearLayout = view.findViewById(R.id.llMain)
    }

    class ViewHolderClean(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCalories: TextView = view.findViewById(R.id.tvCalories)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val llMain: LinearLayout = view.findViewById(R.id.llMain)
    }
}