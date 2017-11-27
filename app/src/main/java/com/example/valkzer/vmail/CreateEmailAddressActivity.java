package com.example.valkzer.vmail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.valkzer.vmail.Models.EmailAddress;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

public class CreateEmailAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_email_address);
    }

    public void registerEmailAddress(View view) {
        String emailAddress = ((TextView) findViewById(R.id.txtEmailAddress)).getText().toString();
        final EmailAddress newEmailAddress = new EmailAddress();
        newEmailAddress.setEmailAddress(emailAddress);
        try {
            newEmailAddress.register(this, new Runnable() {
                @Override
                public void run() {
                    setRegisteredEmailAddress(newEmailAddress);
                    openUnreadEmailsActivity();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openUnreadEmailsActivity() {
        Intent myIntent = new Intent(CreateEmailAddressActivity.this, UnreadEmailsActivity.class);
        CreateEmailAddressActivity.this.startActivity(myIntent);
    }

    private void setRegisteredEmailAddress(EmailAddress emailAddress) {
        SharedPreferences prefs = getSharedPreferences(MainActivity.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(MainActivity.EMAIL_ID, emailAddress.getId());
        editor.putString(MainActivity.EMAIL_ADDRESS, emailAddress.getEmailAddress());
        editor.commit();
    }
}