package com.example.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "students.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "students";
    public static final String COL_ID = "id";
    public static final String COL_NUME = "nume";
    public static final String COL_VARSTA = "varsta";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NUME + " TEXT, " +
                COL_VARSTA + " INTEGER)";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertStudent(String nume, int varsta) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_NUME, nume);
        v.put(COL_VARSTA, varsta);
        db.insert(TABLE_NAME, null, v);
        db.close();
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        while (c.moveToNext()) {
            list.add(new Student(
                    c.getInt(0),
                    c.getString(1),
                    c.getInt(2)));
        }
        c.close(); db.close();
        return list;
    }

    public Student getStudentByName(String nume) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_NUME + "=?",
                new String[]{nume});
        if (c.moveToFirst()) {
            Student s = new Student(
                    c.getInt(0),
                    c.getString(1),
                    c.getInt(2));
            c.close(); db.close();
            return s;
        }
        c.close(); db.close();
        return null;
    }

    public List<Student> getStudentsBetweenAges(int min, int max) {
        List<Student> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COL_VARSTA + " BETWEEN ? AND ?",
                new String[]{String.valueOf(min), String.valueOf(max)});
        while (c.moveToNext()) {
            list.add(new Student(
                    c.getInt(0),
                    c.getString(1),
                    c.getInt(2)));
        }
        c.close(); db.close();
        return list;
    }

    public void deleteStudentsByAgeCondition(String cond, int val) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(
                "DELETE FROM " + TABLE_NAME +
                        " WHERE " + COL_VARSTA + " " + cond + " " + val);
        db.close();
    }

    public void increaseAgeForNameStartingWith(String letter) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(
                "UPDATE " + TABLE_NAME +
                        " SET " + COL_VARSTA + " = " + COL_VARSTA + " + 1" +
                        " WHERE " + COL_NUME + " LIKE ?",
                new Object[]{ letter + "%" });
        db.close();
    }
}
