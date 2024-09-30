package com.ramadhan.mysayur.view.listlocation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ramadhan.mysayur.core.domain.model.LocationTracker
import com.ramadhan.mysayur.core.domain.usecase.LocationUseCase

class ListLocationViewModel(
    private val locationUseCase: LocationUseCase
) : ViewModel() {

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> get() = _loading

    private val _locations: MutableLiveData<List<LocationTracker>> = MutableLiveData()
    val locations: LiveData<List<LocationTracker>> get() = _locations

    private val _isEmpty: MutableLiveData<Boolean> = MutableLiveData()
    val isEmpty: LiveData<Boolean> get() = _isEmpty


    fun getAllLocations(){
        try {
            _loading.value = true
            _locations.value = locationUseCase.getAllLocations()
            _isEmpty.value = locationUseCase.isDataExist()
            _loading.value = false
        } catch (e: Exception) {
            _loading.value = false
            Log.e("ListLocationViewModel", e.message.toString())
        }
    }

}