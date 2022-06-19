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

class IngredientsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredients)

        val btnAddIngredient = findViewById<Button>(R.id.btnAddIngredient)
            .setOnClickListener() {
                addIngredientDialog()
            }

        setupRecyclerViewData()
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
        val adapter = IngredientAdapter(this, getIngredientsList())
        recyclerView.adapter = adapter
    }

    // Wyświetla dialog dodawania nowego składnika, dodaje składnik do bazy
    fun addIngredientDialog() {
        val addDialog = Dialog(this)
        addDialog.setCancelable(false)
        addDialog.setContentView(R.layout.dialog_add_ingredient)

        val etName = addDialog.findViewById<EditText>(R.id.etName)
        val etCalories = addDialog.findViewById<EditText>(R.id.etCalories)
        val tvAdd = addDialog.findViewById<TextView>(R.id.tvAdd)
        val tvCancel = addDialog.findViewById<TextView>(R.id.tvCancel)

        tvAdd.setOnClickListener(View.OnClickListener {

            val name = etName.text.toString()
            val calories = etCalories.text.toString()
            val databaseManager: DatabaseManager = DatabaseManager(this)

            if(name.isNotEmpty() && calories.isNotEmpty()) {
                val status =
                    databaseManager.addIngredient(
                        ItemModelIngredient(
                        id = 0, name = name, calories = calories.toInt()))
                if (status > -1) {
                    Toast.makeText(applicationContext,
                        getString(R.string.addedIngredientToast), Toast.LENGTH_SHORT).show()
                    setupRecyclerViewData()
                    addDialog.dismiss()
                }
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

    // Wyświetla dialog aktualizowania składnika, aktualizuje składnik w bazie
    fun updateIngredientDialog(ingredient: ItemModelIngredient) {
        val updateDialog = Dialog(this)
        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.dialog_update_ingredient)

        val etName = updateDialog.findViewById<EditText>(R.id.etName)
        val etCalories = updateDialog.findViewById<EditText>(R.id.etCalories)
        val tvAdd = updateDialog.findViewById<TextView>(R.id.tvAdd)
        val tvCancel = updateDialog.findViewById<TextView>(R.id.tvCancel)

        etName.setText(ingredient.name)
        etCalories.setText(ingredient.calories.toString())

        tvAdd.setOnClickListener(View.OnClickListener {

            val name = etName.text.toString()
            val calories = etCalories.text.toString()
            val databaseManager: DatabaseManager = DatabaseManager(this)

            if(name.isNotEmpty() && calories.isNotEmpty()) {
                val status =
                    databaseManager.updateIngredient(
                        ItemModelIngredient(
                            id = ingredient.id, name = name, calories = calories.toInt()))
                if (status > -1) {
                    Toast.makeText(applicationContext,
                        getString(R.string.updatedIngredientToast), Toast.LENGTH_SHORT).show()
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

    // Wyświetla dialog ostrzegawczy, usuwa składnik z bazy
    fun deleteIngredientDialog(ingredient: ItemModelIngredient) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.deleteDialogTitle))
        builder.setMessage(getString(R.string.deleteDialogDescription))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(getString(R.string.positiveBtn)) { dialogInterface, which ->
            val databaseManager: DatabaseManager = DatabaseManager(this)
            val status = databaseManager.deleteIngredient(ItemModelIngredient(ingredient.id,
                "", 0))
            if(status > -1) {
                Toast.makeText(applicationContext,
                    getString(R.string.deletedIngredientToast), Toast.LENGTH_SHORT).show()

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