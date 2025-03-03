package com.myjoyy.composetutorial2.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myjoyy.composetutorial2.db.MessageDao
import com.myjoyy.composetutorial2.db.UserDao

//https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories
class UserFactory(private val userDao: UserDao, private val messageDao: MessageDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(userDao, messageDao) as T
    }
}