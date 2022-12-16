package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.udacity.project4.R
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.core.context.GlobalContext.get
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.util.DataBindingIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.mockito.Mockito.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest {

//    TODO: test the navigation of the fragments.
//    TODO: test the displayed data on the UI.
//    TODO: add testing for the error messages.
    private lateinit var remindersRepository: ReminderDataSource
    private val reminder: ReminderDTO = ReminderDTO("Title", "Description", "location", 0.0, 0.0)

    @get:Rule
    @Mock
    @InjectMocks
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start(){
        stopKoin()
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    getApplicationContext(),
                    get() as ReminderDataSource
                )
            }

            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { LocalDB.createRemindersDao(getApplicationContext()) }
        }

        startKoin {
            androidContext(getApplicationContext())
            modules(listOf(myModule))
        }
        remindersRepository = get().koin.get()
    }

    @Test
    fun emptyRepositoryDisplayNoDataTextView(){
        runBlocking {
            remindersRepository.deleteAllReminders()
            launchFragmentInContainer<ReminderListFragment>(Bundle(),R.style.AppTheme)
            onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun nonEmptyRepositoryReminderDisplayed(){
        runBlocking {
            remindersRepository.saveReminder(reminder)
            launchFragmentInContainer<ReminderListFragment>(Bundle(),R.style.AppTheme)
            onView(withText(reminder.title)).check(matches(isDisplayed()))
            onView(withText(reminder.description)).check(matches(isDisplayed()))
            onView(withText(reminder.location)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun onClickAddReminderFABButtonNavigateToSaveReminderFragment(){
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(),R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
        onView(withId(R.id.addReminderFAB)).perform(click())
        verify(navController).navigate(ReminderListFragmentDirections.toSaveReminder())
    }



}