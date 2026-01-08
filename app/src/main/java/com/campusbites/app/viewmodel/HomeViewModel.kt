package com.campusbites.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusbites.app.api.RetrofitInstance
import com.campusbites.app.model.Shop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _shops = MutableStateFlow<List<Shop>>(emptyList())
    val shops: StateFlow<List<Shop>> = _shops

    fun fetchShops(context: Context) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.getApi(context).getShops()
                if (response.isSuccessful && response.body() != null) {
                    _shops.value = response.body()!!
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
