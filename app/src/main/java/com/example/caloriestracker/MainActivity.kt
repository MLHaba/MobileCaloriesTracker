package com.example.caloriestracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ingredientsButton = findViewById<Button>(R.id.btnIngredients)
        ingredientsButton.setOnClickListener() {
            val intent = Intent(this, IngredientsActivity::class.java)
            startActivity(intent)
        }

        val mealsButton = findViewById<Button>(R.id.btnMeals)
        mealsButton.setOnClickListener() {
            val intent = Intent(this, MealsActivity::class.java)
            startActivity(intent)
        }

        val calendarButton = findViewById<Button>(R.id.btnCalendar)
        calendarButton.setOnClickListener() {
            val intent = Intent(this, DaysActivity::class.java)
            startActivity(intent)
        }

        val caloriesButton = findViewById<ImageView>(R.id.btnCalories)
        caloriesButton.setOnClickListener() {
            val intent = Intent(this, CaloriesActivity::class.java)
            startActivity(intent)
        }
    }
}