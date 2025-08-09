package com.campusbites.app.viewmodel

import androidx.lifecycle.ViewModel
import com.campusbites.app.models.CartItem
import com.campusbites.app.models.MenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    fun addToCart(item: MenuItem) {
        val current = _cartItems.value.toMutableList()
        val existing = current.find { it.id == item.id }

        if (existing != null) {
            val updated = existing.copy(quantity = existing.quantity + 1)
            current[current.indexOf(existing)] = updated
        } else {
            current.add(
                CartItem(
                    id = item.id,
                    shopId = item.shopId,
                    name = item.name,
                    price = item.price,
                    quantity = 1
                )
            )
        }
        _cartItems.value = current
    }

    fun removeFromCart(id: Int) {
        _cartItems.value = _cartItems.value.filterNot { it.id == id }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}
