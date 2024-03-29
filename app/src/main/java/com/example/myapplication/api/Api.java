package com.example.myapplication.api;
import android.util.Log;
import android.widget.TextView;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.adapters.ContactsListAdapter;
import com.example.myapplication.entities.Contact;
import com.example.myapplication.entities.Message;
import com.example.myapplication.entities.User;
import com.example.myapplication.repositories.ContactsRepository;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

public class Api {
    Retrofit retrofit;
    WebServiceApi webServiceApi;

    public Api(String server){
        if(server.startsWith("10.0.2.2")) {
            String url = "http://" + server + "/api/";
            retrofit=new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
            webServiceApi = retrofit.create(WebServiceApi.class);
    }

    public void sendToken(HashMap<String, String> data){
        Call<Void> call=webServiceApi.sendToken(data);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                call.cancel();
            }


        });
    }

    public void checkLogin(HashMap<String, String> data, final MyCallBack mycallback){
        Call<User> call=webServiceApi.isUser(data);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                mycallback.onResponseUser(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                call.cancel();
            }


        });
    }

    public void RegisterUser(User user, final MyCallbackVoid mycallback){
        Call<Void> call=webServiceApi.createUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mycallback.onResponse(response.code() == 201);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                call.cancel();
            }


        });
    }

    public void inviteContact(HashMap<String, String> data, final MyIntegerCallBack mycallback){
        String url="http://"+data.get("server")+"/api/invitation/AddContact";
        Call<Void> call = webServiceApi.inviteContact(url,data);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code()==404){
                    mycallback.onResponse(0);
                } else if(response.code()==400){
                    mycallback.onResponse(1);
                }
                else{
                    mycallback.onResponse(2);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mycallback.onResponse(3);
                call.cancel();
            }
        });
    }



    public void getContacts(String user,MyCallBackContactsList myCallBackContactsList) {
        Call<List<Contact>> call = webServiceApi.getContacts(user);
        call.enqueue(new Callback<List<Contact>>() {

            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                if(response.code()!=404){
                    myCallBackContactsList.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                call.cancel();
            }
        });
    }


    public void addContact(HashMap<String, String> data, final MyIntegerCallBack mycallback){
        Call<Void> call = webServiceApi.addContact(data);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code()==404){
                    mycallback.onResponse(0);
                } else if(response.code()==400){
                    mycallback.onResponse(1);
                }
                mycallback.onResponse(2);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void getMessages(String user, String contact, MyCallbackMessagesList myCallBackMessagesList) {
        Call<List<Message>> call = webServiceApi.getMessages(contact, user);
        call.enqueue(new Callback<List<Message>>() {

            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if(response.code()!=404){
                    myCallBackMessagesList.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void postMessage(String user, String contact, Message message, MyCallbackVoid myCallbackVoid) {
        HashMap<String, String> data_user = new HashMap<String, String>() {{
            put("content", message.getContent());
            put("user", user);
        }};

        Call<Void> from = webServiceApi.createMessage(contact, data_user);
        from.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code()!=404){
                    myCallbackVoid.onResponse(true);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void transfer(String user, String contact,String server, Message message, MyCallbackVoid myCallbackVoid) {
        HashMap<String, String> data = new HashMap<String, String>() {{
            put("from", user);
            put("to", contact);
            put("content", message.getContent());
        }};
        String newServer=server;
        String[] parts=server.split(":");
        if(parts[0].equals("localhost")){
            newServer="10.0.2.2:"+parts[1];
        }
        String url="http://"+newServer+"/api/transfer";

        Call<Void> call = webServiceApi.transferMsg(url,data);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code()!=404){
                    myCallbackVoid.onResponse(true);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
