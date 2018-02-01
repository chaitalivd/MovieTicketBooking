
package com.example.indian.movieticketbooking.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.indian.movieticketbooking.activities.SelectedMovieDetailsActivity;
import com.example.indian.movieticketbooking.helper.DataBaseHelper;
import com.example.indian.movieticketbooking.pojo.BookedTickets;
import com.example.indian.movieticketbooking.pojo.Review;
import com.example.indian.movieticketbooking.pojo.SeatStatus;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by Chaitali on 26/01/2018.
 */

public class DataBaseHandler {
    static final String DATABASE_NAME = "movies.db";
    static final int DATABASE_VERSION = 1;

    public static final String USER_TABLE_CREATE = "create table LOGIN (ID integer primary key autoincrement," + "USERNAME text, PASSWORD text, CITY text, IMAGE blob)";
    public static final String REVIEW_TABLE_CREATE = "create table REVIEW (ID integer primary key autoincrement," + "USERNAME text, MOVIENAME text, RATESTAR text, MOVIEREVIEW text)";
    public static final String TICKETS_TABLE_CREATE = "create table TICKETS (ID integer primary key autoincrement," + "USERNAME text, MOVIENAME text, THEATRENAME text, SCHEDULE text, SEATNO text)";
    public static final String AVAILABILITY_TABLE_CREATE = "create table AVAILABILITY (ID integer primary key autoincrement," + "MOVIENAME text, THEATRENAME text, SEATNO text, STATUS text)";

    public SQLiteDatabase db;
    private final Context context;
    private DataBaseHelper dataBaseHelper;

