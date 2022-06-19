package com.example.caloriestracker.adapters

import android.content.Context
import android.os.Build
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriestracker.DatabaseManager
import com.example.caloriestracker.DaysActivity
import com.example.caloriestracker.ItemModelDay
import com.example.caloriestracker.R

class DayAdapter (private val context: Context, private val list: List<ItemModelDay>) : RecyclerView.Adapter<DayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_view_layout_day,
                parent, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViewModel = list[position]

        holder.tvDate.text = itemViewModel.date
        holder.tvCalories.text = String.format("%d kcal", itemViewModel.calories)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvCalories: TextView = view.findViewById(R.id.tvCalories)
    }
}