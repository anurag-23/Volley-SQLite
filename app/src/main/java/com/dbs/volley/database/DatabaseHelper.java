package com.dbs.volley.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

        for (int i=0; i<DatabaseAdapter.INSERT_STATEMENTS.length; i++){
            sqLiteDatabase.execSQL(DatabaseAdapter.INSERT_STATEMENTS[i]);
        }
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
