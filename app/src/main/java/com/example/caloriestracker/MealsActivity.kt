package com.example.caloriestracker

import android.app.AlertDialog
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
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriestracker.adapters.IngredientAdapter
import com.example.caloriestracker.adapters.MealAdapter

class MealsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meals)

        val btnAddIngredient = findViewById<Button>(R.id.btnAddMeal)
            .setOnClickListener() {
                //addMealDialog()
                val intent = Intent(this, CreatingMealRecipe::class.java)
                startActivity(intent)
            }
        
        setupRecyclerViewData()
    }

    override fun onRestart() {
        super.onRestart()
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
        val adapter = MealAdapter(this, getMealsList(), MealAdapter.VIEW_TYPE_REGULAR)
        recyclerView.adapter = adapter
    }

    // Wyświetla dialog aktualizowania posiłku, aktualizuje posiłek w bazie
    fun updateMealDialog(meal: ItemModelMeal) {
        val updateDialog = Dialog(this)
        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.dialog_update_meal)

        val etName = updateDialog.findViewById<EditText>(R.id.etName)
        val etCalories = updateDialog.findViewById<EditText>(R.id.etCalories)
        val etDesc = updateDialog.findViewById<EditText>(R.id.etDesc)
        val tvAdd = updateDialog.findViewById<TextView>(R.id.tvAdd)
        val tvCancel = updateDialog.findViewById<TextView>(R.id.tvCancel)

        etName.setText(meal.name)
        etCalories.setText(meal.calories.toString())
        etDesc.setText(meal.description)

        tvAdd.setOnClickListener(View.OnClickListener {

            val name = etName.text.toString()
            val calories = etCalories.text.toString()
            val description = etDesc.text.toString()
            val databaseManager: DatabaseManager = DatabaseManager(this)

            if(name.isNotEmpty() && calories.isNotEmpty() && description.isNotEmpty()) {
                val status =
                    databaseManager.updateMeal(
                        ItemModelMeal(
                            id = meal.id, name = name, calories = calories.toInt(), description = description))
                if (status > -1) {
                    Toast.makeText(applicationContext,
                        getString(R.string.updatedMealToast), Toast.LENGTH_SHORT).show()
                    setupRecyclerViewData()
                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(applicationContext,
                    getString(R.string.insertDataToast), Toast.LENGTH_SHORT).show()
            }
        })

        tvCancel.setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })

        updateDialog.show()
    }

    // Wyświetla dialog ostrzegawczy, usuwa posiłek z bazy
    fun deleteMealDialog(meal: ItemModelMeal) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.deleteDialogTitle))
        builder.setMessage(getString(R.string.deleteDialogDescription))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(getString(R.string.positiveBtn)) { dialogInterface, which ->
            val databaseManager: DatabaseManager = DatabaseManager(this)
            val status = databaseManager.deleteMeal(ItemModelMeal(meal.id,
                "", 0, ""))
            if(status > -1) {
                Toast.makeText(applicationContext,
                    getString(R.string.deletedMealToast), Toast.LENGTH_SHORT).show()

                setupRecyclerViewData()
            }
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