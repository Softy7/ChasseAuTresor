package com.example.Chasse.Model.SQLite;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.Chasse.Model.Message;


@Database(entities = {Message.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {
    private static MessageDatabase instance;

    public abstract MessageDao messageDao();

    public static synchronized MessageDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MessageDatabase.class, "chat.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

