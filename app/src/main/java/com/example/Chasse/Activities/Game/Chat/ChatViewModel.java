package com.example.Chasse.Activities.Game.Chat;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.Chasse.Model.Message;
import com.example.Chasse.Model.SQLite.MessageDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private final LiveData<List<Message>> messages;
    private final MessageDatabase messageDatabase;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        messageDatabase = MessageDatabase.getInstance(application);
        messages = messageDatabase.messageDao().getAllMessagesLive();
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }
}



