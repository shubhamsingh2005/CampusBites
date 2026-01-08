package com.campusbites.app.viewmodel

import androidx.lifecycle.ViewModel
import com.campusbites.app.model.MenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val cartItems: StateFlow<List<MenuItem>> = _cartItems

    fun addToCart(item: MenuItem) {
        _cartItems.value = _cartItems.value + item
    }
}
