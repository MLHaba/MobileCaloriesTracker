//252906, Michał Haba 2022
package com.example.caloriestracker

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DatabaseManager(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

        companion object {
            // Database
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "CaloriesTrackerDatabase"

            // Tables
            private const val TABLE_INGREDIENTS = "IngredientsTable"
            private const val TABLE_MEALS = "MealsTable"
            private const val TABLE_DAYS = "DaysTable"

            // Ingredients table attributes
            private const val KEY_ID_I = "_id"
            private const val KEY_NAME_I = "name_i"
            private const val KEY_CALORIES_I = "calories_i"

            // Meals table attributes
            private const val KEY_ID_M = "_id"
            private const val KEY_NAME_M = "name_m"
            private const val KEY_CALORIES_M = "calories_m"
            private const val KEY_DESCRIPTION_M = "description_m"

            // Days table attributes
            private const val KEY_ID_D = "_id"
            private const val KEY_DATE_D = "date_d"
            private const val KEY_CALORIES_D = "calories_d"

            // Date format
            public const val DATE_PATTERN = "dd-MM-yyyy"
        }

    // Tworzy bazę danych
    override fun onCreate(database: SQLiteDatabase?) {
        val CREATE_TABLE_INGREDIENTS = ("CREATE TABLE " + TABLE_INGREDIENTS + "("
                + KEY_ID_I + " INTEGER PRIMARY KEY, " + KEY_NAME_I + " TEXT, "
                + KEY_CALORIES_I + " INTEGER" + ")")

        val CREATE_TABLE_MEALS = ("CREATE TABLE " + TABLE_MEALS + "("
                + KEY_ID_M + " INTEGER PRIMARY KEY, " + KEY_NAME_M + " TEXT, "
                + KEY_CALORIES_M + " INTEGER, " + KEY_DESCRIPTION_M + " TEXT" + ")")

        val CREATE_TABLE_DAYS = ("CREATE TABLE " + TABLE_DAYS + "("
                + KEY_ID_D + " INTEGER PRIMARY KEY, " + KEY_DATE_D + " TEXT, "
                + KEY_CALORIES_D + " INTEGER" + ")")

        database?.execSQL(CREATE_TABLE_INGREDIENTS)
        database?.execSQL(CREATE_TABLE_MEALS)
        database?.execSQL(CREATE_TABLE_DAYS)
    }

    // Aktualizuje wersję bazy danych
    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        database!!.execSQL("DROP TABLE IF EXISTS $TABLE_INGREDIENTS")
        database.execSQL("DROP TABLE IF EXISTS $TABLE_MEALS")
        database.execSQL("DROP TABLE IF EXISTS $TABLE_DAYS")
        onCreate(database)
    }

    // region INGREDIENT
    // Dodawaje składnik do bazy danych
    fun addIngredient(ingredient: ItemModelIngredient) : Long {
        val database = this.writableDatabase

        // Pairing values with keys
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME_I, ingredient.name)
        contentValues.put(KEY_CALORIES_I, ingredient.calories)

        // Adding element to database
        val success = database.insert(TABLE_INGREDIENTS, null, contentValues)
        database.close()
        return success
    }

    // Wyświetla składniki w bazie danych
    @SuppressLint("Range")
    fun viewIngredients() : ArrayList<ItemModelIngredient> {
        val ingredientsList = ArrayList<ItemModelIngredient>()

        val selectQuery = "SELECT * FROM $TABLE_INGREDIENTS"
        val database = this.readableDatabase
        var cursor: Cursor? = null

        // Reading query from database
        try {
            cursor = database.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            database.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var calories: Int

        if(cursor.moveToFirst()){
            do {
                // Reading values from cursor
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID_I))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME_I))
                calories = cursor.getInt(cursor.getColumnIndex(KEY_CALORIES_I))

                // Adding Ingredient to list
                val ingredient = ItemModelIngredient(id = id, name = name, calories = calories)
                ingredientsList.add(ingredient)

            } while (cursor.moveToNext())
        }

        return ingredientsList
    }

    // Aktualizuje składnik w bazie danych
    fun updateIngredient(ingredient: ItemModelIngredient) : Int {
        val database = this.writableDatabase

        // Pairing values with keys
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME_I, ingredient.name)
        contentValues.put(KEY_CALORIES_I, ingredient.calories)

        // Updating element in database
        val success = database.update(TABLE_INGREDIENTS, contentValues,
            KEY_ID_I + "=" + ingredient.id, null)

        database.close()
        return success
    }

    // Usuwa składnik z bazy danych
    fun deleteIngredient(ingredient: ItemModelIngredient) : Int {
        val database = this.writableDatabase

        // Deleting element from database
        val success = database.delete(TABLE_INGREDIENTS,
            KEY_ID_I + "=" + ingredient.id, null)

        database.close()
        return success
    }

    // Sprawdza czy istnieje składnik o podanej nazwie, zwraca wartość kalorii jeśli istnieje,
    //zwraca -1 jeśli nie istnieje
    @SuppressLint("Range")
    fun existsIngredient(name: String) : Int {
        val database = this.writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_INGREDIENTS WHERE $KEY_NAME_I = ?"
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(selectQuery, arrayOf(name))
        } catch (e: SQLiteException) {
            database.execSQL(selectQuery)
            return -1
        }

        val calories: Int

        return if (cursor.moveToFirst()){
            calories = cursor.getInt(cursor.getColumnIndex(KEY_CALORIES_I))
            calories

        } else {
            -1
        }
    }
    //endregion

    //region MEAL
    // Dodaje posiłek do bazy danych
    fun addMeal(meal: ItemModelMeal) : Long {
        val database = this.writableDatabase

        // Pairing values with keys
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME_M, meal.name)
        contentValues.put(KEY_CALORIES_M, meal.calories)
        contentValues.put(KEY_DESCRIPTION_M, meal.description)

        // Adding element to database
        val success = database.insert(TABLE_MEALS, null, contentValues)
        database.close()
        return success
    }

    // Wyświetla posiłki w bazie danych
    @SuppressLint("Range")
    fun viewMeals() : ArrayList<ItemModelMeal> {
        val mealsList = ArrayList<ItemModelMeal>()

        val selectQuery = "SELECT * FROM $TABLE_MEALS"
        val database = this.readableDatabase
        var cursor: Cursor? = null

        // Reading query from database
        try {
            cursor = database.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            database.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var calories: Int
        var description: String

        if(cursor.moveToFirst()){
            do {
                // Reading values from cursor
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID_M))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME_M))
                calories = cursor.getInt(cursor.getColumnIndex(KEY_CALORIES_M))
                description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION_M))

                // Adding Meal to list
                val meal = ItemModelMeal(id = id, name = name, calories = calories,
                    description = description)
                mealsList.add(meal)

            } while (cursor.moveToNext())
        }

        return mealsList
    }

    // Aktualizuje posiłek w bazie danych
    fun updateMeal(meal: ItemModelMeal) : Int {
        val database = this.writableDatabase

        // Lączenie wartości z kluczami
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME_M, meal.name)
        contentValues.put(KEY_CALORIES_M, meal.calories)
        contentValues.put(KEY_DESCRIPTION_M, meal.description)

        // Aktualizowanie w bazie danych
        val success = database.update(TABLE_MEALS, contentValues,
            KEY_ID_M + "=" + meal.id, null)

        database.close()
        return success
    }

    // Usuwa posiłek z bazy danych
    fun deleteMeal(meal: ItemModelMeal) : Int {
        val database = this.writableDatabase

        // Usuwa element z bazy
        val success = database.delete(TABLE_MEALS,
            KEY_ID_M + "=" + meal.id, null)

        database.close()
        return success
    }
    //endregion

    // region DAY
    // Dodaje dzień do bazy danych
    fun addDay(day: ItemModelDay) : Long {
        val database = this.writableDatabase

        // Pairing values with keys
        val contentValues = ContentValues()
        contentValues.put(KEY_DATE_D, day.date)
        contentValues.put(KEY_CALORIES_D, day.calories)

        // Adding element to database
        val success = database.insert(TABLE_DAYS, null, contentValues)
        database.close()
        return success
    }

    // Wyświetla dni w bazie danych
    @SuppressLint("Range")
    fun viewDays() : ArrayList<ItemModelDay> {
        val daysList = ArrayList<ItemModelDay>()

        val selectQuery = "SELECT * FROM $TABLE_DAYS ORDER BY $KEY_ID_D DESC"
        val database = this.readableDatabase
        var cursor: Cursor? = null

        // Reading query from database
        try {
            cursor = database.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            database.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var date: String
        var calories: Int

        if(cursor.moveToFirst()){
            do {
                // Reading values from cursor
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID_D))
                date = cursor.getString(cursor.getColumnIndex(KEY_DATE_D))
                calories = cursor.getInt(cursor.getColumnIndex(KEY_CALORIES_D))

                // Adding Ingredient to list
                val day = ItemModelDay(id = id, date = date, calories = calories)
                daysList.add(day)

            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()

        return daysList
    }

    // Aktualizuje dzień w bazie danych
    fun updateDay(day: ItemModelDay) : Int {
        val database = this.writableDatabase

        // Pairing values with keys
        val contentValues = ContentValues()
        contentValues.put(KEY_DATE_D, day.date)
        contentValues.put(KEY_CALORIES_D, day.calories)

        // Updating element in database
        val success = database.update(TABLE_DAYS, contentValues,
            KEY_ID_D + "=" + day.id, null)

        database.close()
        return success
    }

    // Usuwa dzień z bazy danych
    fun deleteDay(day: ItemModelDay) : Int {
        val database = this.writableDatabase

        // Deleting element from database
        val success = database.delete(TABLE_DAYS,
            KEY_ID_D + "=" + day.id, null)

        database.close()
        return success
    }

    // Jeśli dzień o podanej dacie nie istnieje, dodaje go do bazy danych, jeśli istnieje, sumuje
    // ilość kalorii istniejącego dnia z podaną i aktualizuje dane w bazie
    @SuppressLint("Range")
    fun updateDayOnDate(day: ItemModelDay) {
        val database = this.writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_DAYS WHERE $KEY_DATE_D = ?"
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(selectQuery, arrayOf(day.date))
        } catch (e: SQLiteException) {
            database.execSQL(selectQuery)
            return
        }

        val id: Int
        var date: String
        val calories: Int

        if (cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex(KEY_ID_D))
            calories = cursor.getInt(cursor.getColumnIndex(KEY_CALORIES_D))
            updateDay(ItemModelDay(id, day.date, calories + day.calories))
        } else {
            addDay(day)
        }
    }
    // endregion

    // DEBUG MODE
    fun temp(id: Int) : Int {
        val database = this.writableDatabase

        // Deleting element from database
        val success = database.delete(TABLE_DAYS,
            KEY_ID_D + "=" + id, null)

        database.close()
        return success
    }
}