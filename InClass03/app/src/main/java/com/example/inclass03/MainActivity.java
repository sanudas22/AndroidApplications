package com.example.inclass03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.lang.reflect.Array;
import java.util.List;

/**
 * a. Assignment #4
 * b. File Name.: inClass03
 * c. Full name of students of Group 20.: SANU DAS, AKHIL CHUNDARATHIL, RAVI THEJA GOALLA
 */
public class MainActivity extends AppCompatActivity {

    RadioButton rb_female;
    RadioButton rb_male;
    RadioGroup rb_gender;
    ImageView iv_gender;
    Button button_save;
    EditText et_firstname;
    EditText et_lastname;
    static public String USER_KEY = "user";
    final static int REQ_CODE = 1001;
    final static String RESULT_KEY = "result";
    String fname;
    String lname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("My Profile Builder");

        rb_gender = findViewById(R.id.rb_gender);
        rb_female = findViewById(R.id.rb_female);
        rb_male = findViewById(R.id.rb_male);
        iv_gender = findViewById(R.id.iv_gender);
        et_firstname = findViewById(R.id.et_firstname);
        et_lastname = findViewById(R.id.et_lastname);
        button_save = findViewById(R.id.button_save);
        final String[] flag_image = {""};

        rb_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rb_female:
                        iv_gender.setImageDrawable(getDrawable(R.drawable.female));
                        flag_image[0] = "Female";
                        break;
                    case R.id.rb_male:
                        iv_gender.setImageDrawable(getDrawable(R.drawable.male));
                        flag_image[0] = "Male";
                        break;
                    default:
                        break;
                }
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_firstname.getText().toString().equals("")) {
                    fname = et_firstname.getText().toString();
                } else {
                    et_firstname.setError("Hey, I need a value!");
                    return;
                }
                if(!et_lastname.getText().toString().equals("")){
                    lname = et_lastname.getText().toString();
                } else {
                    et_lastname.setError("Hey, I need a value!");
                    return;
                }
                Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                User user = new User(fname, lname, flag_image[0]);
                intent.putExtra(USER_KEY,user);
                startActivityForResult(intent, REQ_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQ_CODE) {
            if(requestCode == RESULT_OK && data.getExtras().containsKey(RESULT_KEY)) {
                User user = (User) data.getExtras().getSerializable(RESULT_KEY);
                et_firstname.setText(user.firstName);
                et_lastname.setText(user.lastName);
                if(user.gender.equals("Female")) {
                    iv_gender.setImageDrawable(getDrawable(R.drawable.female));
                    rb_female.setChecked(true);
                } else if(user.gender.equals("Male")) {
                    iv_gender.setImageDrawable(getDrawable(R.drawable.male));
                    rb_male.setChecked(true);
                }
            }
        }
    }


}
