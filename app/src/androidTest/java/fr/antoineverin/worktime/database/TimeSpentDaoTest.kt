package fr.antoineverin.worktime.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.antoineverin.worktime.database.dao.TimeSpentDao
import fr.antoineverin.worktime.database.entities.TimeSpent
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

@RunWith(AndroidJUnit4::class)
class TimeSpentDaoTest {

    private lateinit var database: WtDatabase
    private lateinit var timeSpentDao: TimeSpentDao

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, WtDatabase::class.java).build()
        timeSpentDao = database.timeSpentDao()
    }

    @After
    fun close() {
        database.close()
    }

    @Test
    fun testGetTimeSpent() {
        val ts = TimeSpent(
            0,
            YearMonth.of(2023, 3),
            LocalDate.of(2023, 3, 4),
            LocalTime.of(8, 30),
            LocalTime.of(16, 45)
        )
        val ts2 = TimeSpent(
            1,
            YearMonth.of(2023, 4),
            LocalDate.of(2023, 4, 4),
            LocalTime.of(8, 30),
            LocalTime.of(16, 45)
        )
//        timeSpentDao.insert(ts)
//        timeSpentDao.insert(ts2)
//        assertThat(timeSpentDao.getAllTimeSpent()[0], equalTo(ts))
//        assertThat(timeSpentDao.getAllTimeSpent()[1], equalTo(ts2))
//        assertThat(timeSpentDao.getTimeSpentFromPeriod(YearMonth.of(2023, 3).toString())[0], equalTo(ts))
//        assertThat(timeSpentDao.getTimeSpentFromPeriod(YearMonth.of(2023, 4).toString())[0], equalTo(ts2))
    }

}
