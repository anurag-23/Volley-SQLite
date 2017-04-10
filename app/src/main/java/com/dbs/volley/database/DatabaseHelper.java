package com.dbs.volley.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by anurag on 2/4/17.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseAdapter.LOGIN_CREATE);
        sqLiteDatabase.execSQL(DatabaseAdapter.VOL_CREATE);
        sqLiteDatabase.execSQL(DatabaseAdapter.ORG_CREATE);
        sqLiteDatabase.execSQL(DatabaseAdapter.VOL_FOR_CREATE);
        sqLiteDatabase.execSQL(DatabaseAdapter.EVENT_CREATE);
        sqLiteDatabase.execSQL(DatabaseAdapter.ORG_DELETE_TRIGGER);
        sqLiteDatabase.execSQL(DatabaseAdapter.VOL_DELETE_TRIGGER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DatabaseAdapter.LOGIN_DROP);
        sqLiteDatabase.execSQL(DatabaseAdapter.VOL_DROP);
        sqLiteDatabase.execSQL(DatabaseAdapter.ORG_DROP);
        sqLiteDatabase.execSQL(DatabaseAdapter.VOL_FOR_DROP);
        sqLiteDatabase.execSQL(DatabaseAdapter.EVENT_DROP);
        onCreate(sqLiteDatabase);
    }
}
