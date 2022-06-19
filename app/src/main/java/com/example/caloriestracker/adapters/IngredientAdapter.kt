package com.example.caloriestracker.adapters

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriestracker.IngredientsActivity
import com.example.caloriestracker.ItemModelIngredient
import com.example.caloriestracker.R

class IngredientAdapter (private val context: Context, private val list: List<ItemModelIngredient>) : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_view_layout_ingredient,
            parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViewModel = list[position]
        holder.tvName.text = itemViewModel.name
        holder.tvCalories.text = String.format("%d kcal/100g", itemViewModel.calories)

        holder.ivModify.setOnClickListener{
            if(context is IngredientsActivity) {
                context.updateIngredientDialog(itemViewModel)
            }
        }

        holder.ivDelete.setOnClickListener{
            if(context is IngredientsActivity) {
                context.deleteIngredientDialog(itemViewModel)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCalories: TextView = view.findViewById(R.id.tvCalories)
        val ivModify: ImageView = view.findViewById(R.id.ivModify)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
    }
}