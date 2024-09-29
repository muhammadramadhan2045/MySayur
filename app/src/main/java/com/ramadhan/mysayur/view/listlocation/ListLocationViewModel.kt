package com.ramadhan.mysayur.view.listlocation

import androidx.lifecycle.ViewModel
import com.ramadhan.mysayur.core.domain.usecase.LocationUseCase

class ListLocationViewModel(
    private val locationUseCase: LocationUseCase
) : ViewModel(){

        fun getAllLocations() = locationUseCase.getAllLocations()

        fun isDataExist() = locationUseCase.isDataExist()
}