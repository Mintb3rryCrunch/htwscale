package com.example.oli.scaleuser2.core;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oli on 25.07.2017.
 */

public class UserList extends ArrayAdapter {

    public static List listGewichtTimeline = new ArrayList();
    public static List listGewichtCommunity = new ArrayList();
    public static List listDatum = new ArrayList();
    public static List listName = new ArrayList();

    public UserList(Context context, int resource)
    {
        super(context, resource);
    }

    public static void addGewicht(Object object)
    {
        listGewichtTimeline.add(object);
    }

    public static void addDatum(Object object)
    {
        listDatum.add(object);
    }

    public int getCount()
    {
        return listGewichtTimeline.size();
    }

    public static Object getItemGewichtTimeline(int position)
    {
        return listGewichtTimeline.get(position);
    }

    public static Object getItemGewichtCommunity(int position)
    {
        return listGewichtCommunity.get(position);
    }

    public static Object getItemDatum(int position)
    {
        return listDatum.get(position);
    }

    public static Object getItemName(int position)
    {
        return listName.get(position);
    }

}
