package com.wolfcreations.mylistmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Gebruiker on 31/10/2015.
 */
public class LijstDbAdapter {
    //these are the column names
    public static final String COL_ID = "_id";
    public static final String COL_NAAM = "naam";
    public static final String COL_BELANGRIJK = "belangrijk";
    //these are the corresponding indices
    public static final int INDEX_ID = 0;
    public static final int INDEX_NAAM = INDEX_ID + 1;
    public static final int INDEX_BELANGRIJK = INDEX_ID + 2;
    //used for logging
    private static final String TAG = "LijstDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "dba_lijst";
    private static final String TABLE_NAME = "tbl_lijst";
    private static final int DATABASE_VERSION = 1;
    private Context mCtx;
    //SQL statement used to create the database
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_NAAM + " TEXT, " +
                    COL_BELANGRIJK + " INTEGER );";

     public  LijstDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    //open
    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }
    //close
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    //CREATE
//note that the id will be created for you automatically
    public void createlijst(String name, boolean important) {
        ContentValues values = new ContentValues();
        values.put(COL_NAAM, name);
        values.put(COL_BELANGRIJK, important ? 1 : 0);
        mDb.insert(TABLE_NAME, null, values);
    }
    //overloaded to take a reminder
    public long createlijst(Lijst lijst) {
        ContentValues values = new ContentValues();
        values.put(COL_NAAM, lijst.getNaam() ); // Contact Name
        values.put(COL_BELANGRIJK, lijst.getBelangrijk()); // Contact Phone Number
// Inserting Row
        return mDb.insert(TABLE_NAME, null, values);
    }

    //READ
    public Lijst fetchLijstById(int id) {
        Cursor cursor = mDb.query(TABLE_NAME, new String[]{COL_ID,
                        COL_NAAM, COL_BELANGRIJK}, COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null
        );
        if (cursor != null)
            cursor.moveToFirst();
        return new Lijst(
                cursor.getInt(INDEX_ID),
                cursor.getString(INDEX_NAAM),
                cursor.getInt(INDEX_BELANGRIJK)
        );
    }
    public Cursor fetchAllLijst() {
        Cursor mCursor = mDb.query(TABLE_NAME, new String[]{COL_ID,
                        COL_NAAM, COL_BELANGRIJK},
                null, null, null, null, null
        );
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //UPDATE
    public void updateLijst(Lijst lijst) {
        ContentValues values = new ContentValues();
        values.put(COL_NAAM, lijst.getNaam());
        values.put(COL_BELANGRIJK, lijst.getBelangrijk());
        mDb.update(TABLE_NAME, values,
                COL_ID + "=?", new String[]{String.valueOf(lijst.getId())});
    }
    //DELETE
    public void deleteLijstById(int nId) {
        mDb.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(nId)});
    }
    public void deleteAllLijsten() {
        mDb.delete(TABLE_NAME, null, null);
    }



    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}
