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

    static final String[] INSERT_STATEMENTS = {"INSERT INTO ORGANIZATION VALUES ('Teach For India', 'tfi@gmail.com', '51 Carter Road', 'Mumbai', 'Maharashtra', '6508428567', 'teachforindia.org');","INSERT INTO ORGANIZATION VALUES ('Going To School', 'gts@gmail.com', 'D-12 A Kailash Colony', 'New Delhi', 'New Delhi', '9674825154', 'goingtoschool.org');","INSERT INTO ORGANIZATION VALUES ('HelpAge India', 'hai@gmail.com', 'Qutab Institutional Area', 'New Delhi', 'New Delhi', '4567823874', 'helpage.in');","INSERT INTO ORGANIZATION VALUES ('Prayatn', 'prayatn@gmail.com', 'Sanganer', 'Jaipur', 'Rajasthan', '8546127540', 'prayatn.org');","INSERT INTO ORGANIZATION VALUES ('Pratham', 'pratham@gmail.com', 'Nariman Point', 'Mumbai', 'Maharashtra', '9646582546', 'pratham.org');","INSERT INTO ORGANIZATION VALUES ('Care India', 'ci@gmail.com', 'Okhla Industrial Area', 'New Delhi', 'New Delhi', '7854612354', 'careindia.org');","INSERT INTO ORGANIZATION VALUES ('CRY India', 'cry@gmail.com', 'Anand Estate', 'Mumbai', 'Maharashtra', '9640504030', 'cryindia.org');","INSERT INTO ORGANIZATION VALUES ('Goonj', 'goonj@gmail.com', 'Sarita Vihar', 'New Delhi', 'New Delhi', '7574834723', 'goonjindia.org');","INSERT INTO VOLUNTEER VALUES ('Anurag Choudhary', 'anurag@gmail.com', '9591966814', 'Gurgaon', 'Haryana');","INSERT INTO VOLUNTEER VALUES ('Sai Preetham', 'sai@gmail.com', '8776859485', 'Bangalore', 'Karnataka');","INSERT INTO VOLUNTEER VALUES ('Achlendra Dhari Singh', 'ads@gmail.com', '9786758475', 'Patna', 'Bihar');","INSERT INTO VOLUNTEER VALUES ('Amandeep Singh Kalsi', 'ask@gmail.com', '8675849586', 'New Delhi', 'New Delhi');","INSERT INTO VOLUNTEER VALUES ('Udbhav Kush', 'udk@gmail.com', '7869586748', 'Ghaziabad', 'Uttar Pradesh');","INSERT INTO VOLUNTEER VALUES ('Akhilesh Kalase', 'kalase@gmail.com', '9685748596', 'Mumbai', 'Maharashtra');","INSERT INTO VOLUNTEER VALUES ('Akhil Vaidyanathan', 'av@gmail.com', '9683438596', 'Bangalore', 'Karnataka');","INSERT INTO VOLUNTEER VALUES ('Pranav Walia', 'rolls@gmail.com', '9685748595', 'Manipal', 'Karnataka');","INSERT INTO VOLUNTEER VALUES ('Chaitanya Kumar', 'chaits@gmail.com', '9685723456', 'New Delhi', 'New Delhi');","INSERT INTO VOLUNTEER VALUES ('Sharang Pai', 'pi@gmail.com', '9586748495', 'Pune', 'Maharashtra');","INSERT INTO VOLUNTEER VALUES ('Shaurya Malik', 'sm@gmail.com', '9685748347', 'Mumbai', 'Maharashtra');","INSERT INTO VOLUNTEER VALUES ('Rishabh Surendran', 'rishabh@gmail.com', '9512348314', 'Hyderabad', 'Telangana');","INSERT INTO VOLUNTEER VALUES ('Anchit Bansal', 'anchit@gmail.com', '7685738327', 'Yamunanagar', 'Haryana');","INSERT INTO VOLUNTEER VALUES ('Shivam Ojha', 'shivam@gmail.com', '7685749372', 'Noida', 'Uttar Pradesh');",                                                         "INSERT INTO LOGIN VALUES ('tfi@gmail.com', 'tfi', 'ORGANIZATION');","INSERT INTO LOGIN VALUES ('gts@gmail.com', 'gts', 'ORGANIZATION');","INSERT INTO LOGIN VALUES ('hai@gmail.com', 'hai', 'ORGANIZATION');","INSERT INTO LOGIN VALUES ('prayatn@gmail.com', 'pray', 'ORGANIZATION');","INSERT INTO LOGIN VALUES ('pratham@gmail.com', 'prat', 'ORGANIZATION');", "INSERT INTO LOGIN VALUES ('ci@gmail.com', 'ci', 'ORGANIZATION');","INSERT INTO LOGIN VALUES ('cry@gmail.com', 'cry', 'ORGANIZATION');","INSERT INTO LOGIN VALUES ('goonj@gmail.com', 'goonj', 'ORGANIZATION');","INSERT INTO LOGIN VALUES ('anurag@gmail.com', 'anuragc', 'VOLUNTEER');","INSERT INTO LOGIN VALUES ('sai@gmail.com', 'saip', 'VOLUNTEER');","INSERT INTO LOGIN VALUES ('ads@gmail.com', 'ads', 'VOLUNTEER');","INSERT INTO LOGIN VALUES ('ask@gmail.com', 'ask', 'VOLUNTEER');","INSERT INTO LOGIN VALUES ('udk@gmail.com', 'udk', 'VOLUNTEER');","INSERT INTO LOGIN VALUES ('kalase@gmail.com', 'akhileshk', 'VOLUNTEER');","INSERT INTO LOGIN VALUES ('av@gmail.com', 'akhilv', 'VOLUNTEER');","INSERT INTO LOGIN VALUES ('rolls@gmail.com', 'walia', 'VOLUNTEER');","INSERT INTO LOGIN VALUES ('chaits@gmail.com', 'ck', 'VOLUNTEER');","INSERT INTO LOGIN VALUES ('pi@gmail.com', 'spai', 'VOLUNTEER');","INSERT INTO LOGIN VALUES ('sm@gmail.com', 'shauryam', 'VOLUNTEER');","INSERT INTO LOGIN VALUES ('rishabh@gmail.com', 'rs', 'VOLUNTEER');","INSERT INTO LOGIN VALUES ('anchit@gmail.com', 'anchitb', 'VOLUNTEER');","INSERT INTO LOGIN VALUES ('shivam@gmail.com', 'sojha', 'VOLUNTEER');","INSERT INTO EVENT(NAME, OEMAIL, EDATE, ETIME, ADDRESS, CITY) VALUES ('Education Drive', 'tfi@gmail.com', '17/04/2017', '5:30 PM', 'Worli', 'Mumbai');","INSERT INTO EVENT(NAME, OEMAIL, EDATE, ETIME, ADDRESS, CITY) VALUES ('Textbook Donation', 'tfi@gmail.com', '19/04/2017', '3:30 PM', 'Bandra', 'Mumbai');","INSERT INTO EVENT(NAME, OEMAIL, EDATE, ETIME, ADDRESS, CITY) VALUES ('Cloth Donation Drive', 'tfi@gmail.com', '24/04/2017', '5:30 PM', 'Colaba', 'Mumbai');","INSERT INTO EVENT(NAME, OEMAIL, EDATE, ETIME, ADDRESS, CITY) VALUES ('Donate Your Tools', 'tfi@gmail.com', '26/04/2017', '8:30 AM', 'Andheri', 'Mumbai');","INSERT INTO EVENT(NAME, OEMAIL, EDATE, ETIME, ADDRESS, CITY) VALUES ('Mid-day Meal Initiative Launch', 'tfi@gmail.com', '27/04/2017', '2:30 PM', 'Christ School', 'Manipal');","INSERT INTO EVENT(NAME, OEMAIL, EDATE, ETIME, ADDRESS, CITY) VALUES ('Old Age Home Volunteering', 'hai@gmail.com', '18/04/2017', '1:30 PM', 'Sarojini Nagar', 'New Delhi');","INSERT INTO EVENT(NAME, OEMAIL, EDATE, ETIME, ADDRESS, CITY) VALUES ('Indoor Sports Competitions with the Elderly', 'hai@gmail.com', '22/04/2017', '3:30 PM', 'Saket', 'New Delhi');","INSERT INTO EVENT(NAME, OEMAIL, EDATE, ETIME, ADDRESS, CITY) VALUES ('Awareness Drive', 'goonj@gmail.com', '25/04/2017', '10:30 AM', 'Rohini', 'New Delhi');","INSERT INTO EVENT(NAME, OEMAIL, EDATE, ETIME, ADDRESS, CITY) VALUES ('Charity Concert', 'goonj@gmail.com', '29/04/2017', '8:30 PM', 'South Delhi', 'New Delhi');","INSERT INTO EVENT(NAME, OEMAIL, EDATE, ETIME, ADDRESS, CITY) VALUES ('Girl Child Empowerment Drive', 'prayatn@gmail.com', '15/04/2017', '2:30 PM', 'Hawa Mahal', 'Jaipur');","INSERT INTO EVENT(NAME, OEMAIL, EDATE, ETIME, ADDRESS, CITY) VALUES ('Volunteering at Girls Schools', 'prayatn@gmail.com', '23/04/2017', '7:30 AM', 'Syndicate Circle', 'Manipal');","INSERT INTO EVENT(NAME, OEMAIL, EDATE, ETIME, ADDRESS, CITY) VALUES ('Child Rights Awareness Campaign', 'cry@gmail.com', '12/04/2017', '5:30 PM', 'Bandra', 'Mumbai');","INSERT INTO EVENT(NAME, OEMAIL, EDATE, ETIME, ADDRESS, CITY) VALUES ('Day Out With The Children', 'cry@gmail.com', '19/04/2017', '8:30 PM', 'Pawai', 'Mumbai');","INSERT INTO VOLUNTEERS_FOR VALUES ('anurag@gmail.com', 'tfi@gmail.com', '01/04/2017', 4);","INSERT INTO VOLUNTEERS_FOR VALUES ('sai@gmail.com', 'tfi@gmail.com', '02/04/2017', 4);","INSERT INTO VOLUNTEERS_FOR VALUES ('ads@gmail.com', 'tfi@gmail.com', '03/04/2017', 4);","INSERT INTO VOLUNTEERS_FOR VALUES ('ask@gmail.com', 'tfi@gmail.com', '04/04/2017', 4);","INSERT INTO VOLUNTEERS_FOR VALUES ('udk@gmail.com', 'tfi@gmail.com', '05/04/2017', 4);","INSERT INTO VOLUNTEERS_FOR VALUES ('kalase@gmail.com', 'cry@gmail.com', '06/04/2017', 4);","INSERT INTO VOLUNTEERS_FOR VALUES ('av@gmail.com', 'cry@gmail.com', '07/04/2017', 4);","INSERT INTO VOLUNTEERS_FOR VALUES ('rolls@gmail.com', 'cry@gmail.com', '08/04/2017', 4);","INSERT INTO VOLUNTEERS_FOR VALUES ('chaits@gmail.com', 'cry@gmail.com', '09/04/2017', 4);","INSERT INTO VOLUNTEERS_FOR VALUES ('pi@gmail.com', 'prayatn@gmail.com', '10/04/2017', 4);","INSERT INTO VOLUNTEERS_FOR VALUES ('sm@gmail.com', 'prayatn@gmail.com', '11/04/2017', 4);","INSERT INTO VOLUNTEERS_FOR VALUES ('rishabh@gmail.com', 'prayatn@gmail.com', '11/04/2017', 4);","INSERT INTO VOLUNTEERS_FOR VALUES ('anchit@gmail.com', 'goonj@gmail.com', '11/04/2017', 4);","INSERT INTO VOLUNTEERS_FOR VALUES ('shivam@gmail.com', 'goonj@gmail.com', '11/04/2017', 4);"};

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
        Log.d("", insertQuery);
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
        Log.d("", insertQuery);
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
        Log.d("", insertQuery);
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
        Log.d("", query);
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
