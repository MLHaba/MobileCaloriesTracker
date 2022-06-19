package com.example.caloriestracker

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriestracker.adapters.IngredientAdapter
import kotlin.math.ceil

class CreatingMealRecipe() : AppCompatActivity(){

    companion object {
        // lista składników w przepisie
        lateinit var ingredientsListPairs: ArrayList<Pair<Int, ItemModelIngredient>>
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
            addIngredientRecipeDialog()
        }

        val cancelButton = findViewById<Button>(R.id.btnCancel)
        cancelButton.setOnClickListener() {
            finish()
        }

        ingredientsListPairs = ArrayList<Pair<Int, ItemModelIngredient>>()
        setupRecyclerViewData()
    }

    // Zwraca listę składników w przepisie
    private fun getIngredientsListRecipe() : ArrayList<ItemModelIngredient>{
        val list = ArrayList<ItemModelIngredient>()
        for(pair in ingredientsListPairs){
            list.add(pair.second)
        }
        return list
    }

    // Zwraca listę składników przechowywanych w bazie danych
    private fun getIngredientsList(): ArrayList<ItemModelIngredient> {
        val databaseManager: DatabaseManager = DatabaseManager(this)
        return databaseManager.viewIngredients()
    }

    // Ustawia źródło danych i adapter dla RecyclerView
    private fun setupRecyclerViewData() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = IngredientAdapter(this, getIngredientsListRecipe(), IngredientAdapter.VIEW_TYPE_RECIPE)
        recyclerView.adapter = adapter
    }

    // Tworzy posiłek na podstawie listy składników i masy, dodaje go do bazy
    private fun createMeal(){

        if(ingredientsListPairs.size <= 0) {
            Toast.makeText(applicationContext,
                (R.string.noIngredientsToast), Toast.LENGTH_SHORT).show()
            return
        }

        var total_calories: Int = 0
        var total_amount: Int = 0
        var ingredients: String = String()
        for (pair in ingredientsListPairs) {
            total_calories += pair.second.calories
            total_amount += pair.first
            ingredients += pair.second.name + ", "
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
    private fun addIngredientRecipeDialog() {
        val addDialog = Dialog(this)
        addDialog.setCancelable(false)
        addDialog.setContentView(R.layout.dialog_add_ingredient_list)

        val etName = addDialog.findViewById<EditText>(R.id.etName)
        val etAmount = addDialog.findViewById<EditText>(R.id.etAmount)
        val tvCancel = addDialog.findViewById<TextView>(R.id.tvCancel)

        val recyclerView = addDialog.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = IngredientAdapter(this, getIngredientsList(), IngredientAdapter.VIEW_TYPE_CLEAN)
        recyclerView.adapter = adapter

        tvCancel.setOnClickListener(View.OnClickListener {
            addDialog.dismiss()
        })

        addDialog.show()
    }

    fun addIngredientRecipe(ingredient: ItemModelIngredient) {

    }

    // Usuwa składnik z listy składników w przepisie
    fun deleteIngredientDialog(ingredient: ItemModelIngredient) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.deleteDialogTitle))
        builder.setMessage(getString(R.string.deleteDialogDescription))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(getString(R.string.positiveBtn)) { dialogInterface, which ->

            for (pair in ingredientsListPairs) {
                if(pair.second.name == ingredient.name &&
                        pair.second.calories == ingredient.calories){
                    ingredientsListPairs.remove(pair)
                    break
                }
            }

            setupRecyclerViewData()
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(getString(R.string.negativeBtn)) { dialogInterface, which ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}