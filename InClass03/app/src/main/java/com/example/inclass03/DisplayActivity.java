package com.example.inclass03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {
    private ImageView iv_profile;
    private Button button_edit;
    private TextView tv_name;
    private TextView tv_gender;
    static public String USER_EDIT_KEY = "user_edit";
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        setTitle("My Profile Builder");

        iv_profile = findViewById(R.id.iv_profile);
        button_edit = findViewById(R.id.button_edit);
        tv_name = findViewById(R.id.tv_name);
        tv_gender = findViewById(R.id.tv_gender);

        if(getIntent() != null && getIntent().getExtras() != null) {
           user = (User) getIntent().getExtras().getSerializable(MainActivity.USER_KEY);
           if(user.gender.equals("Female")) {
               iv_profile.setImageDrawable(getDrawable(R.drawable.female));
           } else if(user.gender.equals("Male")) {
               iv_profile.setImageDrawable(getDrawable(R.drawable.male));
           }
           tv_name.setText("Name: " + user.firstName + " " + user.lastName);
           tv_gender.setText(user.gender);

        }

        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.RESULT_KEY,user);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }


}
