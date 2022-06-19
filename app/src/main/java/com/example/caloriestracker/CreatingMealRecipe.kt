package com.example.caloriestracker

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriestracker.adapters.IngredientAdapter
import com.example.caloriestracker.adapters.MealAdapter
import kotlin.math.ceil

class CreatingMealRecipe() : AppCompatActivity(){

    companion object {
        // lista składników w przepisie
        lateinit var ingredientsList: ArrayList<ItemModelIngredient>

        // całkowita masa posiłku
        var total_amount: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creating_meal_recipe)

        val ingredientsButton = findViewById<Button>(R.id.btnAddMeal)
        ingredientsButton.setOnClickListener() {
            createMeal()
        }

        val mealsButton = findViewById<Button>(R.id.btnAddIngredient)
        mealsButton.setOnClickListener() {
            addIngredientMealDialog()
        }

        ingredientsList = ArrayList<ItemModelIngredient>()
        total_amount = 0
        setupRecyclerViewData()
    }

    // Ustawia źródło danych i adapter dla RecyclerView
    private fun setupRecyclerViewData() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = IngredientAdapter(this, ingredientsList)
        recyclerView.adapter = adapter
    }

    // Tworzy posiłek na podstawie listy składników i masy, dodaje go do bazy
    private fun createMeal(){

        if(ingredientsList.size <= 0) {
            Toast.makeText(applicationContext,
                (R.string.noIngredientsToast), Toast.LENGTH_SHORT).show()
            return
        }

        var total_calories: Int = 0
        var ingredients: String = String()
        for (itemModelIngredient in ingredientsList) {
            total_calories += itemModelIngredient.calories
            ingredients += itemModelIngredient.name + ", "
        }

        if(total_calories <= 0) {
            Toast.makeText(applicationContext,
                (R.string.invalidCalories), Toast.LENGTH_SHORT).show()
            return
        }

        val etName = findViewById<EditText>(R.id.etName)
        val name = etName.text.toString()
        if(name.isEmpty()) {
            Toast.makeText(applicationContext,
                (R.string.insertDataToast), Toast.LENGTH_SHORT).show()
            return
        }

        val calculatedCalories = ceil(total_calories.toDouble() / total_amount.toDouble() * 100)
        val databaseManager: DatabaseManager = DatabaseManager(this)
        val status =
            databaseManager.addMeal(ItemModelMeal(0, name, calculatedCalories.toInt(), ingredients))
        if(status > -1){
            Toast.makeText(applicationContext,
                (R.string.addedMealToast), Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    // Wyświetla dialog dodawania składnika do posiłku, dodaje składnik do listy przepisu
    private fun addIngredientMealDialog() {
        val addDialog = Dialog(this)
        addDialog.setCancelable(false)
        addDialog.setContentView(R.layout.dialog_add_ingredient_meal)

        val etName = addDialog.findViewById<EditText>(R.id.etName)
        val etAmount = addDialog.findViewById<EditText>(R.id.etAmount)
        val tvAdd = addDialog.findViewById<TextView>(R.id.tvAdd)
        val tvCancel = addDialog.findViewById<TextView>(R.id.tvCancel)

        tvAdd.setOnClickListener(View.OnClickListener {

            val name = etName.text.toString()
            val amount = etAmount.text.toString()
            val databaseManager: DatabaseManager = DatabaseManager(this)

            if(name.isNotEmpty() && amount.isNotEmpty()) {
                val status =
                    databaseManager.existsIngredient(name)
                if (status > -1) {
                    val calculatedCalories = ceil(status.toDouble() * (amount.toDouble()/100))
                    ingredientsList.add(ItemModelIngredient(0, name, calculatedCalories.toInt()))
                    total_amount += amount.toInt()
                    setupRecyclerViewData()
                    addDialog.dismiss()
                } else {
                    Toast.makeText(applicationContext,
                        getString(R.string.unknownIngredientToast), Toast.LENGTH_SHORT).show()
            }
            }else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.insertDataToast), Toast.LENGTH_SHORT
                ).show()
            }
        })

        tvCancel.setOnClickListener(View.OnClickListener {
            addDialog.dismiss()
        })

        addDialog.show()
    }
}