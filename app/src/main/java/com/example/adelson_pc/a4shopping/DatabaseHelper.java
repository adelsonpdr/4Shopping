package com.example.adelson_pc.a4shopping;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Adelson on 07/15/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String baseName = "database1.db";
    public static final String table = "user";
    public static final String id = "ID";
    public static final String name = "Name";
    public static final String user = "User";
    public static final String password = "Password";
    public static final String security_question = "Security_Question";

    public DatabaseHelper(Context context) {
        super(context, baseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + table + "( " + id +
                " integer primary key autoincrement, " + name + " text, " + user + " text, "
                + password + " text, " + security_question + " text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table);
        onCreate(db);
    }

    public boolean changePassword(String user_, String password_, String security_question_) {

        SQLiteDatabase db = this.getWritableDatabase();
        String buildSQL = "SELECT " + name + " FROM " + table + " WHERE " + user + " = '" + user_ +
                "' AND " + security_question + " = '" + security_question_ + "'";
        System.out.println(buildSQL);
        Cursor cursor = db.rawQuery(buildSQL, null);
        if (cursor.getCount() > 0) {
            try {
                ContentValues values = new ContentValues();
                values.put(password, password_);
                db.update(table, values, user + " = " + user_ + ", " + security_question + " = " +
                        security_question_, null);
            } catch (Exception e) {
                e.getMessage();
            }
            System.out.println("Trocada");
            return true;
        }
        return false;
    }


    public boolean login(String user_, String password_) {
        SQLiteDatabase db = this.getWritableDatabase();
        String buildSQL = "SELECT " + name + " FROM " + table + " WHERE " + user + " = '" + user_ +
                "' AND " + password + " = '" + password_ + "'";
        System.out.println(buildSQL);
        Cursor cursor = db.rawQuery(buildSQL, null);
        if (cursor.getCount() == 0) {
            return false;
        }
        System.out.println("Logado");
        return true;
    }

    public boolean signUp(String name_, String user_, String password_, String security_question_) {
        if (table.isEmpty() || !userRegistered(user_)) {
            insert(name_, user_, password_, security_question_);
            return true;
        } else {
            return false;
        }
    }


    public void insert(String name_, String user_, String password_, String security_question_) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(name, name_);
            values.put(user, user_);
            values.put(password, password_);
            values.put(security_question, security_question_);
            db.insert(table, null, values);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public boolean userRegistered(String user_) {
        SQLiteDatabase db = this.getWritableDatabase();
        String buildSQL = "SELECT " + name + " FROM " + table + " WHERE " + user + " = '" + user_ + "'";
        System.out.println(buildSQL);
        Cursor cursor = db.rawQuery(buildSQL, null);
        if (cursor.getCount() > 0) {
            System.out.println("Aqui");
            return true;
        }
        return false;
    }

    public boolean drop() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + table);
        return true;
    }
}
