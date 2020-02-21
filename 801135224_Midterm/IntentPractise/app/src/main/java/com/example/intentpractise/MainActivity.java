package com.example.intentpractise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http:/www.uncc.edu"));
                startActivity(intent);
            }
        });

        findViewById(R.id.button_goto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.intentpractise.intent.action.VIEW");
                startActivity(intent);
            }
        });
        if(getIntent() != null && getIntent().getExtras() != null) {
            /*User user = (User) getIntent().getExtras().getSerializable(Main2Activity.USER_KEY);
            Toast.makeText(this,"User details: " + user.toString(), Toast.LENGTH_SHORT).show();
*/
            ParcellableUser puser = getIntent().getExtras().getParcelable(Main2Activity.PUSER);
            Toast.makeText(this, puser.toString(), Toast.LENGTH_LONG).show();
        }

    }
}
