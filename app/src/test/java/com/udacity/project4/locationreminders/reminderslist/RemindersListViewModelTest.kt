package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    //TODO: provide testing to the RemindersListViewModel and its live data objects
    private val reminder: ReminderDTO = ReminderDTO("Title", "Description", "location", 0.0, 0.0)
    private val reminder2: ReminderDTO = ReminderDTO("Title2", "Description", "location", 0.0, 0.0)
    private val reminder3: ReminderDTO = ReminderDTO("Title3", "Description", "location", 0.0, 0.0)
    private val reminder4: ReminderDTO = ReminderDTO("Title4", "Description", "location", 0.0, 0.0)
    private val reminder5: ReminderDTO = ReminderDTO("Title5", "Description", "location", 0.0, 0.0)
    private val reminders = listOf(reminder,reminder2,reminder3,reminder4,reminder5)
    private lateinit var remindersRepository: FakeDataSource
    private lateinit var viewModel: RemindersListViewModel

    @get:Rule
    var mainCoroutine = MainCoroutine()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start() {
        stopKoin()
        remindersRepository = FakeDataSource()
        viewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(), remindersRepository)
    }

    @After
    fun end() {
        stopKoin()
    }
    @Test
    fun getRemindersReturnError(){
        mainCoroutine.runBlockingTest {
            remindersRepository.returnError = true
            viewModel.loadReminders()
            Truth.assertThat(viewModel.showSnackBar.value).isEqualTo("Error getting reminders")
        }
    }
    @Test
    fun getRemindersReturnNonEmptyList(){
       mainCoroutine. runBlockingTest{
            remindersRepository.returnError = false
            reminders.forEach {reminder->
                remindersRepository.saveReminder(reminder)
            }
            viewModel.loadReminders()
           Truth.assertThat(viewModel.showNoData.value).isFalse()
           Truth.assertThat(viewModel.remindersList.value).isNotEmpty()

        }
    }
    @Test
    fun getRemindersReturnEmptyList(){
        mainCoroutine. runBlockingTest{
            remindersRepository.returnError = false
            viewModel.loadReminders()
            Truth.assertThat(viewModel.showNoData.value).isTrue()
            Truth.assertThat(viewModel.remindersList.value).isEmpty()
        }
    }

}