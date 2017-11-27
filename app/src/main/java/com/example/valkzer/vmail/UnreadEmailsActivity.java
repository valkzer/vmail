package com.example.valkzer.vmail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class UnreadEmailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unread_emails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String emailAddress = getCurrentEmailAddress();
        this.setTitle(emailAddress != null ? emailAddress : "UNREAD EMAILS");
    }

    private String getCurrentEmailAddress() {
        SharedPreferences prefs = getSharedPreferences(MainActivity.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        return prefs.getString(MainActivity.EMAIL_ADDRESS, null);
    }

}
