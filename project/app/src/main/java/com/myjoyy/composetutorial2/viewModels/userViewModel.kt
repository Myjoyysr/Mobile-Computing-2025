package com.myjoyy.composetutorial2.viewModels

import androidx.lifecycle.LiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myjoyy.composetutorial2.db.User
import com.myjoyy.composetutorial2.db.UserDao
import kotlinx.coroutines.launch
//https://developer.android.com/topic/libraries/architecture/coroutines
class UserViewModel(private val userDao: UserDao) : ViewModel() {
    val user: LiveData<User?> = userDao.getUser()

    fun updateUser(newUser: User){
        viewModelScope.launch{
            userDao.updateUser(newUser)
        }
    }
}

