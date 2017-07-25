package com.example.oli.scaleuser2;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mamoudou on 23.07.2017.
 */

public class UserAdapter extends ArrayAdapter {

    List list = new ArrayList();
    public UserAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }


    public void add(User object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        //return super.getCount();
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        //return super.getItem(position);
        return list.get(position);
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        View row;
        row = convertView;
        UserHolder userHolder;
        if(row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.historylistview_activity,parent,false);
            userHolder = new UserHolder();
            //userHolder.tx_userid = (TextView) row.findViewById(R.id.tx_userid);
            userHolder.tx_gewicht = (TextView) row.findViewById(R.id.tx_gewicht);
            userHolder.tx_datum = (TextView) row.findViewById(R.id.tx_datum);
            row.setTag(userHolder);

        }
        else
        {
            userHolder = (UserHolder) row.getTag();
        }

        User user = (User)this.getItem(position);
        //userHolder.tx_userid.setText(user.getUser_id());
        userHolder.tx_gewicht.setText(user.getGewicht());
        userHolder.tx_datum.setText(user.getDatum());
        //return super.getView(position, convertView, parent);
        return row;
    }

    static class UserHolder
    {
        TextView tx_userid, tx_gewicht, tx_datum;
    }
}
