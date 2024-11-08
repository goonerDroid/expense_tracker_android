package com.sublime.myexpensetracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sublime.myexpensetracker.data.models.User
import com.sublime.myexpensetracker.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserManagementViewModel
    @Inject
    constructor(
        private val expenseRepository: ExpenseRepository,
    ) : ViewModel() {
        private val _users = MutableStateFlow<List<User>>(emptyList())
        val users: StateFlow<List<User>> = _users.asStateFlow()

        private val _currentUser = MutableStateFlow<User?>(null)
        val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

        fun createUser(name: String) {
            viewModelScope.launch {
                val newUser = expenseRepository.createUser(name)
                _users.value = expenseRepository.getAllUsers()
                _currentUser.value = newUser
            }
        }
    }
