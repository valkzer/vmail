package com.example.valkzer.vmail;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.content.Context;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.content.SharedPreferences;
import android.widget.AutoCompleteTextView;
import android.support.v7.app.AppCompatActivity;

import com.example.valkzer.vmail.Models.Mail;
import com.example.valkzer.vmail.Util.EventListener;
import com.example.valkzer.vmail.Models.EmailAddress;

import java.util.List;
import java.util.ArrayList;

public class CreateMailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mail);
        fillToWithEmailAddressOptions();
    }

    private void fillToWithEmailAddressOptions() {
        final EmailAddress emailAddress = new EmailAddress();
        emailAddress.getAllEmailAddresses(getApplicationContext(), new EventListener() {
            @Override
            public void triggerEvent(final Object object) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<EmailAddress> emailAddresses = (List<EmailAddress>) object;
                        ArrayList<String> emailAddressList = new ArrayList<String>();

                        for (EmailAddress emailAddress : emailAddresses) {
                            if (emailAddress.getEmailAddress() != null) {
                                emailAddressList.add(emailAddress.getEmailAddress());
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item, emailAddressList);
                        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.txtTo);
                        actv.setThreshold(1);
                        actv.setAdapter(adapter);
                    }
                });
            }
        });
    }

    private EmailAddress getCurrentEmailAddress() {
        SharedPreferences prefs = getSharedPreferences(MainActivity.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        String emailAddress = prefs.getString(MainActivity.EMAIL_ADDRESS, null);
        String id = prefs.getString(MainActivity.EMAIL_ADDRESS, null);
        return new EmailAddress(id, emailAddress);
    }

    public void sendMail(View view) {

        String body = ((TextView) findViewById(R.id.txtBody)).getText().toString();
        String subject = ((TextView) findViewById(R.id.txtSubject)).getText().toString();
        String to = ((AutoCompleteTextView) findViewById(R.id.txtTo)).getText().toString();
        String from = getCurrentEmailAddress().getEmailAddress();
        Mail mail = new Mail();
        try {
            mail.setBody(body)
                    .setFrom(from)
                    .setSubject(subject)
                    .setTo(to)
                    .send(getApplicationContext(), new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Mail has been sent", Toast.LENGTH_SHORT).show();
                                    Intent myIntent = new Intent(getApplicationContext(), UnreadMailsActivity.class);
                                    startActivity(myIntent);
                                }
                            });
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Unable to send mail", Toast.LENGTH_SHORT).show();
        }


    }
}
