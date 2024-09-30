package com.ramadhan.mysayur

import com.ramadhan.mysayur.core.domain.repository.ILocationRepository
import com.ramadhan.mysayur.core.domain.usecase.LocationInteractor
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class LocationInteractorTest {

    private lateinit var locationRepository: ILocationRepository
    private lateinit var locationInteractor: LocationInteractor

    @Before
    fun setUp() {
        locationRepository = mock(ILocationRepository::class.java)
        locationInteractor = LocationInteractor(locationRepository)
    }

    @Test
    fun `isDataExist should return true when repository has data`() {
        // Given
        `when`(locationRepository.isDataExist()).thenReturn(true)

        // When
        val result = locationInteractor.isDataExist()

        // Then
        assert(result)
        verify(locationRepository).isDataExist()
    }

    @Test
    fun `isDataExist should return false when repository has no data`() {
        // Given
        `when`(locationRepository.isDataExist()).thenReturn(false)

        // When
        val result = locationInteractor.isDataExist()

        // Then
        assert(!result)
        verify(locationRepository).isDataExist()
    }
}
