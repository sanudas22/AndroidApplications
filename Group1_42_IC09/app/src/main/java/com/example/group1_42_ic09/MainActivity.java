package com.example.group1_42_ic09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * a. In Class 09
 * b. File Name.: Group1_42_IC09
 * c. Full name of students of Group1 42.: SANU DAS
 */
public class MainActivity extends AppCompatActivity {
    private EditText email, password;
    private Button login, signup;
    public String emailValue, passwordValue;
    private final OkHttpClient client = new OkHttpClient();
    public SharedPreferences sharedPreferences;
    public static String SHAREDPREF = "sharedPreferences";
    public static String TOKEN = "token", TOKEN_INTENT = "tokenIntent", FNAME = "fname", LNAME = "lname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Mailer");

        sharedPreferences = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        if(sharedPreferences.contains(TOKEN)) {
            Intent intent = new Intent(MainActivity.this, InboxActivity.class);
            String t = sharedPreferences.getString(TOKEN, "");
            String fname =sharedPreferences.getString("first", "");
            String lname =sharedPreferences.getString("last", "");
            intent.putExtra(TOKEN_INTENT, t);
            intent.putExtra(FNAME, fname);
            intent.putExtra(LNAME, lname);
            startActivity(intent);
        }
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signUpMain);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString() == null || password.getText().toString() == null)
                {
                    Toast.makeText(MainActivity.this, "All fields are mandatory!! Please enter the missing values.", Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestBody formBody = new FormBody.Builder()
                        .add("email", email.getText().toString())
                        .add("password", password.getText().toString())
                        .build();

                Request request = new Request.Builder()
                        .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/login")
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Login was not successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful()){
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Login was Unsuccessful!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            final JSONObject root = new JSONObject(responseBody.string());
                            if (root.getString("status").equals("error")) {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Toast.makeText(MainActivity.this, root.getString("message"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                                sharedPreferences = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                String fname = root.getString("user_fname");
                                String lname = root.getString("user_lname");
                                String token = root.getString("token");
                                editor.putString(TOKEN, token);
                                editor.putString("first", fname);
                                editor.putString("last", lname);
                                editor.commit();
                                System.out.println(token);
                            Intent intent = new Intent(MainActivity.this, InboxActivity.class);
                            intent.putExtra(TOKEN_INTENT, token);
                            intent.putExtra(FNAME, fname);
                            intent.putExtra(LNAME, lname);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
