package com.example.proiect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/* ****************************************
    In aceasta sursa am codat DatabaseHelper
    ca in laboratorul cu baze de date,
    doar ca l-am adaptat la clasa mea si am
    adaugat functii pentru aplicatia mea
   **************************************** */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DatingApp.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_BIRTHDATE = "birthdate";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_INTERESTED_IN = "interestedIn";
    private static final String COLUMN_BIO = "bio";
    private static final String COLUMN_PROFILE_IMAGE_URL = "profileImageUrl";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Aici creez tabela de useri
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_BIRTHDATE + " TEXT,"
                + COLUMN_CITY + " TEXT,"
                + COLUMN_GENDER + " TEXT,"
                + COLUMN_INTERESTED_IN + " TEXT,"
                + COLUMN_BIO + " TEXT,"
                + COLUMN_PROFILE_IMAGE_URL + " TEXT,"
                + COLUMN_LATITUDE + " REAL,"
                + COLUMN_LONGITUDE + " REAL"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Aici creez tabela de like-uri (fiecare user poate da like la alti useri)
        String CREATE_LIKES_TABLE = "CREATE TABLE likes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_who_liked INTEGER,"
                + "user_who_is_liked INTEGER"
                + ")";
        db.execSQL(CREATE_LIKES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // In aceasta functie fac adaugarea unui nou user in tabela de useri
    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_BIRTHDATE, user.getBirthdate());
        values.put(COLUMN_CITY, user.getCity());
        values.put(COLUMN_GENDER, user.getGender());
        values.put(COLUMN_INTERESTED_IN, user.getInterestedIn());
        values.put(COLUMN_BIO, user.getBio());
        values.put(COLUMN_PROFILE_IMAGE_URL, user.getProfileImageUrl());
        values.put(COLUMN_LATITUDE, user.getLatitude());
        values.put(COLUMN_LONGITUDE, user.getLongitude());

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Cu aceasta functie verific in baza de date daca exista deja un user cu acele credentiale
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // Aici cand dau pe like dau like la un user anume si se actualizeaza tabela de like
    public void addLike(int whoLikedId, int whoIsLikedId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_who_liked", whoLikedId);
        values.put("user_who_is_liked", whoIsLikedId);
        db.insert("likes", null, values);
        db.close();
    }

    // In functia asta calculez cate like-uri are in total un user
    public int getLikesCount(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM likes WHERE user_who_is_liked = ?", new String[]{String.valueOf(userId)});

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    // Aici iau din baza de date toti utilizatorii si ii pun intr-o lista sa o pot folosii in alte activitati
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password")),
                        cursor.getString(cursor.getColumnIndexOrThrow("birthdate")),
                        cursor.getString(cursor.getColumnIndexOrThrow("city")),
                        cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                        cursor.getString(cursor.getColumnIndexOrThrow("interestedIn")),
                        cursor.getString(cursor.getColumnIndexOrThrow("bio")),
                        cursor.getString(cursor.getColumnIndexOrThrow("profileImageUrl")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("latitude")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"))
                );

                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));

                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return userList;
    }

    // Caut ID-ul unui user dupa email. Daca nu il gasesc returnez -1
    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE email = ?", new String[]{email});

        int userId = -1;

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }

        cursor.close();
        db.close();

        return userId;
    }

    // Verific daca eu am dat deja like user-ului la care am apasat pe butonul de like
    // Ca sa nu pot da 10000 de like-uri unui singur user (like-urile trebuie sa fie unice)
    public boolean hasUserAlreadyLiked(int whoLikedId, int whoIsLikedId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM likes WHERE user_who_liked = ? AND user_who_is_liked = ?",
                new String[]{String.valueOf(whoLikedId), String.valueOf(whoIsLikedId)}
        );

        boolean alreadyLiked = cursor.moveToFirst();
        cursor.close();
        db.close();

        return alreadyLiked;
    }

    // Functie in care sterg like-ul (in cazul in care vreau sa nu mai am like dat la un user)
    public void removeLike(int whoLikedId, int whoIsLikedId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                "likes",
                "user_who_liked = ? AND user_who_is_liked = ?",
                new String[]{String.valueOf(whoLikedId), String.valueOf(whoIsLikedId)}
        );
        db.close();
    }
}
