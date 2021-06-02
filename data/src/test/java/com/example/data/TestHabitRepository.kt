package com.example.data

import com.example.data.interfaces.HabitDao
import com.example.data.interfaces.HabitService
import com.example.data.repositories.HabitRepositoryImpl
import com.example.domain.enums.HabitType
import com.example.domain.enums.PriorityType
import com.example.domain.models.Habit
import com.example.domain.models.HabitUid
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.AdditionalAnswers.returnsFirstArg
import retrofit2.Response

@ExperimentalCoroutinesApi
class TestHabitRepository {
    lateinit var habit: Habit
    lateinit var updatedHabit: Habit
    lateinit var habitService: HabitService
    lateinit var habitDao: HabitDao

    @Before
    fun setUp() {
        habit = Habit("t", "t", PriorityType.Low, HabitType.Negative, 2, 3, "#ffffff")
            .apply { uid = "testUid" }
        habitService = mock()
        habitDao = mock()
        updatedHabit = Habit(
            "Test change",
            "Test description",
            habit.priority,
            habit.type,
            7,
            6,
            habit.color
        ).apply { uid = habit.uid }
    }

    @Test
    fun testHabitRepository_addHabit_should_increaseSize() = runBlocking {
        val habitsList = mutableListOf<Habit>()
        whenever(habitDao.insert(any())).then {
            habitsList.add(returnsFirstArg<Habit>().answer(it))
        }

        /*val response = Response.success(HabitUid("13edf"))
        whenever(habitService.addHabit(habit.toHabitNetwork())).thenReturn(response)*/

        val habitRepository = HabitRepositoryImpl(habitDao, habitService)
        habitRepository.insert(habit)

        Assert.assertEquals(habitsList.size, 1)
    }

    @FlowPreview
    @Test
    fun testHabitRepository_getHabits_should_getAll() = runBlockingTest {
        val expected = listOf(habit, habit, habit)
        val flowOfList = flowOf(expected)
        whenever(habitDao.getAll()).thenReturn(flowOfList)

        //whenever(habitService.getHabits()).thenReturn(Response.success(listOf()))

        val habitRepository = HabitRepositoryImpl(habitDao, habitService)
        val actual = habitRepository.getAll().flatMapConcat { it.asFlow() }.toList()

        Assert.assertEquals(expected.size, actual.size)

        for (i in expected.indices)
            Assert.assertEquals(expected[i], actual[i])
    }

    @Test
    fun testHabitRepository_updateHabit_should_update(): Unit = runBlocking {
        val habits = listOf(habit)
        whenever(habitDao.update(any())).then { mock ->
            val argHabit = returnsFirstArg<Habit>().answer(mock)
            habits.first { it.uid == argHabit.uid }.apply {
                title = argHabit.title
                description = argHabit.description
                type = argHabit.type
                priority = argHabit.priority
                color = argHabit.color
                completeCounter = argHabit.completeCounter
                date = argHabit.date
                periodNumber = argHabit.periodNumber
            }
        }

        /*val response = Response.success(HabitUid(habit.uid))
        whenever(habitService.addHabit(habit.toHabitNetwork())).thenReturn(response)*/

        val habitRepository = HabitRepositoryImpl(habitDao, habitService)
        val prevUid = habit.uid
        habitRepository.update(updatedHabit)

        habit.apply {
            Assert.assertEquals(updatedHabit.title, title)
            Assert.assertEquals(updatedHabit.description, description)
            Assert.assertEquals(updatedHabit.priority, priority)
            Assert.assertEquals(updatedHabit.type, type)
            Assert.assertEquals(updatedHabit.completeCounter, completeCounter)
            Assert.assertEquals(updatedHabit.periodNumber, periodNumber)
            Assert.assertEquals(updatedHabit.color, color)
            Assert.assertEquals(prevUid, uid)
        }
    }

    @Test
    fun testHabitRepository_deleteHabit_should_delete() = runBlocking {
        val habits = mutableListOf(habit)
        whenever(habitDao.delete(any())).then { mock ->
            val argHabit = returnsFirstArg<Habit>().answer(mock)
            habits.remove(argHabit)
        }

        val habitRepository = HabitRepositoryImpl(habitDao, habitService)
        habitRepository.delete(habit)

        Assert.assertEquals(habits.size, 0)
    }
}