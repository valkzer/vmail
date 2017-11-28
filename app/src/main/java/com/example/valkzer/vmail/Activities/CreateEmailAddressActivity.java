package com.example.valkzer.vmail.Activities;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.example.valkzer.vmail.R;
import com.example.valkzer.vmail.Models.EmailAddress;

public class CreateEmailAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_email_address);
    }

    public void registerEmailAddress(View view) {
        String emailAddress = ((TextView) findViewById(R.id.txtEmailAddress)).getText().toString();
        final EmailAddress newEmailAddress = new EmailAddress();
        newEmailAddress.setEmail(emailAddress);
        newEmailAddress.register(getApplicationContext(), new Runnable() {
            @Override
            public void run() {
                setRegisteredEmailAddress(newEmailAddress);
                openUnreadEmailsActivity();
            }
        });
    }

    private void openUnreadEmailsActivity() {
        Intent myIntent = new Intent(CreateEmailAddressActivity.this, UnreadMailsActivity.class);
        CreateEmailAddressActivity.this.startActivity(myIntent);
    }

    private void setRegisteredEmailAddress(EmailAddress emailAddress) {
        SharedPreferences prefs = getSharedPreferences(MainActivity.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(MainActivity.EMAIL_ID, emailAddress.getId());
        editor.putString(MainActivity.EMAIL_ADDRESS, emailAddress.getEmail());
        editor.commit();
    }
}
