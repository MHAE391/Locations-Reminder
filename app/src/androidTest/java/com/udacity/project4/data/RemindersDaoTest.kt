package com.udacity.project4.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.RemindersDao
import com.udacity.project4.locationreminders.data.local.RemindersDatabase

import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers
import org.hamcrest.core.IsNull.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

//    TODO: Add testing implementation to the RemindersDao.kt

    private val reminder:  ReminderDTO = ReminderDTO("Title", "Description", "location", 0.0, 0.0)
    private val reminder2: ReminderDTO = ReminderDTO("Title2", "Description", "location", 0.0, 0.0)
    private val reminder3: ReminderDTO = ReminderDTO("Title3", "Description", "location", 0.0, 0.0)
    private val reminder4: ReminderDTO = ReminderDTO("Title4", "Description", "location", 0.0, 0.0)
    private val reminder5: ReminderDTO = ReminderDTO("Title5", "Description", "location", 0.0, 0.0)

    private val reminders = listOf(reminder,reminder2,reminder3,reminder4,reminder5)
    private lateinit var database: RemindersDatabase
    private lateinit var remindersDao: RemindersDao

    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
        remindersDao = database.reminderDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun saveReminder() {
        runBlocking { remindersDao.saveReminder(reminder) }
    }

    @Test
    fun getReminderReturnSuccess() {
        runBlocking {
           reminders.forEach {reminder->
               remindersDao.saveReminder(reminder)
           }
            val result = remindersDao.getReminderById(reminder.id)
            assertThat(result as ReminderDTO, Matchers.notNullValue())
            with(result) {
                assertThat(id, Matchers.`is`(reminder.id))
                assertThat(title, Matchers.`is`(reminder.title))
                assertThat(description, Matchers.`is`(reminder.description))
                assertThat(latitude, Matchers.`is`(reminder.latitude))
                assertThat(longitude, Matchers.`is`(reminder.longitude))
                assertThat(location, Matchers.`is`(reminder.location))
            }
        }
    }

    @Test
    fun deleteAllRemindersReturnEmptyList() {
        runBlocking {
            reminders.forEach {reminder->
                remindersDao.saveReminder(reminder)
            }
            remindersDao.deleteAllReminders()
            val result = remindersDao.getReminders()
            assertThat(result, Matchers.`is`(emptyList()))
        }
    }

    @Test
    fun getAllRemindersReturnList() {
        runBlocking {
            reminders.forEach {reminder->
                remindersDao.saveReminder(reminder)
            }
            val result = remindersDao.getReminders()
            assertThat(result.size, Matchers.`is`(5))
        }
    }
    @Test
    fun getReminderReturnNull() {
        runBlocking {
            remindersDao.saveReminder(reminder)
            remindersDao.deleteAllReminders()
            val result = remindersDao.getReminderById(reminder.id)
            assertThat(result, nullValue())
        }
    }


}