package com.example.driverlogisticsapp.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driverlogisticsapp.domain.DeliveryRepository
import com.example.driverlogisticsapp.domain.model.Delivery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(
    private val repository: DeliveryRepository
): ViewModel() {
    val deliveryList: StateFlow<List<Delivery>> = repository.getAllFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    var isLoading by mutableStateOf(false)
        private set


    fun loadDeliveryList(){
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                val data = repository.getAll()
                Log.i("DeliveryViewModel", "Loaded delivery list: $data")
                if (data.isEmpty()) {
                    repository.fetchDeliveryList()
                }
            } catch (e: Exception){
                Log.e("DeliveryViewModel", "Error loading delivery list", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun updateStatus(id: Long, bool: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateStatus(id, bool)
        }
    }
}