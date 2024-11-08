package com.sublime.myexpensetracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val userId: String,
    val name: String,
)
