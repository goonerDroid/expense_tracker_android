package com.sublime.myexpensetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.sublime.myexpensetracker.data.models.Expense
import com.sublime.myexpensetracker.data.models.ExpenseSplit
import com.sublime.myexpensetracker.data.models.User
import java.math.BigDecimal

@Database(entities = [User::class, Expense::class, ExpenseSplit::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun expenseDao(): ExpenseDao

    abstract fun expenseSplitDao(): ExpenseSplitDao
}

class Converters {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String = value.toString()

    @TypeConverter
    fun toBigDecimal(value: String): BigDecimal = BigDecimal(value)
}
