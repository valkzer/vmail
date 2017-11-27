package com.example.valkzer.vmail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;

public class MainActivity extends AppCompatActivity {

    /**
     * Mobile Service Client reference
     */
    private MobileServiceClient mClient;

    public static final String SHARED_PREFERENCE_FILE = "temp";
    public static final String USER_ID_PREFERENCE = "uid";
    public static final String TOKEN_PREFERENCE = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.authenticate();
    }

    private void openHomeScreen() {

    }

    private void authenticate() {
        if (loadUserTokenCache(mClient)) {
            createAndShowDialog("You are now logged in", "Success");
        } else {
            ListenableFuture<MobileServiceUser> mLogin =
                    mClient.login(MobileServiceAuthenticationProvider.Facebook);
            Futures.addCallback(mLogin, new FutureCallback<MobileServiceUser>() {
                @Override
                public void onFailure(@NonNull Throwable exc) {
                    createAndShowDialog("You must log in. Login Required", "Error");
                }

                @Override
                public void onSuccess(MobileServiceUser user) {
                    createAndShowDialog(String.format(
                            "You are now logged in - %1$2s",
                            user.getUserId()), "Success");
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

    private boolean loadUserTokenCache(MobileServiceClient client) {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        String userId = prefs.getString(USER_ID_PREFERENCE, null);
        if (userId == null)
            return false;
        String token = prefs.getString(TOKEN_PREFERENCE, null);
        if (token == null)
            return false;
        MobileServiceUser user = new MobileServiceUser(userId);
        user.setAuthenticationToken(token);
        client.setCurrentUser(user);
        return true;
    }

    private void cacheUserToken(MobileServiceUser user) {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_ID_PREFERENCE, user.getUserId());
        editor.putString(TOKEN_PREFERENCE, user.getAuthenticationToken());
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
}
