package com.example.oli.scaleuser2.core;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Verwaltet Listen deren Objekte  Nutzerdaten enthalten
 * und zur Visualisierung bereitgestellt werden.
 *
 * @author Oliver Dziedzic, Mamoudou Balde
 *
 * @version 1.0
 */

public class UserList extends ArrayAdapter {

    public static List listGewichtTimeline = new ArrayList();
    public static List listGewichtCommunity = new ArrayList();
    public static List listDatum = new ArrayList();
    public static List listName = new ArrayList();

    /**
     *  Konstruktor
     *
     * @param context die gegebene Kontext der Klasse
     *
     * @param resource die gegebene Resource der Klasse
     *
     */
    public UserList(Context context, int resource)
    {
        super(context, resource);
    }

    /**
     * Fuegt die Gewichtsdaten als Objekt in der Liste hinzu.
     *
     * @param object das gegebene Gewicht als Objekt
     *
     */
    public static void addGewicht(Object object)
    {
        listGewichtTimeline.add(object);
    }

    /**
     * Fuegt der Datumsverlauf als Objekt in der Liste hinzu.
     *
     * @param object das gegebene Datum als Objekt
     *
     */
    public static void addDatum(Object object)
    {
        listDatum.add(object);
    }

    /**
     * Gibt die Groesse der Liste zurueck.
     *
     * @return die Anzahl der gespeicherten Gewichtsdaten in der Liste
     *
     */
    public int getCount()
    {
        return listGewichtTimeline.size();
    }

    /**
     * Gibt das Objekt an der jeweiligen Position
     * für den History Graph zurueck.
     *
     * @param position die gegebene Position des Objektes
     *
     * @return das Objekt an der gegebenen  Position
     *
     */
    public static Object getItemGewichtTimeline(int position)
    {
        return listGewichtTimeline.get(position);
    }

    /**
     * Gibt das Objekt an der jeweiligen Position
     * für den Community Graph zurueck.
     *
     * @param position die gegebene Position des Objektes
     *
     * @return das Objekt an der gegebenen  Position
     *
     */
    public static Object getItemGewichtCommunity(int position)
    {
        return listGewichtCommunity.get(position);
    }

    /**
     * Gibt das Datum als Objekt an der jeweiligen position zurueck.
     *
     * @param position die gegebene Position des Objektes
     *
     * @return das Objekt an der gegebenen Position
     *
     */
    public static Object getItemDatum(int position)
    {
        return listDatum.get(position);
    }

    /**
     * Gibt der Name als Objekt an der jeweiligen position zurueck.
     *
     * @param position die gegebene Position des Objektes
     *
     * @return das Objekt an der gegebenen Position
     *
     */
    public static Object getItemName(int position)
    {
        return listName.get(position);
    }

}

