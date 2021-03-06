package com.example.valkzer.vmail.Util;

import android.content.Context;

import com.example.valkzer.vmail.Models.Mail;
import com.example.valkzer.vmail.Models.EmailAddress;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;

import java.net.MalformedURLException;

public class AzureWebServicesHelper {

    private static String userId;
    private static String token;

    private static MobileServiceTable<Mail> mailsTable;
    private static MobileServiceTable<EmailAddress> emailAddressesTable;

    public static MobileServiceClient getMobileServiceClient(Context context) throws MalformedURLException {
        MobileServiceClient mobileServiceClient = new MobileServiceClient("https://isw-1313.azurewebsites.net", context);
        authenticateMobileServiceClient(mobileServiceClient);
        return mobileServiceClient;
    }

    public static MobileServiceTable<EmailAddress> getEmailAddressTable(Context context) throws MalformedURLException {
        if (emailAddressesTable == null) {
            emailAddressesTable = getMobileServiceClient(context).getTable(EmailAddress.class);
        }
        return emailAddressesTable;
    }

    public static MobileServiceTable<Mail> getMailsTable(Context context) throws MalformedURLException {
        if (mailsTable == null) {
            mailsTable = getMobileServiceClient(context).getTable(Mail.class);
        }
        return mailsTable;
    }

    public static void setAuth(MobileServiceClient client, String userId, String token) {
        AzureWebServicesHelper.userId = userId;
        AzureWebServicesHelper.token = token;
        authenticateMobileServiceClient(client);
    }

    private static void authenticateMobileServiceClient(MobileServiceClient client) {
        MobileServiceUser user = new MobileServiceUser(userId);
        user.setAuthenticationToken(token);
        client.setCurrentUser(user);
    }
}
