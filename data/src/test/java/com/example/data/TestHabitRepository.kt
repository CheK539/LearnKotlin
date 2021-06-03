package com.example.data

import com.example.data.interfaces.HabitDao
import com.example.data.interfaces.HabitService
import com.example.data.repositories.HabitRepositoryImpl
import com.example.domain.enums.HabitType
import com.example.domain.enums.PriorityType
import com.example.domain.models.Habit
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.AdditionalAnswers.returnsFirstArg

@FlowPreview
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
            "Mod",
            "Test description",
            habit.priority,
            habit.type,
            7,
            6,
            habit.color
        ).apply { uid = habit.uid }
    }

    @Test
    fun testHabitRepository_should_addHabit() = runBlocking {
        val habitsList = mutableListOf<Habit>()
        whenever(habitDao.insert(any())).then {
            habitsList.add(returnsFirstArg<Habit>().answer(it))
        }

        val habitRepository = HabitRepositoryImpl(habitDao, habitService)
        habitRepository.insert(habit)

        Assert.assertEquals(habitsList.size, 1)
    }

    @Test
    fun testHabitRepository_should_getHabits() = runBlockingTest {
        val expected = listOf(habit, habit, habit)
        val flowOfList = flowOf(expected)
        whenever(habitDao.getAll()).thenReturn(flowOfList)

        val habitRepository = HabitRepositoryImpl(habitDao, habitService)
        val actual = habitRepository.getAll().flatMapConcat { it.asFlow() }.toList()

        Assert.assertEquals(expected.size, actual.size)

        for (i in expected.indices)
            Assert.assertEquals(expected[i], actual[i])
    }

    @Test
    fun testHabitRepository_should_updateHabit(): Unit = runBlocking {
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
    fun testHabitRepository_should_delete() = runBlocking {
        val habits = mutableListOf(habit)
        whenever(habitDao.delete(any())).then { mock ->
            val argHabit = returnsFirstArg<Habit>().answer(mock)
            habits.remove(argHabit)
        }

        val habitRepository = HabitRepositoryImpl(habitDao, habitService)
        habitRepository.delete(habit)

        Assert.assertEquals(habits.size, 0)
    }

    @Test
    fun testHabitRepository_should_deleteAll() = runBlocking {
        val habits = mutableListOf(habit, habit, habit)
        whenever(habitDao.deleteAll()).then { habits.clear() }

        val habitRepository = HabitRepositoryImpl(habitDao, habitService)
        habitRepository.deleteAll()

        Assert.assertEquals(habits.size, 0)
    }

    @Test
    fun testHabitRepository_should_getByTitle() = runBlockingTest {
        val habits = listOf(habit, updatedHabit)
        whenever(habitDao.getByTitle(habit.title))
            .thenReturn(flowOf(habits.filter { it.title.contains(habit.title) }))

        val habitRepository = HabitRepositoryImpl(habitDao, habitService)
        val actual = habitRepository.getByTitle(habit.title).flatMapConcat { it.asFlow() }.toList()

        Assert.assertEquals(1, actual.size)
    }

    @Test
    fun testHabitRepository_should_getByPriorityAscending() = runBlockingTest {
        updatedHabit.priority = PriorityType.High
        val sortedHabits = listOf(habit, updatedHabit).sortedBy { it.priority.priorityId }
        whenever(habitDao.getByPriorityAscending())
            .thenReturn(flowOf(sortedHabits))

        val habitRepository = HabitRepositoryImpl(habitDao, habitService)
        val actual = habitRepository.getByPriorityAscending().flatMapConcat { it.asFlow() }
            .toList()
            .toTypedArray()

        Assert.assertArrayEquals(sortedHabits.toTypedArray(), actual)
    }

    @Test
    fun testHabitRepository_should_getByPriorityDescending() = runBlockingTest {
        updatedHabit.priority = PriorityType.High
        val sortedHabits = listOf(habit, updatedHabit).sortedByDescending { it.priority.priorityId }
        whenever(habitDao.getByPriorityDescending())
            .thenReturn(flowOf(sortedHabits))

        val habitRepository = HabitRepositoryImpl(habitDao, habitService)
        val actual = habitRepository.getByPriorityDescending().flatMapConcat { it.asFlow() }
            .toList()
            .toTypedArray()

        Assert.assertArrayEquals(sortedHabits.toTypedArray(), actual)
    }

    @Test
    fun testHabitRepository_should_getByUid() = runBlockingTest {
        updatedHabit.uid = "mod"
        val filterHabit = listOf(habit, updatedHabit).first { it.uid == updatedHabit.uid }
        whenever(habitDao.getByUid(updatedHabit.uid)).thenReturn(flowOf(filterHabit))

        val habitRepository = HabitRepositoryImpl(habitDao, habitService)
        val actualHabit = habitRepository.getByUid(updatedHabit.uid).first()

        Assert.assertEquals(filterHabit.uid, actualHabit.uid)
    }

    @Test
    fun testHabitRepository_should_findAllByTitle() = runBlockingTest {
        val habits = listOf(habit, updatedHabit)
        whenever(habitDao.getByTitle(""))
            .thenReturn(flowOf(habits.filter { it.title.contains("") }))

        val habitRepository = HabitRepositoryImpl(habitDao, habitService)
        val actual = habitRepository.getByTitle("").flatMapConcat { it.asFlow() }.toList()

        Assert.assertEquals(habits.size, actual.size)
    }

    @Test
    fun testHabitRepository_should_getEmptyByUid() = runBlockingTest {
        updatedHabit.uid = "mod"
        val filterHabit = listOf(habit, updatedHabit).firstOrNull { it.uid == "" }
            ?: Habit("", "", PriorityType.High, HabitType.Positive, 0, 0, "")
        whenever(habitDao.getByUid(updatedHabit.uid))
            .thenReturn(flowOf(filterHabit))

        val habitRepository = HabitRepositoryImpl(habitDao, habitService)
        val actualHabit = habitRepository.getByUid(updatedHabit.uid).first()

        Assert.assertEquals("", actualHabit.uid)
    }
}