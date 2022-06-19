package com.example.caloriestracker

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriestracker.adapters.DayAdapter
import com.example.caloriestracker.adapters.IngredientAdapter
import java.time.LocalDate

class DaysActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_days)

        setupRecyclerViewData()
    }

    // Zwraca listę dni przechowywanych w bazie danych
    private fun getDaysList(): ArrayList<ItemModelDay> {
        val databaseManager: DatabaseManager = DatabaseManager(this)
        return databaseManager.viewDays()
    }

    // Ustawia źródło danych i adapter dla RecyclerView
    private fun setupRecyclerViewData() {
        val tvNoRecords = findViewById<TextView>(R.id.tvNoRecords)
        if(getDaysList().size <=0) {
            tvNoRecords.visibility = android.view.View.VISIBLE
        } else {
            tvNoRecords.visibility = android.view.View.GONE
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            val adapter = DayAdapter(this, getDaysList())
            recyclerView.adapter = adapter
        }
    }

    // DEBUG MODE
    fun deleteDay(day: ItemModelDay) {
        val databaseManager: DatabaseManager = DatabaseManager(this)
        val status = databaseManager.deleteDay(
            ItemModelDay(
                day.id,
                "", 0
            )
        )
        if (status > -1) {
            Toast.makeText(
                applicationContext,
                "Usunięto dzień", Toast.LENGTH_SHORT
            ).show()

            setupRecyclerViewData()
        }
    }
}