package com.example.oli.scaleuser2;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oli on 25.07.2017.
 */

public class UserHistoryList extends ArrayAdapter {

    public static List listGewicht = new ArrayList();
    public static List listDatum = new ArrayList();

    public UserHistoryList(Context context, int resource)
    {
        super(context, resource);
    }

    public static void addGewicht(Object object)
    {
        listGewicht.add(object);
    }

    public static void addDatum(Object object)
    {
        listDatum.add(object);
    }

    public int getCount()
    {
        return listGewicht.size();
    }

    public static Object getItemGewicht(int position)
    {
        return listGewicht.get(position);
    }

    public static Object getItemDatum(int position)
    {
        return listDatum.get(position);
    }

}
