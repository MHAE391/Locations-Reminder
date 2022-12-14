package com.udacity.project4.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.RemindersDatabase
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

//    TODO: Add testing implementation to the RemindersLocalRepository.kt

    private val reminder : ReminderDTO = ReminderDTO("Title","Description","location",0.0,0.0)

    private lateinit var remindersLocalRepository: RemindersLocalRepository
    private lateinit var database: RemindersDatabase

    @Before
    fun createRepository(){
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),RemindersDatabase::class.java).allowMainThreadQueries().build()
        remindersLocalRepository = RemindersLocalRepository(database.reminderDao(),Dispatchers.Main)
    }

    @After
    @Throws(IOException::class)
    fun end(){
        database.close()
    }

    @Test
     fun saveReminder(){
      runBlocking {    remindersLocalRepository.saveReminder(reminder) }
    }
    @Test
    fun getReminderReturnSuccess(){

        runBlocking {
            remindersLocalRepository.saveReminder(reminder)
            val result = remindersLocalRepository.getReminder(reminder.id)
            ViewMatchers.assertThat(result is Result.Success, Matchers.`is`(true))
            with((result as Result.Success).data) {
                ViewMatchers.assertThat(title, Matchers.`is`(reminder.title))
                ViewMatchers.assertThat(description, Matchers.`is`(reminder.description))
                ViewMatchers.assertThat(latitude, Matchers.`is`(reminder.latitude))
                ViewMatchers.assertThat(longitude, Matchers.`is`(reminder.longitude))
                ViewMatchers.assertThat(location, Matchers.`is`(reminder.location))
            }
        }

    }

    @Test
    fun deleteAllReminders(){
        runBlocking{
            remindersLocalRepository.saveReminder(reminder)
            remindersLocalRepository.deleteAllReminders()
            val result = remindersLocalRepository.getReminders()
            ViewMatchers.assertThat(result is Result.Success, Matchers.`is`(true))
            ViewMatchers.assertThat((result as Result.Success).data, Matchers.`is`(emptyList()))
        }
    }

    @Test
    fun deleteReminderReturnError (){
        runBlocking {
            remindersLocalRepository.saveReminder(reminder)
            remindersLocalRepository.deleteAllReminders()
            val result = remindersLocalRepository.getReminder(reminder.id)
            ViewMatchers.assertThat(result is Result.Error, Matchers.`is`(true))
            ViewMatchers.assertThat(
                (result as Result.Error).message,
                Matchers.`is`("Reminder not found!")
            )
        }
    }

}