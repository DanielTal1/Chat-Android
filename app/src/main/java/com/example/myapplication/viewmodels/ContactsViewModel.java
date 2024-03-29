package com.example.myapplication.viewmodels;

import static com.example.myapplication.MyApplication.context;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.api.Api;
//import com.example.myapplication.api.ContactApi;
import com.example.myapplication.entities.Contact;
import com.example.myapplication.repositories.ContactsRepository;

import java.util.List;

public class ContactsViewModel extends AndroidViewModel {

    private ContactsRepository mRepository;
    private String user;
    private LiveData<List<Contact>> contacts;

    public ContactsViewModel (Application application) {
        super(application);
    }

    public void init(Context context,String user, String server) {
        setUser(user);
        mRepository = new ContactsRepository(context, user, server);
        contacts = mRepository.getAll();
    }

    public LiveData<List<Contact>> get() {
        return contacts;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void getListFromSource() {
        mRepository.getSourceListTodb();
    }
}