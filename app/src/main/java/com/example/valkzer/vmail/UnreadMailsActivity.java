package com.example.valkzer.vmail;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.content.Intent;
import android.content.Context;
import android.widget.ListView;
import android.view.MenuInflater;
import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;

import com.example.valkzer.vmail.Models.Mail;
import com.example.valkzer.vmail.Util.EventListener;
import com.example.valkzer.vmail.Models.EmailAddress;
import com.example.valkzer.vmail.Adapters.MailListAdapter;

import java.util.List;

public class UnreadMailsActivity extends AppCompatActivity {

    private MailListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unread_mails);
        setUpMenus();
        setUpTitle();
        loadUnreadMails();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUnreadMails();
    }

    private void loadUnreadMails() {
        mAdapter = new MailListAdapter(this, R.layout.mail_list_item);
        ListView listViewToDo = (ListView) findViewById(R.id.mail_list);
        listViewToDo.setAdapter(mAdapter);

        Mail mail = new Mail();
        mail.getAllEmailsUnreadForEmailAddress(getApplicationContext(), getCurrentEmailAddress(), new EventListener() {
            @Override
            public void triggerEvent(Object object) {
                final List<Mail> mails = (List<Mail>) object;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();

                        for (Mail mail : mails) {
                            mAdapter.add(mail);
                        }
                    }
                });
            }
        });
    }

    public void openCreateEmailAddressActivity(MenuItem item) {
        Intent myIntent = new Intent(UnreadMailsActivity.this, CreateEmailAddressActivity.class);
        UnreadMailsActivity.this.startActivity(myIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.unread_mails_menu, menu);
        return true;
    }

    private EmailAddress getCurrentEmailAddress() {
        SharedPreferences prefs = getSharedPreferences(MainActivity.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        String emailAddress = prefs.getString(MainActivity.EMAIL_ADDRESS, null);
        String id = prefs.getString(MainActivity.EMAIL_ADDRESS, null);
        return new EmailAddress(id, emailAddress);
    }

    private void setUpMenus() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(UnreadMailsActivity.this, CreateMailActivity.class);
                UnreadMailsActivity.this.startActivity(myIntent);
            }
        });
    }

    private void setUpTitle() {
        String emailAddress = getCurrentEmailAddress().getEmailAddress();
        this.setTitle(emailAddress != null ? emailAddress : "UNREAD EMAILS");
    }
}
