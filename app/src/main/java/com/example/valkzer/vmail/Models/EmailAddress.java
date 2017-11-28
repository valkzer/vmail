package com.example.valkzer.vmail.Models;

import android.os.AsyncTask;
import android.content.Context;

import com.example.valkzer.vmail.Util.AzureResource;
import com.example.valkzer.vmail.Util.EventListener;
import com.example.valkzer.vmail.Util.AzureWebServicesHelper;

import java.util.List;

public class EmailAddress extends AzureResource {

    private String id;
    private String email;

    public EmailAddress() {
    }

    public EmailAddress(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public EmailAddress setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public EmailAddress setEmail(String email) {
        this.email = email;
        return this;
    }

    public void register(final Context context, final Runnable runnable) {

        final EmailAddress emailAddress = this;
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final EmailAddress newEmailAddress = AzureWebServicesHelper.getEmailAddressTable(context).insert(emailAddress).get();
                    setId(newEmailAddress.getId());
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        runAsyncTask(task);
    }

    public void getAllEmailAddresses(final Context context, final EventListener listener) {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                final List<EmailAddress> emailAddresses;
                try {
                    emailAddresses = AzureWebServicesHelper.getEmailAddressTable(context).where().execute().get();
                    listener.triggerEvent(emailAddresses);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return null;
            }
        };
        runAsyncTask(task);
    }

}
