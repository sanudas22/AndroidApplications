package com.example.group1_42_ic09;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayMail extends AppCompatActivity {

    private Button finish;
    private TextView subject, sender, message, createdat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mail);

        setTitle("Mail");
        finish = findViewById(R.id.finish);
        subject = findViewById(R.id.subject);
        sender = findViewById(R.id.senderName);
        message = findViewById(R.id.messageDesc);
        createdat = findViewById(R.id.createdat);

        if(getIntent() != null && getIntent().getExtras() != null) {
            Email email = (Email) getIntent().getSerializableExtra(InboxRecyclerAdapter.MyViewHolder.EMAIL);
            subject.setText("Subject : "+email.subjectbody);
            sender.setText("Email : "+email.senderName);
            message.setText("Message : "+email.message);

        }

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
