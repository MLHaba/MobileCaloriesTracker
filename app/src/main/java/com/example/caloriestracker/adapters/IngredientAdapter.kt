package com.example.caloriestracker.adapters

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriestracker.*

class IngredientAdapter (private val context: Context, private val list: List<ItemModelIngredient>, val viewType: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_REGULAR = 1
        const val VIEW_TYPE_RECIPE = 2
        const val VIEW_TYPE_CLEAN = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == VIEW_TYPE_REGULAR)
        {
            val viewRegular = LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_view_layout_ingredient,
                    parent, false)

            return ViewHolder(viewRegular)
        } else if(viewType == VIEW_TYPE_RECIPE){
            val viewRecipe = LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_view_layout_ingredient_recipe,
                    parent, false)

            return ViewHolderRecipe(viewRecipe)
        } else {
            val viewClean = LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_view_layout_ingredient_clean,
                    parent, false)

            return ViewHolderClean(viewClean)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val itemViewModel = list[position]
        if(holder is ViewHolder)
        {
            holder.tvName.text = itemViewModel.name
            holder.tvCalories.text = String.format(
                context.resources.getString(R.string.kcalPerGrams), itemViewModel.calories)

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

        if(holder is ViewHolderRecipe){
            holder.tvName.text = itemViewModel.name
            holder.tvCalories.text = String.format(
                context.resources.getString(R.string.kcal), itemViewModel.calories)

            holder.ivDelete.setOnClickListener{
                if(context is CreatingMealRecipe) {
                    context.deleteIngredientDialog(itemViewModel)
                }
            }
        }

        if(holder is ViewHolderClean){
            holder.tvName.text = itemViewModel.name
            holder.tvCalories.text = String.format(
                context.resources.getString(R.string.kcalPerGrams), itemViewModel.calories)

            holder.llMain.setOnClickListener {
                if(context is CreatingMealRecipe) {
                    context.addIngredientRecipe(itemViewModel)
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
        val ivModify: ImageView = view.findViewById(R.id.ivModify)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
    }

    class ViewHolderRecipe(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCalories: TextView = view.findViewById(R.id.tvCalories)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
    }

    class ViewHolderClean(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCalories: TextView = view.findViewById(R.id.tvCalories)
        val llMain: LinearLayout = view.findViewById(R.id.llMain)
    }
}