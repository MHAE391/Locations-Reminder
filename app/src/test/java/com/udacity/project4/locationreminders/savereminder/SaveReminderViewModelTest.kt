package com.udacity.project4.locationreminders.savereminder

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.MainCoroutine
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {


    //TODO: provide testing to the SaveReminderView and its live data objects

    private lateinit var remindersRepository: FakeDataSource
    private lateinit var viewModel: SaveReminderViewModel

    private val successfulReminder: ReminderDataItem = ReminderDataItem("Title", "Description", "location", 0.0, 0.0)
    private val emptyTitleReminder: ReminderDataItem = ReminderDataItem("", "Description", "location", 0.0, 0.0)
    private val nullLocationReminder: ReminderDataItem = ReminderDataItem("Title", "Description", null, 0.0, 0.0)

    @get:Rule
    var mainCoroutine = MainCoroutine()
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start() {
        remindersRepository = FakeDataSource()
        viewModel =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), remindersRepository)
    }

    @After
    fun end() {
        stopKoin()
    }
    @Test
    fun saveReminderAndShowSuccessToast() {
            Truth.assertThat(viewModel.validateEnteredData(successfulReminder)).isTrue()
            viewModel.saveReminder(successfulReminder)
        val value = viewModel.showToast.getOrAwaitValue()
        MatcherAssert.assertThat(value, Matchers.`is`("Reminder Saved !"))
    }
    @Test
    fun validateEnteredDataWithEmptyTitleAndShowSnackBarError() {
        runBlocking {
        Truth.assertThat(viewModel.validateEnteredData(emptyTitleReminder)).isFalse()
        Truth.assertThat(viewModel.showSnackBarInt.value).isEqualTo(R.string.err_enter_title)
        }
    }

    @Test
    fun validateEnteredDataWithNullLocationAndShowSnackBarError() {
        runBlocking {
            Truth.assertThat(viewModel.validateEnteredData(nullLocationReminder)).isFalse()
            Truth.assertThat(viewModel.showSnackBarInt.value)
                .isEqualTo(R.string.err_select_location)
        }
    }


    @Test
    fun addNewReminderIsLoadingTriggered() = mainCoroutine.runBlockingTest {

        mainCoroutine.pauseDispatcher()
        viewModel.validateAndSaveReminder(successfulReminder)

        MatcherAssert.assertThat(
            viewModel.showLoading.getOrAwaitValue(),
            Matchers.`is`(true)
        )
        mainCoroutine.resumeDispatcher()
        MatcherAssert.assertThat(
            viewModel.showLoading.getOrAwaitValue(),
            Matchers.`is`(false)
        )
    }

}