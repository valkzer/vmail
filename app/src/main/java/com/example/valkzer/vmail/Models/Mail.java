package com.example.valkzer.vmail.Models;

import android.os.AsyncTask;
import android.content.Context;

import com.example.valkzer.vmail.Util.AzureResource;
import com.example.valkzer.vmail.Util.AzureWebServicesHelper;
import com.example.valkzer.vmail.Util.EventListener;

import java.util.List;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;


public class Mail extends AzureResource {
    private String id;
    private String subject;
    private String to;
    private String from;
    private String body;
    private Boolean read;

    public Mail() {
        this.read = false;
    }

    public Mail(String id, String subject, String to, String from, String body, Boolean read) {
        this.id = id;
        this.subject = subject;
        this.to = to;
        this.from = from;
        this.body = body;
        this.read = read;
    }

    public String getId() {
        return id;
    }

    public Mail setId(String id) {
        this.id = id;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Mail setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getTo() {
        return to;
    }

    public Mail setTo(String to) {
        this.to = to;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public Mail setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Mail setBody(String body) {
        this.body = body;
        return this;
    }

    public Boolean getRead() {
        return read;
    }

    public Mail setRead(Boolean read) {
        this.read = read;
        return this;
    }

    public void getAllEmailsUnreadForEmailAddress(final Context context, final EmailAddress emailAddress, final EventListener listener) {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                final List<Mail> mails;
                try {
                    mails = AzureWebServicesHelper.getMailsTable(context).where()
                            .field("To").eq(val(emailAddress.getId())).and()
                            .field("Read").eq(val(false))
                            .execute().get();

                    listener.triggerEvent(mails);

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

    public void send(final Context context, final Runnable runnable) throws ExecutionException, InterruptedException, MalformedURLException {

        final Mail mail = this;
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final Mail newMail = AzureWebServicesHelper.getMailsTable(context).insert(mail).get();
                    setId(newMail.getId());
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
}
