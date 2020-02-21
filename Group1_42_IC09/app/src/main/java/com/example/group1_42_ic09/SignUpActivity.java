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
import java.sql.SQLOutput;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstname, lastname, emailId, choosepwd, repeatpwd;
    private Button signUp, cancel;
    private final OkHttpClient client = new OkHttpClient();
    SharedPreferences sharedPreferences;
    public static String SHAREDPREF_SIGNUP = "sharedPreferences";
    public static String TOKEN = "token", TOKEN_INTENT = "tokenIntent", FNAME = "fname", LNAME = "lname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign Up");
        firstname = findViewById(R.id.fname);
        lastname = findViewById(R.id.lname);
        emailId = findViewById(R.id.emailSignUp);
        choosepwd = findViewById(R.id.choosepwd);
        repeatpwd = findViewById(R.id.repeatpwd);
        signUp = findViewById(R.id.bt_signup);
        cancel = findViewById(R.id.bt_cancel);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstname.getText().toString() == null || lastname.getText().toString() == null
                        || emailId.getText().toString() == null || choosepwd.getText().toString() == null
                        || repeatpwd.getText().toString() == null)
                {
                    Toast.makeText(SignUpActivity.this, "All fields are mandatory!! Please enter the missing values.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String choosepassword = choosepwd.getText().toString();
                String repeatpassword = repeatpwd.getText().toString();
                if(!choosepassword.equals(repeatpassword)) {
                    repeatpwd.setError("Incorrect password");
                    return;
                }

                RequestBody formBody = new FormBody.Builder()
                        .add("email", emailId.getText().toString())
                        .add("password", choosepassword)
                        .add("fname", firstname.getText().toString())
                        .add("lname", lastname.getText().toString())
                        .build();

                Request request = new Request.Builder()
                        .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/signup")
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        Toast.makeText(SignUpActivity.this, "SignUp was not successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override public void onResponse(Call call, final Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful()) {
                                SignUpActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SignUpActivity.this, "SignUp was Unsuccessful!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //throw new IOException("Unexpected code " + response);
                            }
                            final JSONObject root = new JSONObject(responseBody.string());
                            if (root.getString("status").equals("error")) {
                                SignUpActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Toast.makeText(SignUpActivity.this, root.getString("message"), Toast.LENGTH_SHORT).show();
                                            return;
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                           else {
                                SignUpActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SignUpActivity.this, "User has been created.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                sharedPreferences = getSharedPreferences(SHAREDPREF_SIGNUP, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                String fname = root.getString("user_fname");
                                String lname = root.getString("user_lname");
                                String token = root.getString("token");
                                editor.putString(TOKEN, token);
                                editor.putString("first", fname);
                                editor.putString("last", lname);
                                editor.commit();
                                //System.out.println(token);

                                Intent intent = new Intent(SignUpActivity.this, InboxActivity.class);
                                intent.putExtra(TOKEN_INTENT, token);
                                intent.putExtra(FNAME, fname);
                                intent.putExtra(LNAME, lname);
                                startActivity(intent);
                                finish();
                                //System.out.println(responseBody.string());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
