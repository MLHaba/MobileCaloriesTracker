package com.example.caloriestracker

// Model składnika
class ItemModelIngredient(val id: Int, val name: String, val calories: Int){}

// Model posiłku
class ItemModelMeal(val id: Int, val name: String, val calories: Int, val description: String){}

// Model dnia
class ItemModelDay(val id: Int, val date: String, val calories: Int){}


