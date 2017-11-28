package com.example.valkzer.vmail.Activities;

import android.view.View;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.app.AlertDialog;
import android.widget.ProgressBar;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.net.MalformedURLException;

import com.example.valkzer.vmail.R;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.SettableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import com.example.valkzer.vmail.Util.AzureWebServicesHelper;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;


public class MainActivity extends AppCompatActivity {

    /**
     * Mobile Service Client reference
     */
    private MobileServiceClient mClient;

    /**
     * Progress spinner to use for table operations
     */
    private ProgressBar mProgressBar;

    public static final String SHARED_PREFERENCE_FILE = "temp";
    public static final String USER_ID_PREFERENCE = "uid";
    public static final String TOKEN_PREFERENCE = "token";
    public static final String EMAIL_ID = "email_id";
    public static final String EMAIL_ADDRESS = "email_address";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {

            mClient = AzureWebServicesHelper.getMobileServiceClient(this).withFilter(new ProgressFilter());
            this.authenticate();

        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e) {
            createAndShowDialog(e, "Error");
        }
    }

    private void openHomeScreen() {

        findViewById(R.id.btnAuthenticate).setVisibility(View.INVISIBLE);
        Intent myIntent;

        if (checkHasRegisteredEmailAddress()) {
            myIntent = new Intent(MainActivity.this, UnreadMailsActivity.class);
        } else {
            myIntent = new Intent(MainActivity.this, CreateEmailAddressActivity.class);
        }

        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        MainActivity.this.startActivity(myIntent);
    }

    private boolean checkHasRegisteredEmailAddress() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        String emailAddress = prefs.getString(EMAIL_ADDRESS, null);
        String emailId = prefs.getString(EMAIL_ID, null);
        return (emailAddress != null && emailId != null);
    }

    private void authenticate() {
        if (loadUserTokenCache(mClient)) {
            openHomeScreen();
        } else {
            ListenableFuture<MobileServiceUser> mLogin =
                    mClient.login(MobileServiceAuthenticationProvider.Facebook);
            Futures.addCallback(mLogin, new FutureCallback<MobileServiceUser>() {
                @Override
                public void onFailure(@NonNull Throwable exc) {
                    findViewById(R.id.btnAuthenticate).setVisibility(View.VISIBLE);
                }

                @Override
                public void onSuccess(MobileServiceUser user) {
                    cacheUserToken(mClient.getCurrentUser());
                    try {
                        openHomeScreen();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public boolean loadUserTokenCache(MobileServiceClient client) {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        String userId = prefs.getString(USER_ID_PREFERENCE, null);
        if (userId == null)
            return false;
        String token = prefs.getString(TOKEN_PREFERENCE, null);
        if (token == null)
            return false;
        AzureWebServicesHelper.setAuth(client, userId, token);
        return true;
    }

    private void cacheUserToken(MobileServiceUser user) {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_ID_PREFERENCE, user.getUserId());
        editor.putString(TOKEN_PREFERENCE, user.getAuthenticationToken());
        AzureWebServicesHelper.setAuth(mClient, user.getUserId(), user.getAuthenticationToken());
        editor.apply();
    }

    /**
     * Creates a dialog and shows it
     *
     * @param exception The exception to show in the dialog
     * @param title     The dialog title
     */
    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }

    /**
     * Creates a dialog and shows it
     *
     * @param exception The exception to show in the dialog
     * @param title     The dialog title
     */
    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if (exception.getCause() != null) {
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    /**
     * Creates a dialog and shows it
     *
     * @param message The dialog message
     * @param title   The dialog title
     */
    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    public void doAuth(View view) {
        this.authenticate();
    }

    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);

            Futures.addCallback(future, new FutureCallback<ServiceFilterResponse>() {
                @Override
                public void onFailure(@NonNull Throwable e) {
                    resultFuture.setException(e);
                }

                @Override
                public void onSuccess(ServiceFilterResponse response) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    });

                    resultFuture.set(response);
                }
            });

            return resultFuture;
        }
    }
}
