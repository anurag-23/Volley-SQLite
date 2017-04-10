package com.dbs.volley.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.util.Log;

import com.dbs.volley.models.Event;
import com.dbs.volley.models.Organization;
import com.dbs.volley.models.Volunteer;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

    static final String VOL_FOR_CREATE = "create table if not exists VOLUNTEERS_FOR"+
            "(VEMAIL varchar(20) primary key, OEMAIL varchar(20), STARTDATE date, PERIOD int, foreign key (VEMAIL) references VOLUNTEER(EMAIL), foreign key (OEMAIL) references ORGANIZATION(EMAIL) on delete cascade);";

    static final String EVENT_CREATE = "create table if not exists EVENT"+
            "(ID integer primary key autoincrement, NAME varchar(20) unique, OEMAIL varchar(20), EDATE date, ETIME time, ADDRESS varchar(30), CITY varchar(20), foreign key (OEMAIL) references ORGANIZATION(EMAIL) on delete cascade);";

    static final String ORG_DELETE_TRIGGER = "CREATE TRIGGER IF NOT EXISTS ORG_DELETE"+
            " AFTER DELETE ON ORGANIZATION"+
            " FOR EACH ROW" +
            " BEGIN" +
            " DELETE FROM VOLUNTEERS_FOR WHERE OEMAIL = OLD.EMAIL;" +
            " DELETE FROM EVENT WHERE OEMAIL = OLD.EMAIL;"+
            " END;";

    static final String VOL_DELETE_TRIGGER = "CREATE TRIGGER IF NOT EXISTS VOL_DELETE"+
            " AFTER DELETE ON VOLUNTEER"+
            " FOR EACH ROW" +
            " BEGIN" +
            " DELETE FROM VOLUNTEERS_FOR WHERE VEMAIL = OLD.EMAIL;" +
            " END;";


    static final String LOGIN_DROP = "DROP TABLE IF EXISTS LOGIN;";
    static final String VOL_DROP = "DROP TABLE IF EXISTS VOLUNTEER;";
    static final String ORG_DROP = "DROP TABLE IF EXISTS ORGANIZATION;";
    static final String VOL_FOR_DROP = "DROP TABLE IF EXISTS VOLUNTEERS_FOR;";
    static final String EVENT_DROP = "DROP TABLE IF EXISTS EVENT;";

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
        String updateQuery = "UPDATE VOLUNTEER SET NAME = \'"+v.getName()+"\', PHONE = \'"+v.getPhone()+"\', CITY = \'"+v.getCity()+"\', STATE = \'"+v.getState()+"\' WHERE EMAIL = \'"+v.getEmail()+"\';";
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

    public List<Volunteer> getVolFromOrg(String orgEmail){
        String query = "SELECT * FROM VOLUNTEER WHERE EMAIL IN (SELECT VEMAIL FROM VOLUNTEERS_FOR WHERE OEMAIL = \'"+orgEmail+"\');";
        List<Volunteer> volList = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);
        if (c!=null && c.getCount()>0){
            c.moveToFirst();
            for (int i=0; i<c.getCount(); i++){
                Volunteer v = new Volunteer();
                v.setName(c.getString(0));
                v.setEmail(c.getString(1));
                v.setPhone(c.getString(2));
                v.setCity(c.getString(3));
                v.setState(c.getString(4));
                volList.add(v);
                c.moveToNext();
            }
            c.close();
        }
        return volList;
    }

    public List<Volunteer> fetchVol(String orgEmail, String key){
        String query = "SELECT * FROM VOLUNTEER WHERE EMAIL IN (SELECT VEMAIL FROM VOLUNTEERS_FOR WHERE OEMAIL = \'"+orgEmail+"\') AND (NAME LIKE '%"+key+"%' OR CITY LIKE '%"+key+"%');";
        List<Volunteer> volList = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);
        if (c!=null && c.getCount()>0){
            c.moveToFirst();
            for (int i=0; i<c.getCount(); i++){
                Volunteer v = new Volunteer();
                v.setName(c.getString(0));
                v.setEmail(c.getString(1));
                v.setPhone(c.getString(2));
                v.setCity(c.getString(3));
                v.setState(c.getString(4));
                volList.add(v);
                c.moveToNext();
            }
            c.close();
        }
        return volList;
    }



    public void orgInsert(Organization o){
        String insertQuery = "INSERT INTO ORGANIZATION VALUES (\'"+o.getName()+"\', \'"+o.getEmail()+"\', \'"+o.getAddress()+"\', \'"+o.getCity()+"\', \'"+o.getState()+"\', \'"+o.getPhone()+"\', \'"+o.getWebsite()+"\');";
        db.execSQL(insertQuery);
    }

    public void orgDelete(String email){
        String deleteQuery = "DELETE FROM ORGANIZATION WHERE EMAIL = \'"+email+"\';";
        db.execSQL(deleteQuery);
    }

    public void orgUpdate(Organization o){String updateQuery = "UPDATE ORGANIZATION SET NAME = \'"+o.getName()+"\', ADDRESS = \'"+o.getAddress()+"\', PHONE = \'"+o.getPhone()+"\', WEBSITE = \'"+o.getWebsite()+"\', CITY = \'"+o.getCity()+"\', STATE = \'"+o.getState()+"\' WHERE EMAIL = \'"+o.getEmail()+"\';";
        db.execSQL(updateQuery);
    }

    public Organization orgQuery(String email){
        String query = "SELECT * FROM ORGANIZATION WHERE EMAIL = \'"+email+"\';";
        Cursor c = db.rawQuery(query, null);

        if (c != null && c.getCount() > 0){
            Log.d("Reached", "here");
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

    public List<Organization> fetchOrg(String key){
        String query = "SELECT * FROM ORGANIZATION WHERE NAME LIKE '%"+key+"%' OR CITY LIKE '%"+key+"%';";
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


    public void volForInsert(String vEmail, String oEmail, int period){
        java.util.Date d = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String date = sdf.format(d);

        String query = "INSERT INTO VOLUNTEERS_FOR VALUES (\'"+vEmail+"\', \'"+oEmail+"\', "+date+", "+period+");";
        Log.d("", query);
        db.execSQL(query);
    }

    public boolean canVolunteerForOrg(String vEmail, String oEmail){
        String query = "SELECT * FROM VOLUNTEERS_FOR WHERE VEMAIL = \'"+vEmail+"\' AND OEMAIL = \'"+oEmail+"\';";
        Cursor c = db.rawQuery(query, null);
        if (c!=null){
            if (c.getCount() != 0){
                return false;
            }
            c.close();
        }

        return true;
    }

    public boolean isVolunteer(String vEmail){
        String query = "SELECT * FROM VOLUNTEERS_FOR WHERE VEMAIL = \'"+vEmail+"\';";
        Cursor c = db.rawQuery(query, null);
        if (c!=null){
            if (c.getCount() != 0) return true;
            c.close();
        }
        return false;
    }

    public String getOrg(String vEmail){
        String query = "SELECT OEMAIL FROM VOLUNTEERS_FOR WHERE VEMAIL = \'"+vEmail+"\';";
        Cursor c = db.rawQuery(query, null);
        if (c!=null){
            if (c.getCount()>0){
                c.moveToFirst();
                String temp = c.getString(0);
                c.close();
                return temp;
            }
        }
        return "";
    }

    public void volForDelete(String email){
        String query = "DELETE FROM VOLUNTEERS_FOR WHERE VEMAIL = \'"+email+"\';";
        db.execSQL(query);
    }

    public void eventInsert(Event e){
        String query = "INSERT INTO EVENT(NAME, OEMAIL, EDATE, ETIME, ADDRESS, CITY) VALUES (\'"+e.getName()+"\', \'"+e.getOrgEmail()+"\', \'"+e.getEventDate()+"\', \'"+e.getEventTime()+"\', \'"+e.getAddress()+"\', \'"+e.getCity()+"\');";
        db.execSQL(query);
    }

    public Organization getOrgFromEvent(String eventName){
        String query = "SELECT * FROM ORGANIZATION WHERE EMAIL = (SELECT OEMAIL FROM EVENT WHERE NAME = \'"+eventName+"\');";
        Cursor c = db.rawQuery(query, null);

        if (c!=null && c.getCount()>0){
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

    public List<Event> fetchEvents(String orgEmail){
        String query = "SELECT * FROM EVENT WHERE OEMAIL = \'"+orgEmail+"\';";
        Cursor c = db.rawQuery(query, null);
        List<Event> eventList = new ArrayList<>();

        if (c!=null && c.getCount() > 0){
            c.moveToFirst();

            for (int i=0; i<c.getCount(); i++){
                Event e = new Event();
                e.setName(c.getString(1));
                e.setOrgEmail(c.getString(2));
                e.setEventDate(c.getString(3));
                e.setEventTime(c.getString(4));
                e.setAddress(c.getString(5));
                e.setCity(c.getString(6));
                eventList.add(e);
                c.moveToNext();
            }
            c.close();
        }

        return eventList;
    }

    public List<Event> fetchEvents(String orgEmail, String key){
        String query = "SELECT * FROM EVENT WHERE OEMAIL = \'"+orgEmail+"\' AND (NAME LIKE '%"+key+"%' OR CITY LIKE '%"+key+"%');";
        Cursor c = db.rawQuery(query, null);
        List<Event> eventList = new ArrayList<>();

        if (c!=null && c.getCount() > 0){
            c.moveToFirst();

            for (int i=0; i<c.getCount(); i++){
                Event e = new Event();
                e.setName(c.getString(1));
                e.setOrgEmail(c.getString(2));
                e.setEventDate(c.getString(3));
                e.setEventTime(c.getString(4));
                e.setAddress(c.getString(5));
                e.setCity(c.getString(6));
                eventList.add(e);
                c.moveToNext();
            }
            c.close();
        }

        return eventList;
    }

    public void eventDelete(String name){
        String query = "DELETE FROM EVENT WHERE NAME = \'"+name+"\';";
        db.execSQL(query);
    }
}
