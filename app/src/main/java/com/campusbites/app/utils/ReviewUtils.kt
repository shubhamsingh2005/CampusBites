package com.campusbites.app.utils

import com.campusbites.app.models.Review

object ReviewUtils {
    fun getAverageRating(reviews: List<Review>): Double {
        return if (reviews.isEmpty()) 0.0 else reviews.sumOf { it.rating.toDouble() } / reviews.size
    }

    fun getReviewCount(reviews: List<Review>): Int {
        return reviews.size
    }
}