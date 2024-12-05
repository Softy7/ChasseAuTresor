package com.example.Chasse.Model.SQLite;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import com.example.Chasse.Model.Message;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM messages")
    LiveData<List<Message>> getAllMessagesLive();

    @Insert
    void addMessage(Message message);

    @Query("DELETE FROM messages")
    void clearMessages();
}


