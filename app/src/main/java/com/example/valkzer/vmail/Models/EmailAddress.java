package com.example.valkzer.vmail.Models;

import android.os.Build;
import android.os.AsyncTask;
import android.content.Context;

import com.example.valkzer.vmail.Util.AzureResource;
import com.example.valkzer.vmail.Util.EventListener;
import com.example.valkzer.vmail.Util.AzureWebServicesHelper;

import java.util.List;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

public class EmailAddress extends AzureResource {

    private String id;
    private String emailAddress;

    public EmailAddress() {
    }

    public EmailAddress(String id, String email) {
        this.id = id;
        this.emailAddress = email;
    }

    public String getId() {
        return id;
    }

    public EmailAddress setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public EmailAddress setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public void register(final Context context, final Runnable runnable) throws ExecutionException, InterruptedException, MalformedURLException {

        final EmailAddress emailAddress = this;
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final EmailAddress newEmailAddress = AzureWebServicesHelper.getEmailAddressTable(context).insert(emailAddress).get();
                    setId(newEmailAddress.getId());
                    runnable.run();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


                return null;
            }
        };
        runAsyncTask(task);
    }

}
