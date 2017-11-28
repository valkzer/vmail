package com.example.valkzer.vmail;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.example.valkzer.vmail.Models.Mail;
import com.example.valkzer.vmail.Models.EmailAddress;

public class ReadMailActivity extends AppCompatActivity {

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

    private void setUpTitle() {
        String emailAddress = getCurrentEmailAddress().getEmail();
        this.setTitle(emailAddress != null ? emailAddress : "UNREAD EMAILS");
    }

    private EmailAddress getCurrentEmailAddress() {
        SharedPreferences prefs = getSharedPreferences(MainActivity.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        String emailAddress = prefs.getString(MainActivity.EMAIL_ADDRESS, null);
        String id = prefs.getString(MainActivity.EMAIL_ADDRESS, null);
        return new EmailAddress(id, emailAddress);
    }


}
