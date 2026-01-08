package com.campusbites.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusbites.app.api.RetrofitInstance
import com.campusbites.app.model.MenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShopPageViewModel : ViewModel() {
    private val _menu = MutableStateFlow<List<MenuItem>>(emptyList())
    val menu: StateFlow<List<MenuItem>> = _menu

    fun fetchMenu(context: Context, shopId: Long) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.getApi(context).getMenu(shopId)
                if (response.isSuccessful && response.body() != null) {
                    _menu.value = response.body()!!
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
