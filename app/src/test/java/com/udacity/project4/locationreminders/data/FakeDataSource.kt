package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.dto.Result.Success



//Use FakeDataSource that acts as a test double to the LocalDataSource
@Suppress("UNREACHABLE_CODE", "UnusedEquals")
class FakeDataSource(private var reminders: MutableList<ReminderDTO> = mutableListOf()) : ReminderDataSource {

//    TODO: Create a fake data source to act as a double to the real data source
    var returnError = false
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
//        TODO("Return the reminders")
        if(returnError)return Result.Error("Error getting reminders")
        return Success(reminders)
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
       // TODO("save the reminder")
        reminders.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
     //   TODO("return the reminder with the id")
        if (returnError) {
          return  Result.Error("Error getting reminder with id $id")
        } else {
            reminders.forEach{ it.id == id }.let { return@let Success(it) }
       }
        return Result.Error("Reminder not found")
    }


    override suspend fun deleteAllReminders() {
      //  TODO("delete all the reminders")
        reminders.clear()
    }


}