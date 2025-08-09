package com.campusbites.app.utils
import com.campusbites.app.models.OrderItem

object PriceUtils {
    fun calculateTotal(items: List<OrderItem>): Double {
        return items.sumOf { it.price * it.quantity }
    }
    fun format(price: Double): String {
        return "%.2f".format(price)
    }
}
