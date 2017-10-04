package com.example.oli.scaleuser2.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

/**
 * Hilfsklasse zum Erstellen, Aktualisieren und Öffnen der Datenbank.
 *
 * @author Oliver Dziedzic, Mamoudou Balde
 *
 * @version 1.0
 */

public class UserDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Userscale.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "user_table";
    public static final String COL_1=   "ID";
    public static final String COl_2=   "NAME";
    public static final String COl_3=   "SURNAME";
    public static final String COl_4=   "BIRTHDAY";
    public static final String COl_5=   "GENDER";
    public static final String COl_6=   "HEIGHT";
    public static final String COL_7=   "WEIGHT";


    /**
     * Konstruktor
     *
     * @param context der mitgegebene Kontext
     *
     */
    public UserDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Wird aufgerufen, wenn die Datenbank das erste Mal erstellt wird.
     * Hier werden SQL Statements ausgeführt, um die Tabellen zu erstellen.
     *
     * @param db die Datenbank
     *
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT, SURNAME TEXT, BIRTHDAY TEXT, GENDER TEXT, HEIGHT INTEGER, WEIGHT TEXT)");

    }

    /**
     * Wird aufgerufen, wenn eine Datenbankversion gefunden wurde,
     * die älter ist, als die aktuelle. Hier werden alte Datenbanktabellen angepasst.
     *
     * @param db die Datenbank
     * @param oldVersion alte Datenbankversion
     * @param newVersion neue Datenbanversion
     *
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Einfügen von Benutzerdaten in der Datenbank.
     *
     * @param name     Vorname des Benutzers
     * @param surname  Nachname des Benutzers
     * @param birthday Geburtsdatum des Benutzers
     * @param gender   Geschlecht des Benutzers
     * @param height   Grösse des Benutzers
     * @param weight   Gewicht des Benutzers
     *
     * @return true, wenn die Benutzerdaten in der Datenbank eingefügt sind, sonst false
     */
    public boolean insertData (String name, String surname, String birthday, String gender, String height, String weight)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contenvalue = new ContentValues();
        contenvalue.put(COl_2, name);
        contenvalue.put(COl_3, surname);
        contenvalue.put(COl_4, birthday);
        contenvalue.put(COl_5, gender);
        contenvalue.put(COl_6, height);
        contenvalue.put(COL_7, weight);

        long result = db.insert(TABLE_NAME, null, contenvalue);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME, null);
        return res;
    }

    public Cursor getUserData(int getId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int id= getId;
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+ " where "+COL_1+ " = "  +"\""+id+"\"", null);

        return res;
    }

    //select NAME from user_table where NAME = "Lisa"

    /**
     * Abfragen von Benutzerdaten aus der Datenbank.
     *
     * @return Ergebnis der Datenbankabfrage
     */
    public Cursor getAllUser()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] colums = {COL_1, COl_2, COl_3, COl_4, COl_5, COl_6};
        Cursor cursor = db.query(TABLE_NAME, colums, null, null, null, null, null);

        return cursor;
    }

    /**
     * Aktualisieren von Benutzerdatensätzen in der Datenbank.
     *
     * @param id       Id des Benutzers
     * @param name     Vorname des Benutzers
     * @param surname  Nachname des Benutzers
     * @param birthday Geburtsdatum des Benutzers
     * @param gender   Geschlecht des Benutzers
     * @param height   Grösse des Benutzers
     *
     * @return true, wenn die Datensätze in der Datenbank aktualisiert sind, sonst false
     */
    public boolean updateData(String id, String name, String surname, String birthday, String gender, String height)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        if (!TextUtils.isEmpty(name))
        {
            contentValues.put(COl_2, name);
        }
        if (!TextUtils.isEmpty(surname))
        {
            contentValues.put(COl_3, surname);
        }

        contentValues.put(COl_4, birthday);

        contentValues.put(COl_5,gender);

        if (!height.contains("150"))
        {
            contentValues.put(COl_6, height);
        }
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    /**
     * Aktualisieren von Gewichtdatensatz des Benutzers in der Datenbank.
     *
     * @param id      Id des Benutzers
     * @param weight  Gewicht des Benutzers
     *
     * @return true, wenn der Datensatz in der Datenbank aktualisiert ist, sonst false
     */
    public boolean updateData_weight(String id, String weight)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        if (!TextUtils.isEmpty(weight))
        {
            contentValues.put(COL_7, weight);
        }

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    /**
     * Abfragen von Benutzerdaten aus der Datenbank.
     *
     * @return Ergebnis der Datenbankabfrage
     */
    public Cursor getAllUser2()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select *, (select count(*)from " +TABLE_NAME+ " b where a.ID > b.ID) as cnt from " +TABLE_NAME+ " a", null);

        return cursor;
    }

    /**
     * Löschen von Benutzerdatensätzen in der Datenbank.
     *
     * @param id Id des Benutzers
     *
     * @return die gelöschte Id
     */
    public Integer deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }
}


