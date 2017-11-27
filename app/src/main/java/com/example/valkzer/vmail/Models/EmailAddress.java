package com.example.valkzer.vmail.Models;

import android.os.Build;
import android.os.AsyncTask;
import android.content.Context;

import com.example.valkzer.vmail.Util.AzureWebServicesHelper;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

public class EmailAddress {

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

    public void register(final Context context,final Runnable runnable) throws ExecutionException, InterruptedException, MalformedURLException {

        final EmailAddress emailAddress = this;
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final EmailAddress newEmailAddress = addItemInTable(emailAddress, context);
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

    private EmailAddress addItemInTable(EmailAddress item, Context context) throws ExecutionException, InterruptedException, MalformedURLException {
        return AzureWebServicesHelper.getEmailAddressTable(context).insert(item).get();
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }
}
