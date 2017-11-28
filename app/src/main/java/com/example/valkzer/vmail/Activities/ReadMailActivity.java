package com.example.valkzer.vmail.Activities;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import com.google.gson.Gson;
import com.example.valkzer.vmail.R;
import com.example.valkzer.vmail.Models.Mail;

public class ReadMailActivity extends AuthenticatedActivity {

    private Mail mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_mail);
        loadMailInformation();
        setUpTitle();

    }

    private void loadMailInformation() {
        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("mail");
        Mail mail = gson.fromJson(strObj, Mail.class);
        this.mail = mail;

        ((TextView) findViewById(R.id.txtBody)).setText((mail.getMessage() != null ? mail.getMessage() : ""));
        ((TextView) findViewById(R.id.txtFrom)).setText(mail.getFrom());
        ((TextView) findViewById(R.id.txtSubject)).setText(mail.getSubject());
    }

    public void createReply(View view) {
        Gson gson = new Gson();
        String encodedMail = gson.toJson(mail);
        Intent myIntent = new Intent(getApplicationContext(), CreateMailActivity.class);
        myIntent.putExtra("replyToMail", encodedMail);
        startActivity(myIntent);
    }

}
