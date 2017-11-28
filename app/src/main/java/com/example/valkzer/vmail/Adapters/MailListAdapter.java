package com.example.valkzer.vmail.Adapters;

import android.view.View;
import android.app.Activity;
import android.view.ViewGroup;
import android.content.Intent;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageButton;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.support.annotation.NonNull;

import com.example.valkzer.vmail.R;
import com.example.valkzer.vmail.Models.Mail;
import com.example.valkzer.vmail.ReadMailActivity;

import com.google.gson.Gson;

/**
 * Adapter to bind a ToDoItem List to a view
 */
public class MailListAdapter extends ArrayAdapter<Mail> {

    /**
     * Adapter context
     */
    private Context mContext;

    /**
     * Adapter View layout
     */
    private int mLayoutResourceId;

    public MailListAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Returns the view for a specific item on the list
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        final Mail mail = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(mail);

        ((TextView) row.findViewById(R.id.lblFrom)).setText(mail.getFrom());
        ((TextView) row.findViewById(R.id.lblSubject)).setText(mail.getSubject());

        bindButtonEvents(row, mail);

        return row;
    }

    private void bindButtonEvents(View row, final Mail mail) {
        final ImageButton btnRead = (ImageButton) row.findViewById(R.id.btnRead);

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Gson gson = new Gson();
                String encodedMail = gson.toJson(mail);
                mail.markAsRead(getContext());
                Intent myIntent = new Intent(getContext(), ReadMailActivity.class);
                myIntent.putExtra("mail", encodedMail);
                mContext.startActivity(myIntent);
            }
        });
    }

}