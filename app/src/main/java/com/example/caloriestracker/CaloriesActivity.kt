package com.example.caloriestracker

import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriestracker.adapters.MealAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

class CaloriesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calories)

        setupRecyclerViewData()
    }

    // Zwraca listę posiłków przechowywanych w bazie danych
    private fun getMealsList(): ArrayList<ItemModelMeal> {
        val databaseManager: DatabaseManager = DatabaseManager(this)
        return databaseManager.viewMeals()
    }

    // Ustawia źródło danych i adapter dla RecyclerView
    private fun setupRecyclerViewData() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = MealAdapter(this, getMealsList())
        recyclerView.adapter = adapter
    }

    // Zwraca datę w postaci String zformatowaną zgodnie z DATE_PATTERN DatabaseManagera
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateString(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern(DatabaseManager.DATE_PATTERN)
        return date.format(formatter)
    }

    // Dodaje kalorie do dziennego bilansu z aktualną datą
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addCalories(meal: ItemModelMeal) {
        val databaseManager: DatabaseManager = DatabaseManager(this)
        val dateString = getDateString(LocalDate.now())
        databaseManager.updateDayOnDate(ItemModelDay(0, dateString, meal.calories))
        Toast.makeText(applicationContext,
            getString(R.string.addedCaloriesToast), Toast.LENGTH_SHORT).show()
        finish()
    }

    // Wyświetla dialog ilości spożytego posiłku
    // Pozwala na określenie ilości spożytego posiłku w gramach,
    // dodaje obliczoną ilość kalorii do bilansu
    @RequiresApi(Build.VERSION_CODES.O)
    fun addCaloriesDialog(meal: ItemModelMeal) {
        val addDialog = Dialog(this)
        addDialog.setCancelable(false)
        addDialog.setContentView(R.layout.dialog_add_calories)

        val etAmount = addDialog.findViewById<EditText>(R.id.etAmount)
        val tvAdd = addDialog.findViewById<TextView>(R.id.tvAdd)
        val tvCancel = addDialog.findViewById<TextView>(R.id.tvCancel)

        tvAdd.setOnClickListener(View.OnClickListener {

            val amount = etAmount.text.toString()
            val databaseManager: DatabaseManager = DatabaseManager(this)

            if(amount.isNotEmpty()) {
                val calculatedCalories = ceil(meal.calories.toDouble() * (amount.toDouble()/100))
                addCalories(ItemModelMeal(meal.id, meal.name, calculatedCalories.toInt(), meal.description))
            } else {
                Toast.makeText(applicationContext,
                    getString(R.string.insertDataToast), Toast.LENGTH_SHORT).show()
            }
        })

        tvCancel.setOnClickListener(View.OnClickListener {
            addDialog.dismiss()
        })

        addDialog.show()
    }
}