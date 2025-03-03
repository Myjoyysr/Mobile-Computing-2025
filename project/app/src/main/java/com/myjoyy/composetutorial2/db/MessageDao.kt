package com.myjoyy.composetutorial2.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Query("SELECT * FROM dbMessage")
    fun getMessages(): LiveData<List<dbMessage>>

    @Query("Select * FROM dbMessage WHERE id = 1")
    fun getMessageOne(): dbMessage

    @Insert
    fun insertMessage(message: dbMessage)

    @Query("DELETE FROM dbMessage WHERE id = :messageId")
    fun deleteMessage(messageId: Int)
}

