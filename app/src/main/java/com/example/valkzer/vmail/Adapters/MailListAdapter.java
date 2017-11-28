package com.example.valkzer.vmail.Adapters;

import android.view.View;
import android.app.Activity;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.CheckBox;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.example.valkzer.vmail.R;
import com.example.valkzer.vmail.Models.Mail;

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

        final Mail currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentItem);

        ((TextView) row.findViewById(R.id.lblFrom)).setText(currentItem.getFrom());
        ((TextView) row.findViewById(R.id.lblSubject)).setText(currentItem.getSubject());

        return row;
    }

}