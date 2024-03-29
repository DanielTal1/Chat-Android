package com.example.myapplication.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.entities.Contact;
import com.example.myapplication.entities.ContactWithMessages;
import com.example.myapplication.entities.Message;
import com.example.myapplication.repositories.ContactsRepository;

import java.util.List;

public class MessagesViewModel extends AndroidViewModel {
    private ContactsRepository mRepository;

    private LiveData<List<ContactWithMessages>> messages;

    public MessagesViewModel (Application application) {
    super(application);
    }

    public void init(String user,Context context, String server) {
        mRepository = new ContactsRepository(context,user, server);
        messages = mRepository.getAllMessages();
    }

    public LiveData<List<ContactWithMessages>> getMessages() {
        return messages;
    }

    public void getListFromSource() {
        mRepository.getSourceListTodb();
    }
}