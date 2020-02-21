package com.example.group1_42_ic09;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ComposeEmailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String token;
    private EditText subjectBody, messageBody;
    private Button send, cancel;
    private final OkHttpClient client = new OkHttpClient();
    private Spinner spinner;
    ArrayList users = new ArrayList(){{
        add(0,"User");
    }};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_email);

        setTitle("Create New Email");
        subjectBody = findViewById(R.id.subjectBody);
        messageBody = findViewById(R.id.messageBody);
        send = findViewById(R.id.send);
        cancel = findViewById(R.id.cancel);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        if(getIntent() != null && getIntent().getExtras() != null) {
            token = getIntent().getStringExtra(InboxActivity.TOKEN);
            Request request = new Request.Builder()
                    .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/users")
                    .header("Authorization", "BEARER "+token)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    ResponseBody responseBody = response.body();
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    try {
                        JSONObject root = new JSONObject(responseBody.string());
                        JSONArray jsonArray = root.getJSONArray("users");
                        for (int i = 0; i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            User user = new User();
                            user.name = jsonObject.getString("fname")+" "+jsonObject.getString("lname");
                            user.id = jsonObject.getString("id");
                            users.add(user);
                        }
                       ComposeEmailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                createspinner(users);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        /*ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(userAdapter);
*/
        //userAdapter.notifyDataSetChanged();



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItem().toString() == "User") {
                    Toast.makeText(ComposeEmailActivity.this, "Please select valid user.", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestBody formBody = new FormBody.Builder()
                        .add("receiver_id", ((User)spinner.getSelectedItem()).id)
                        .add("subject", subjectBody.getText().toString())
                        .add("message", messageBody.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox/add")
                        .header("Authorization", "BEARER "+token)
                        .header("Content-Type","application/x-www-form-urlencoded")
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        ResponseBody responseBody = response.body();

                        ComposeEmailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ComposeEmailActivity.this, "Message is sent.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                        Headers responseHeaders = response.headers();
                        for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
   void createspinner(ArrayList userslist){

        ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userslist){
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

            @Override
            public boolean isEnabled(int position) {
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
        };

        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(userAdapter);

    }
}