    public DataBaseHandler(Context context) {
        this.context = context;
        dataBaseHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DataBaseHandler open() throws SQLException {
        db = dataBaseHelper.getWritableDatabase();
        db = dataBaseHelper.getReadableDatabase();
        return this;
    }

    public void close(){
        db.close();
    }

    public void insert(String username, String password, String city, byte[] image){
        ContentValues newValues = new ContentValues();
        newValues.put("USERNAME", username);
        newValues.put("PASSWORD", password);
        newValues.put("CITY", city);
        newValues.put("IMAGE", image);

        db.insert("LOGIN", null, newValues);
        close();
    }

    public void insertReview(String username, String movieName, String rateStar, String movieReview){
        ContentValues newValues = new ContentValues();
        newValues.put("USERNAME", username);
        newValues.put("MOVIENAME", movieName);
        newValues.put("RATESTAR", rateStar);
        newValues.put("MOVIEREVIEW", movieReview);

        db.insert("REVIEW", null, newValues);
        close();
    }

    public void insertTickets(String username, String movieName, String theatreName, String schedule, String seatNo){
        ContentValues newValues = new ContentValues();
        newValues.put("USERNAME", username);
        newValues.put("MOVIENAME", movieName);
        newValues.put("THEATRENAME", theatreName);
        newValues.put("SCHEDULE", schedule);
        newValues.put("SEATNO", seatNo);

        db.insert("TICKETS", null, newValues);
        close();
    }

    public void insertAvailability(String movieName, String theatreName, String seatNo, String status){
        ContentValues newValues = new ContentValues();
        newValues.put("MOVIENAME", movieName);
        newValues.put("THEATRENAME", theatreName);
        newValues.put("SEATNO", seatNo);
        newValues.put("STATUS", status);

        db.insert("AVAILABILITY", null, newValues);
        close();
    }

    public String getSingleEntry(String userName) {
        String password;
        Cursor cursor = db.query("LOGIN", null, "USERNAME=?", new String[]{userName}, null, null, null);
        if (cursor.getCount() < 1) {
            password = "Empty";
            //user not exist
        }
        else{
            cursor.moveToFirst();
            password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
        }
        cursor.close();
        close();
        return password;
    }

    public ArrayList<String> getUserDetails(String userName) {
        ArrayList<String> userArray = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + " LOGIN " + " where USERNAME='" + userName + "'", null);
        int count = cursor.getColumnCount();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex("USERNAME"));
                    userArray.add(name);
                    String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
                    userArray.add(password);
                    String city = cursor.getString(cursor.getColumnIndex("CITY"));
                    userArray.add(city);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return userArray;
    }

    public void update(String userName, String chUserName, String chUserCity) {
        //Define the updated row content
        ContentValues updatedValues = new ContentValues();
        //assign values for each row
        updatedValues.put("USERNAME", chUserName);
        updatedValues.put("CITY", chUserCity);
        String where = "USERNAME = ?";
        db.update("LOGIN", updatedValues, where, new String[]{userName});
        close();
    }

    public void updatePwd(String userName, String chPassword) {
        //Define the updated row content
        ContentValues updatedValues = new ContentValues();
        //assign values for each row
        updatedValues.put("PASSWORD", chPassword);
        String where = "USERNAME = ?";
        db.update("LOGIN", updatedValues, where, new String[]{userName});
        close();
    }

    public Bitmap getImage(String userName){
        System.out.println("Enter the query");
        Bitmap bmp = null;
        Cursor cursor = db.query("LOGIN", null, "USERNAME=?", new String[]{userName}, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                byte[] blob = cursor.getBlob(cursor.getColumnIndex("IMAGE"));
                bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
            }
        }
        cursor.close();
        close();
        return bmp;
    }

    public Review getUserReviews(String movieName) {
        Review r = new Review();
        Cursor cursor = db.rawQuery("select * from " + " REVIEW " + " where MOVIENAME='" + movieName + "'", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex("USERNAME"));
                    r.userName.add(name);
                    String rateStar = cursor.getString(cursor.getColumnIndex("RATESTAR"));
                    r.userRate.add(rateStar);
                    String review = cursor.getString(cursor.getColumnIndex("MOVIEREVIEW"));
                    r.userReview.add(review);
                }
                while (cursor.moveToNext());            }
        }
        cursor.close();
        close();
        return r;
    }

    public SeatStatus getSeatAvailability(String movieName, String theatreName) {
        SeatStatus s = new SeatStatus();
        Cursor cursor = db.rawQuery("select * from " + " AVAILABILITY " + " where MOVIENAME='" + movieName + "' and THEATRENAME='" + theatreName + "'", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String seatNum = cursor.getString(cursor.getColumnIndex("SEATNO"));
                    s.seatBlocked.add(seatNum);
                    String status = cursor.getString(cursor.getColumnIndex("STATUS"));
                    s.seatStatus.add(status);
                }
                while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return s;
    }

    public BookedTickets getBookedTickets(String userName) {
        BookedTickets b = new BookedTickets();
        Cursor cursor = db.rawQuery("select * from " + " TICKETS " + " where USERNAME='" + userName + "'", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String moviename = cursor.getString(cursor.getColumnIndex("MOVIENAME"));
                    b.movieNames.add(moviename);
                    String theatrename = cursor.getString(cursor.getColumnIndex("THEATRENAME"));
                    b.theatreNames.add(theatrename);
                    String schedule = cursor.getString(cursor.getColumnIndex("SCHEDULE"));
                    b.schedules.add(schedule);
                    String seat = cursor.getString(cursor.getColumnIndex("SEATNO"));
                    b.seatNos.add(seat);
                }
                while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return b;
    }

    public String getLastBookingDate() {
        String date;
        Cursor cursor = db.rawQuery("select * from " + " TICKETS " + " where  ID=" + " (SELECT MAX(ID) FROM "+" TICKETS)", null);
        if (cursor.getCount() < 1) {
            date = "Empty";
            System.out.println(date);
            //user not exist
        }
        else{
            cursor.moveToFirst();
            date = cursor.getString(cursor.getColumnIndex("SCHEDULE"));
        }
        cursor.close();
        close();
        return date;
    }
}
