package com.example.intentpractise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main2Activity extends AppCompatActivity {

    static String USER_KEY;
    static String PUSER;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                User user = new User("Bob",26);
                //intent.putExtra(USER_KEY, user);
                intent.putExtra(PUSER, new ParcellableUser("RED", 293.67));
                startActivity(intent);

            }
        });



    }
}
