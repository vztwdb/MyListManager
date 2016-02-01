package com.wolfcreations.mylistmanager.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wolfcreations.mylistmanager.model.Book;
import com.wolfcreations.mylistmanager.model.Movie;
import com.wolfcreations.mylistmanager.model.MyList;
import com.wolfcreations.mylistmanager.model.MyListItem;
import com.wolfcreations.mylistmanager.model.TagEnum;
import com.wolfcreations.mylistmanager.model.ToDo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public static final String COL_DESCRIPTION = "Description";
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
    private static final int DATABASE_VERSION = 7;
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
                    COL_RATING + " REAL, " +
                    COL_COMMENT + " TEXT, " +
                    COL_DESCRIPTION + " TEXT, " +
                    COL_URL + " TEXT, " +
                    COL_PICTURE + " TEXT, " +
                    COL_DEADLINE + " TEXT, " +
                    COL_AUTOR + " TEXT, " +
                    COL_TITLE + " TEXT, " +
                    COL_PRODUCER + " TEXT, " +
                    COL_REVIEW + " TEXT, " +
                    COL_IMDB_RATING + " REAL, " +
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
    public long createlist(MyList myList) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, myList.getName());
        values.put(COL_PRIORITY, myList.getPriority());
        values.put(COL_CATEGORY, myList.getCategory());
        return mDb.insert(TBL_LIST, null, values);
    }

    public long createlistitem(String name, int rating, String comment, String url,  int year,
                               String picture, String deadline, String autor, String title,
                               String review, String producer, int imdbrating, boolean important, String descr) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_RATING, rating);
        values.put(COL_COMMENT, comment);
        values.put(COL_DESCRIPTION, descr);
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

    public long createlistitem(MyListItem myListitem) {
        ContentValues values = new ContentValues();
        values.put(COL_LISTID, myListitem.getList().getId());
        values.put(COL_NAME, myListitem.getName());
        values.put(COL_RATING, myListitem.getRating());
        values.put(COL_COMMENT, myListitem.getDescription());
        values.put(COL_DESCRIPTION, myListitem.getComment());
        values.put(COL_URL, myListitem.getUrl());
        values.put(COL_PICTURE, myListitem.getPicture().getTagName());
        values.put(COL_PRIORITY, myListitem.getpriotity());
        if (myListitem.getCategory() == "Todo") {
            ToDo toDo = (ToDo) myListitem;
            values.put(COL_DEADLINE, toDo.getDueDate().toString());
        }
        if (myListitem.getCategory() == "Book") {
            Book book = (Book) myListitem;
            values.put(COL_AUTOR, book.getAutor());
            values.put(COL_TITLE, book.getTitle());
            values.put(COL_REVIEW, book.getReview());
        }

        if (myListitem.getCategory() == "Movie") {
            Movie movie = (Movie) myListitem;
            values.put(COL_PRODUCER, movie.getProducer());
            values.put(COL_IMDB_RATING, movie.getIMDBRating());
            values.put(COL_YEAR, movie.getYear());
        }

        return mDb.insert(TBL_LIST_ITEM, null, values);
    }


    public long updatelistitem(MyListItem myListitem) {
        ContentValues values = new ContentValues();
        values.put(COL_LISTID, myListitem.getList().getId());
        values.put(COL_NAME, myListitem.getName());
        values.put(COL_RATING, myListitem.getRating());
        values.put(COL_COMMENT, myListitem.getComment());
        values.put(COL_DESCRIPTION, myListitem.getDescription());
        values.put(COL_URL, myListitem.getUrl());
        values.put(COL_PICTURE, myListitem.getPicture().getTagName());
        values.put(COL_PRIORITY, myListitem.getpriotity());
        if (myListitem.getCategory() == "Todo") {
            ToDo toDo = (ToDo) myListitem;
            values.put(COL_DEADLINE, toDo.getDueDate().toString());
        }
        if (myListitem.getCategory() == "Book") {
            Book book = (Book) myListitem;
            values.put(COL_AUTOR, book.getAutor());
            values.put(COL_TITLE, book.getTitle());
            values.put(COL_REVIEW, book.getReview());
        }

        if (myListitem.getCategory() == "Movie") {
            Movie movie = (Movie) myListitem;
            values.put(COL_PRODUCER, movie.getProducer());
            values.put(COL_IMDB_RATING, movie.getIMDBRating());
            values.put(COL_YEAR, movie.getYear());
        }

        return mDb.update(TBL_LIST_ITEM, values, "_id=" + myListitem.getId(), null);
    }


    public  long createcategory(String name, int priority){
        ContentValues values = new ContentValues();

        values.put(COL_NAME, name);
        values.put(COL_PRIORITY, priority);
        return mDb.insert(TBL_CATEGORY, null, values);
    }

    //READ
    public MyList fetchListById(int id) {
        Cursor cursor = mDb.query(TBL_LIST, new String[]{COL_ID,
                        COL_NAME, COL_PRIORITY}, COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null
        );
        if (cursor != null)
            cursor.moveToFirst();
        return new MyList(
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

    public List<String> getAllCategories(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        Cursor cursor = mDb.query(TBL_CATEGORY, new String[]{COL_NAME},
                null, null, null, null, null
        );
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(cursor.getColumnIndex(COL_NAME)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        // returning lables
        return labels;
    }

    public List<MyListItem> fetchListItemsBySearchCriteria(String searchText) {
        String myQuery  = "SELECT Listid, a.Name as Listname, b.Name, b.Rating, b.Comment, b.Description, b.URL," +
                " b.Picture, b.Priority, b.End_date, b.Publisher, b.Year, b.Title, b.Producer, bIMDBRating, a.Category," +
                " a.priority as ListPriority  " +
                " FROM tbl_list a INNER JOIN tbl_list_item b ON a.id=b.Listid WHERE b.Name=%? OR b.Comment = %? OR b.Description = %? ";

        String q = String.format(myQuery, "\""+searchText + "%\"");

        Cursor mCursor = mDb.rawQuery(q, null);

        List<MyListItem> myListItems = new ArrayList<MyListItem>();
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        MyListItem anItem ;
        MyList alist;
        while (mCursor.isAfterLast() == false) {
            alist = new MyList( mCursor.getInt(mCursor.getColumnIndex(COL_LISTID)),
                    mCursor.getString(mCursor.getColumnIndex("Listname")),
                    mCursor.getInt(mCursor.getColumnIndex("ListPriority")));
            anItem = new MyListItem(alist,
                    mCursor.getInt(mCursor.getColumnIndex(COL_ID)),
                    mCursor.getString(mCursor.getColumnIndex(COL_NAME)),
                    mCursor.getString(mCursor.getColumnIndex(COL_COMMENT)));
            anItem.setRating(mCursor.getFloat(mCursor.getColumnIndex(COL_RATING)));
            anItem.setUrl(mCursor.getString(mCursor.getColumnIndex(COL_URL)));
            anItem.setDescription(mCursor.getString(mCursor.getColumnIndex(COL_DESCRIPTION)));
            anItem.setPicture(TagEnum.get(mCursor.getString(mCursor.getColumnIndex(COL_PICTURE))));
            anItem.setpriotity(mCursor.getInt(mCursor.getColumnIndex(COL_PRIORITY)));
            myListItems.add(anItem);
            mCursor.moveToNext();
        }
        return myListItems;
    }


    public List<MyListItem> fetchListItemsByListid(MyList alist) {

        Cursor mCursor = mDb.query(TBL_LIST_ITEM ,
                new String[]{COL_ID, COL_NAME, COL_COMMENT,COL_DESCRIPTION, COL_RATING,COL_URL, COL_PICTURE,
                        COL_PRIORITY, COL_DEADLINE, COL_AUTOR,COL_TITLE,COL_REVIEW,
                        COL_PRODUCER,COL_IMDB_RATING,COL_YEAR},
                COL_LISTID + "=?",
                new String[]{String.valueOf(alist.getId())}, null, null, null, null
        );

        List<MyListItem> myListItems = new ArrayList<MyListItem>();
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        MyListItem anItem ;
        while (mCursor.isAfterLast() == false) {
            anItem = new MyListItem(alist,
                    mCursor.getInt(mCursor.getColumnIndex(COL_ID)),
                    mCursor.getString(mCursor.getColumnIndex(COL_NAME)),
                    mCursor.getString(mCursor.getColumnIndex(COL_COMMENT)));
            anItem.setRating(mCursor.getFloat(mCursor.getColumnIndex(COL_RATING)));
            anItem.setUrl(mCursor.getString(mCursor.getColumnIndex(COL_URL)));
            anItem.setDescription(mCursor.getString(mCursor.getColumnIndex(COL_DESCRIPTION)));
            anItem.setPicture(TagEnum.get(mCursor.getString(mCursor.getColumnIndex(COL_PICTURE))));
            anItem.setpriotity(mCursor.getInt(mCursor.getColumnIndex(COL_PRIORITY)));
            anItem.setCategory(alist.getCategory());
            myListItems.add(anItem);
            mCursor.moveToNext();
        }
        return myListItems;
    }

    //UPDATE
    public void updateList(MyList myList) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, myList.getName());
        values.put(COL_PRIORITY, myList.getPriority());
        mDb.update(TBL_LIST, values,
                COL_ID + "=?", new String[]{String.valueOf(myList.getId())});
    }
    //DELETE
    public void deleteListById(int nId) {
        mDb.delete(TBL_LIST_ITEM, COL_LISTID + "=?", new String[]{String.valueOf(nId)});
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
