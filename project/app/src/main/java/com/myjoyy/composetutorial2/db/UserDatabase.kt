package com.myjoyy.composetutorial2.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, dbMessage::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
}