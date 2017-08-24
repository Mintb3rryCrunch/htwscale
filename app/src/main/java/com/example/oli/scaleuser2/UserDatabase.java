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


    public UserDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT, SURNAME TEXT, BIRTHDAY TEXT, GENDER TEXT, HEIGHT INTEGER, WEIGHT TEXT)");

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

    public Cursor getAllUser()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] colums = {COL_1, COl_2, COl_3, COl_4, COl_5, COl_6};
        Cursor cursor = db.query(TABLE_NAME, colums, null, null, null, null, null);

        return cursor;
    }

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


