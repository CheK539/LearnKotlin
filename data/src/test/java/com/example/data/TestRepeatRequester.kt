package com.example.data

import com.example.data.interfaces.HabitService
import com.example.data.network.RepeatRequester
import com.example.domain.models.HabitUid
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class TestRepeatRequester {
    lateinit var habitService: HabitService

    @Before
    fun setUp() {
        habitService = mock()
    }

    @Test
    fun testRepeatRequester_should_callFewTimes_whenThrowException(): Unit = runBlocking {
        whenever(habitService.deleteHabit(any())).thenThrow(NullPointerException::class.java)

        val repeatRequester = RepeatRequester<Unit>()
        repeatRequester.getResponse { habitService.deleteHabit(HabitUid("test")) }

        verify(habitService, times(repeatRequester.repeatCount)).deleteHabit(any())
    }

    @Test
    fun testRepeatRequester_should_callSetTimes_whenThrowException(): Unit = runBlocking {
        whenever(habitService.deleteHabit(any())).thenThrow(NullPointerException::class.java)

        val repeatRequester = RepeatRequester<Unit>()
        repeatRequester.repeatCount = 2
        repeatRequester.getResponse { habitService.deleteHabit(HabitUid("test")) }

        verify(habitService, times(2)).deleteHabit(any())
    }

    @Test
    fun testRepeatRequester_should_callOneTime(): Unit = runBlocking {
        whenever(habitService.deleteHabit(any())).thenReturn(Response.success(null))

        val repeatRequester = RepeatRequester<Unit>()
        repeatRequester.getResponse { habitService.deleteHabit(HabitUid("test")) }

        verify(habitService, times(1)).deleteHabit(any())
    }
}