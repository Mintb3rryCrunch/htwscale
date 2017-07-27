package com.example.oli.scaleuser2;

/**
 * Created by Mamoudou on 23.07.2017.
 */

public class User {
    private String user_id, gewicht, datum;

    public User(String user_id, String gewicht, String datum)
    {
        this.setUser_id(user_id);
        this.setGewicht(gewicht);
        this.setDatum(datum);
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGewicht() {
        return gewicht;
    }

    public void setGewicht(String gewicht) {
        this.gewicht = gewicht;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }
}
