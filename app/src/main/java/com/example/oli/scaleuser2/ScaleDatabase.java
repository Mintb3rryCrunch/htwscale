package com.example.oli.scaleuser2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

/**
 * Created by Oli on 20.04.2017.
 */

public class ScaleDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Userscale.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "scale_table";
    public static final String COL_1=   "ID";
    public static final String COl_2=   "WEIGHT";


    public ScaleDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT,WEIGHT REAL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
/*
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(oldVersion == 3 && newVersion == 4)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
*/

    public void insertData (String weight)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contenvalue = new ContentValues();
        contenvalue.put(COl_2, weight);
        db.insert(TABLE_NAME, null, contenvalue);

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


    public boolean updateData(String id, String weight)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        if (!TextUtils.isEmpty(weight))
        {
            contentValues.put(COl_2, weight);
        }

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public Cursor getAllUser2()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select *, (select count(*)from " +TABLE_NAME+ " b where a.ID > b.ID) as cnt from " +TABLE_NAME+ " a", null);

        return cursor;
    }

    public Integer deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }
}


