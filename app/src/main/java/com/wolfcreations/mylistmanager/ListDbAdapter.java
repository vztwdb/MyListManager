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
public class ListDbAdapter {
    //these are the column names
    public static final String COL_ID = "_id";
    public static final String COL_LISTID = "Listid";
    public static final String COL_NAME = "Name";
    public static final String COL_RATING = "Rating";
    public static final String COL_COMMENT = "Comment";
    public static final String COL_URL = "URL";
    public static final String COL_PICTURE = "Picture";
    public static final String COL_PRIORITY = "Priority";
    public static final String COL_DEADLINE = "End_date";
    public static final String COL_AUTOR = "Publisher";
    public static final String COL_YEAR = "Year";
    public static final String COL_TITLE = "Title";
    public static final String COL_REVIEW = "Review";
    public static final String COL_PRODUCER = "Producer";
    public static final String COL_IMDB_RATING = "IMDBRating";
    public static final String COL_CATEGORY = "Category";
    //these are the corresponding indices
    public static final int INDEX_ID = 0;
    public static final int INDEX_NAME = INDEX_ID + 1;
    public static final int INDEX_PRIORITY = INDEX_ID + 2;
    //used for logging
    private static final String TAG = "ListDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "dba_list";
    private static final String TBL_LIST = "tbl_list";
    private static final String TBL_LIST_ITEM = "tbl_list_item";
    private static final String TBL_CATEGORY = "tbl_category";
    private static final int DATABASE_VERSION = 2;
    private Context mCtx;
    //SQL statement used to create the database
    private static final String TABLE_CATEGORY_CREATE =
            "CREATE TABLE if not exists " + TBL_CATEGORY + " ( " +
                    COL_NAME + " TEXT PRIMARY KEY , " +
                    COL_PRIORITY + " INTEGER );";

    private static final String TABLE_LIST_CREATE =
            "CREATE TABLE if not exists " + TBL_LIST + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_NAME + " TEXT, " +
                    COL_PRIORITY + " INTEGER, " +
                    COL_CATEGORY + " TEXT, " +
                    " FOREIGN KEY ("+COL_CATEGORY+") REFERENCES "+TBL_CATEGORY+"("+COL_NAME+")); ";

    private static final String TABLE_LIST_ITEM_CREATE =
            "CREATE TABLE if not exists " + TBL_LIST_ITEM + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_NAME + " TEXT, " +
                    COL_RATING + " INTEGER, " +
                    COL_COMMENT + " TEXT, " +
                    COL_URL + " TEXT, " +
                    COL_PICTURE + " TEXT, " +
                    COL_DEADLINE + " TEXT, " +
                    COL_AUTOR + " TEXT, " +
                    COL_TITLE + " TEXT, " +
                    COL_PRODUCER + " TEXT, " +
                    COL_REVIEW + " TEXT, " +
                    COL_IMDB_RATING + " INTEGER, " +
                    COL_PRIORITY + " INTEGER ," +
                    COL_YEAR + " INTEGER ," +
                    COL_LISTID + " INTEGER ," +
                    COL_CATEGORY + " TEXT ," +
                    " FOREIGN KEY ("+COL_LISTID+") REFERENCES "+TBL_LIST+"("+COL_ID+")," +
                    " FOREIGN KEY ("+COL_CATEGORY+") REFERENCES "+TBL_CATEGORY+"("+COL_NAME+")); ";

    public ListDbAdapter(Context ctx) {
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
    public long createlist(String name, boolean important, String category) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PRIORITY, important ? 1 : 0);
        values.put(COL_CATEGORY, category);
        return mDb.insert(TBL_LIST, null, values);
    }
    //overloaded to take a reminder
    public long createlist(List list) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, list.getName());
        values.put(COL_PRIORITY, list.getPriority());
        values.put(COL_CATEGORY, list.getCategory());
        return mDb.insert(TBL_LIST, null, values);
    }

    public long createlistitem(String name, int rating, String comment, String url,  int year,
                               String picture, String deadline, String autor, String title,
                               String review, String producer, int imdbrating, boolean important) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_RATING, rating);
        values.put(COL_COMMENT, comment);
        values.put(COL_URL, url);
        values.put(COL_PICTURE, picture);
        values.put(COL_DEADLINE, deadline);
        values.put(COL_AUTOR, autor);
        values.put(COL_TITLE, title);
        values.put(COL_PRODUCER, producer);
        values.put(COL_REVIEW, review);
        values.put(COL_IMDB_RATING, imdbrating);
        values.put(COL_PRIORITY, important ? 1 : 0);
        values.put(COL_YEAR, year);
        return mDb.insert(TBL_LIST_ITEM, null, values);
    }

    public  long createcategory(String name, int priority){
        ContentValues values = new ContentValues();

        values.put(COL_NAME, name);
        values.put(COL_PRIORITY, priority);
        return mDb.insert(TBL_CATEGORY, null, values);
    }

    //READ
    public List fetchListById(int id) {
        Cursor cursor = mDb.query(TBL_LIST, new String[]{COL_ID,
                        COL_NAME, COL_PRIORITY}, COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null
        );
        if (cursor != null)
            cursor.moveToFirst();
        return new List(
                cursor.getInt(INDEX_ID),
                cursor.getString(INDEX_NAME),
                cursor.getInt(INDEX_PRIORITY)
        );
    }
    public Cursor fetchAllList() {
        Cursor mCursor = mDb.query(TBL_LIST, new String[]{COL_ID,
                        COL_NAME, COL_PRIORITY},
                null, null, null, null, null
        );
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //UPDATE
    public void updateList(List list) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, list.getName());
        values.put(COL_PRIORITY, list.getPriority());
        mDb.update(TBL_LIST, values,
                COL_ID + "=?", new String[]{String.valueOf(list.getId())});
    }
    //DELETE
    public void deleteListById(int nId) {
        mDb.delete(TBL_LIST, COL_ID + "=?", new String[]{String.valueOf(nId)});
    }
    public void deleteAllLists() {
        mDb.delete(TBL_LIST, null, null);
    }



    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, TABLE_CATEGORY_CREATE);
            db.execSQL(TABLE_CATEGORY_CREATE);
            Log.w(TAG, TABLE_LIST_CREATE);
            db.execSQL(TABLE_LIST_CREATE);
            Log.w(TAG, TABLE_LIST_ITEM_CREATE);
            db.execSQL(TABLE_LIST_ITEM_CREATE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TBL_LIST_ITEM);
            db.execSQL("DROP TABLE IF EXISTS " + TBL_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TBL_CATEGORY);
            onCreate(db);
            ResetReferentieData(db);
        }

        private static void ResetReferentieData(SQLiteDatabase db) {
            db.execSQL("delete from "+ TBL_CATEGORY);
            String Insert_Data="INSERT INTO " +  TBL_CATEGORY + " VALUES('General',1)";
            db.execSQL(Insert_Data);
            Insert_Data="INSERT INTO " +  TBL_CATEGORY + " VALUES('Book',2)";
            db.execSQL(Insert_Data);
            Insert_Data="INSERT INTO " +  TBL_CATEGORY + " VALUES('Todo',3)";
            db.execSQL(Insert_Data);
            Insert_Data="INSERT INTO " +  TBL_CATEGORY + " VALUES('Movie',4)";
            db.execSQL(Insert_Data);

        }

    }

}
