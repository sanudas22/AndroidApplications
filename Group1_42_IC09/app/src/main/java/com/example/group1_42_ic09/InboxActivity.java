package com.example.group1_42_ic09;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class InboxActivity extends AppCompatActivity {

    private TextView username;
    private ImageView compose, logout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public static final OkHttpClient client = new OkHttpClient();
    public static String TOKEN;
    public static String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        setTitle("Inbox");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        username = findViewById(R.id.username);
        compose = findViewById(R.id.compose);
        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(MainActivity.SHAREDPREF, 0).edit().clear().commit();
                Intent intent = new Intent(InboxActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        compose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InboxActivity.this, ComposeEmailActivity.class);
                intent.putExtra(TOKEN, token);
                startActivity(intent);
            }
        });

        if(getIntent() != null && getIntent().getExtras() != null) {
            token = getIntent().getStringExtra(MainActivity.TOKEN_INTENT);
            String fname = getIntent().getStringExtra(MainActivity.FNAME);
            String lname = getIntent().getStringExtra(MainActivity.LNAME);
            username.setText(fname+" "+lname);
                Request request = new Request.Builder()
                        .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox")
                        .header("Authorization", "BEARER "+token)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                            Headers responseHeaders = response.headers();
                            for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                            }
                            JSONObject root = new JSONObject(responseBody.string());
                            final JSONArray messages = root.getJSONArray("messages");
                            InboxActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter = new InboxRecyclerAdapter(InboxActivity.this, messages);
                                    recyclerView.setAdapter(mAdapter);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        }
    }
}
