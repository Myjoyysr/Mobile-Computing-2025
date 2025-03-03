package com.myjoyy.composetutorial2.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao{
    @Query("Select * FROM User WHERE uid = 1")
    fun getUser(): LiveData<User?>

    @Query("Select * FROM User WHERE uid = 1")
    fun getUserOne(): User?

    @Update
    fun updateUser(user: User)

    @Insert
    fun insertUser(user: User)
}