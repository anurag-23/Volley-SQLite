package com.dbs.volley.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dbs.volley.models.Organization;
import com.dbs.volley.models.Volunteer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 2/4/17.
 */
public class DatabaseAdapter {
    static final String DATABASE_NAME = "volley.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;

    static final String LOGIN_CREATE = "create table if not exists LOGIN"+
            "(EMAIL  varchar(20) primary key,"+
            "PASSWORD varchar(20)," +
            "TYPE varchar(12) check (TYPE IN ('VOLUNTEER', 'ORGANIZATION')));";

    static final String VOL_CREATE = "create table if not exists VOLUNTEER"+
            "(NAME varchar(20), EMAIL varchar(20) primary key, PHONE varchar(15), CITY varchar(20), STATE varchar(20));";

    static final String ORG_CREATE = "create table if not exists ORGANIZATION"+
            "(NAME varchar(20), EMAIL varchar(20) primary key, ADDRESS varchar(20), CITY varchar(20), STATE varchar(20), PHONE varchar(15), WEBSITE varchar(20));";

    static final String LOGIN_DROP = "DROP TABLE IF EXISTS LOGIN;";
    static final String VOL_DROP = "DROP TABLE IF EXISTS VOLUNTEER;";
    static final String ORG_DROP = "DROP TABLE IF EXISTS ORGANIZATION";

    public SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    public DatabaseAdapter(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(this.context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() throws SQLException{
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance(){
        return db;
    }

    public void loginInsert(String email, String password, String type){
        String insertQuery = "INSERT INTO LOGIN VALUES (\'"+email+"\', \'"+password+"\', \'"+type+"\');";
        db.execSQL(insertQuery);
    }

    public void loginDelete(String email){
        String deleteQuery = "DELETE FROM LOGIN WHERE EMAIL = \'"+email+"\';";
        db.execSQL(deleteQuery);
    }

    public void loginUpdate(String email, String password){
        String updateQuery = "UPDATE LOGIN SET PASSWORD = \'"+password+"\' WHERE EMAIL = \'"+email+"\';";
        db.execSQL(updateQuery);
    }

    public String loginVolQuery(String email){
        String query = "SELECT PASSWORD FROM LOGIN WHERE EMAIL = \'"+email+"\' AND TYPE = 'VOLUNTEER';";
        Cursor c = db.rawQuery(query, null);

        if (c != null && c.getCount()>0){
            c.moveToFirst();
            String temp = c.getString(0);
            c.close();
            return temp;
        }

        return null;
    }

    public String loginOrgQuery(String email){
        String query = "SELECT PASSWORD FROM LOGIN WHERE EMAIL = \'"+email+"\' AND TYPE = 'ORGANIZATION';";
        Cursor c = db.rawQuery(query, null);

        if (c != null && c.getCount()>0){
            c.moveToFirst();
            String temp = c.getString(0);
            c.close();
            return temp;
        }

        return null;
    }

    public void volInsert(Volunteer v){
        String insertQuery = "INSERT INTO VOLUNTEER VALUES (\'"+v.getName()+"\', \'"+v.getEmail()+"\', \'"+v.getPhone()+"\', \'"+v.getCity()+"\', \'"+v.getState()+"\');";
        db.execSQL(insertQuery);
    }

    public void volDelete(String email){
        String deleteQuery = "DELETE FROM VOLUNTEER WHERE EMAIL = \'"+email+"\';";
        db.execSQL(deleteQuery);
    }

    public void volUpdate(Volunteer v){
        String updateQuery = "UPDATE VOLUNTEER SET NAME = \'"+v.getName()+"\', SET PHONE = \'"+v.getPhone()+"\', SET CITY = \'"+v.getCity()+"\', SET STATE = \'"+v.getState()+"\' WHERE EMAIL = \'"+v.getEmail()+"\';";
        db.execSQL(updateQuery);
    }

    public Volunteer volQuery(String email){
        String query = "SELECT * FROM VOLUNTEER WHERE EMAIL = \'"+email+"\';";
        Cursor c = db.rawQuery(query, null);

        if (c != null && c.getCount() > 0){
            Volunteer v = new Volunteer();
            c.moveToFirst();
            v.setName(c.getString(0));
            v.setEmail(c.getString(1));
            v.setPhone(c.getString(2));
            v.setCity(c.getString(3));
            v.setState(c.getString(4));
            c.close();
            return v;
        }

        return null;
    }

    public void orgInsert(Organization o){
        String insertQuery = "INSERT INTO ORGANIZATION VALUES (\'"+o.getName()+"\', \'"+o.getEmail()+"\', \'"+o.getAddress()+"\', \'"+o.getCity()+"\', \'"+o.getState()+"\', \'"+o.getPhone()+"\', \'"+o.getWebsite()+"\');";
        db.execSQL(insertQuery);
    }

    public void orgDelete(String email){
        String deleteQuery = "DELETE FROM ORGANIZATION WHERE EMAIL = \'"+email+"\';";
        db.execSQL(deleteQuery);
    }

    public void orgUpdate(Organization o){
        String updateQuery = "UPDATE ORGANIZATION SET NAME = \'"+o.getName()+"\', SET ADDRESS = \'"+o.getAddress()+"\', SET PHONE = \'"+o.getPhone()+"\', SET WEBSITE = \'"+o.getWebsite()+"\', SET CITY = \'"+o.getCity()+"\', SET STATE = \'"+o.getState()+"\' WHERE EMAIL = \'"+o.getEmail()+"\';";
        db.execSQL(updateQuery);
    }

    public Organization orgQuery(String email){
        String query = "SELECT * FROM ORGANIZATION WHERE EMAIL = \'"+email+"\';";
        Cursor c = db.rawQuery(query, null);

        if (c != null && c.getCount() > 0){
            Organization o = new Organization();
            c.moveToFirst();
            o.setName(c.getString(0));
            o.setEmail(c.getString(1));
            o.setAddress(c.getString(2));
            o.setCity(c.getString(3));
            o.setState(c.getString(4));
            o.setPhone(c.getString(5));
            o.setWebsite(c.getString(6));
            c.close();
            return o;
        }

        return null;
    }

    public List<Organization> fetchOrg(){
        String query = "SELECT * FROM ORGANIZATION;";
        Cursor c = db.rawQuery(query, null);
        List<Organization> orgList = new ArrayList<>();

        if (c!=null && c.getCount() > 0){
            c.moveToFirst();

            for (int i=0; i<c.getCount(); i++){
                Organization o = new Organization();
                o.setName(c.getString(0));
                o.setEmail(c.getString(1));
                o.setAddress(c.getString(2));
                o.setCity(c.getString(3));
                o.setState(c.getString(4));
                o.setPhone(c.getString(5));
                o.setWebsite(c.getString(6));
                orgList.add(o);

                c.moveToNext();
            }
            c.close();
        }

        return orgList;
    }
}
