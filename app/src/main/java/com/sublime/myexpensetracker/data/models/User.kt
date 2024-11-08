package com.sublime.myexpensetracker.data.models

import androidx.room.PrimaryKey

data class User(
    @PrimaryKey val userId: String,
    val name: String,
)
