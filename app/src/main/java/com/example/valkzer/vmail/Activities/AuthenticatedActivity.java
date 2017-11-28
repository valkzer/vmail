package com.example.valkzer.vmail.Activities;

import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.example.valkzer.vmail.R;
import com.example.valkzer.vmail.Models.EmailAddress;

public abstract class AuthenticatedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mail);
        setUpTitle();
    }

    protected EmailAddress getCurrentEmailAddress() {
        SharedPreferences prefs = getSharedPreferences(MainActivity.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        String emailAddress = prefs.getString(MainActivity.EMAIL_ADDRESS, null);
        String id = prefs.getString(MainActivity.EMAIL_ADDRESS, null);
        return new EmailAddress(id, emailAddress);
    }

    protected void setUpTitle() {
        String emailAddress = getCurrentEmailAddress().getEmail();
        this.setTitle(emailAddress != null ? emailAddress : "UNREAD EMAILS");
    }
}
