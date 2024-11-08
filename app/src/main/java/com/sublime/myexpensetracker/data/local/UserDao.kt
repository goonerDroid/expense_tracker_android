package com.sublime.myexpensetracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sublime.myexpensetracker.data.models.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user WHERE userId = :userId")
    suspend fun getUser(userId: String): User

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>
}
