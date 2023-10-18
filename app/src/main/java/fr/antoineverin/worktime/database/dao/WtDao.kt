package fr.antoineverin.worktime.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface WtDao<T> {

    @Insert
    suspend fun insert(obj: T)

    @Update
    suspend fun update(obj: T)

    @Delete
    suspend fun delete(obj: T)

}
