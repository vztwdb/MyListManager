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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
    public static final String COL_VIEWED = "Viewed";
    public static final String COL_READ = "Read";
    public static final String COL_DONE = "Done";
    public static final String COL_IMDB_RATING = "IMDBRating";
    public static final String COL_CATEGORY = "Category";
    //these are the corresponding indices
    public static final int INDEX_ID = 0;
    //used for logging
    private static final String TAG = "ListDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "dba_list";
    private static final String TBL_LIST = "tbl_list";
    private static final String TBL_LIST_ITEM = "tbl_list_item";
    private static final String TBL_CATEGORY = "tbl_category";
    private static final int DATABASE_VERSION = 11;
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
                    COL_DEADLINE + " INTEGER, " +
                    COL_AUTOR + " TEXT, " +
                    COL_TITLE + " TEXT, " +
                    COL_PRODUCER + " TEXT, " +
                    COL_DONE + " INTEGER, " +
                    COL_READ + " INTEGER, " +
                    COL_VIEWED + " INTEGER, " +
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

    //overloaded to take a reminder
    public long createlist(MyList myList) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, myList.getName());
        values.put(COL_PRIORITY, myList.getPriority());
        values.put(COL_CATEGORY, myList.getCategory());
        return mDb.insert(TBL_LIST, null, values);
    }

    public long createlistitem(MyListItem myListitem) {
        ContentValues values = new ContentValues();
        values.put(COL_LISTID, myListitem.getList().getId());
        values.put(COL_NAME, myListitem.getName());
        values.put(COL_RATING, myListitem.getRating());
        values.put(COL_COMMENT, myListitem.getComment());
        values.put(COL_DESCRIPTION, myListitem.getDescription());
        values.put(COL_URL, myListitem.getUrl());
        values.put(COL_PICTURE, myListitem.getPicture().getTagName());
        values.put(COL_PRIORITY, myListitem.getpriotity());
        if (myListitem.getCategory().equals("Todo")) {
            ToDo toDo = (ToDo) myListitem;
            if (toDo.getDueDate() != null) {
                values.put(COL_DEADLINE, toDo.getDueDate().getTime());
            }
            values.put(COL_DONE, toDo.isDone());
        }
        if (myListitem.getCategory().equals("Book")) {
            Book book = (Book) myListitem;
            values.put(COL_AUTOR, book.getAutor());
            values.put(COL_TITLE, book.getTitle());
            values.put(COL_YEAR, book.getYear());
            values.put(COL_READ, book.isRead());
        }

        if (myListitem.getCategory().equals("Movie")) {
            Movie movie = (Movie) myListitem;
            values.put(COL_PRODUCER, movie.getProducer());
            values.put(COL_TITLE, movie.getTitle()

            );
            values.put(COL_YEAR, movie.getYear());
            values.put(COL_VIEWED, movie.isViewed());
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
        if (myListitem.getCategory().equals("Todo")) {
            ToDo toDo = (ToDo) myListitem;
            values.put(COL_DEADLINE, toDo.getDueDate().getTime());
            values.put(COL_DONE, toDo.isDone());
        }else if (myListitem.getCategory().equals("Book")) {
            Book book = (Book) myListitem;
            values.put(COL_AUTOR, book.getAutor());
            values.put(COL_TITLE, book.getTitle());
            values.put(COL_REVIEW, book.getReview());
            values.put(COL_READ, book.isRead());
        } else if (myListitem.getCategory().equals("Movie") ) {
            Movie movie = (Movie) myListitem;
            values.put(COL_PRODUCER, movie.getProducer());
            values.put(COL_IMDB_RATING, movie.getIMDBRating());
            values.put(COL_YEAR, movie.getYear());
            values.put(COL_VIEWED, movie.isViewed());
        }

        return mDb.update(TBL_LIST_ITEM, values, "_id=" + myListitem.getId(), null);
    }


    public ArrayList<MyList> getAllList() {
        ArrayList<MyList> myLists = new ArrayList<>();
        Cursor mCursor = mDb.query(TBL_LIST, new String[]{COL_ID,
                        COL_NAME, COL_PRIORITY, COL_CATEGORY},
                null, null, null, null, null
        );
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        MyList alist;
        while (mCursor.isAfterLast() == false) {
            alist = new MyList( mCursor.getInt(mCursor.getColumnIndex(COL_ID)),
                    mCursor.getString(mCursor.getColumnIndex(COL_NAME)),
                    mCursor.getInt(mCursor.getColumnIndex(COL_PRIORITY)));
            alist.setCategory(mCursor.getString(mCursor.getColumnIndex(COL_CATEGORY)));
            myLists.add(alist);
            mCursor.moveToNext();
        }
        // closing connection
        mCursor.close();
        return myLists;
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

    public List<MyListItem> fetchListItemsBySearchCriteria(String searchText) throws ParseException {
        String myQuery  = "SELECT b._id,  Listid, a.Name as Listname, b.Name, b.Rating, b.Comment, b.Description, b.URL," +
                " b.Picture, b.Priority, b.End_date, b.Publisher, b.Year, b.Title, b.Producer, b.Review, b.Done, b.Viewed, b.Read, b.IMDBRating, a.Category," +
                " a.priority as ListPriority  " +
                " FROM tbl_list a INNER JOIN tbl_list_item b ON a._id = b.Listid WHERE b.Name like '%" + searchText + "%' OR b.Comment like '%" + searchText + "%' OR b.Description like '%" + searchText + "%' ";



        Cursor mCursor = mDb.rawQuery(myQuery, null);

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
            alist.setCategory(mCursor.getString(mCursor.getColumnIndex(COL_CATEGORY)));
            if (alist.getCategory().equals("Todo")) {
                anItem = new ToDo(alist,
                        mCursor.getInt(mCursor.getColumnIndex(COL_ID)),
                        mCursor.getString(mCursor.getColumnIndex(COL_NAME)),
                        mCursor.getString(mCursor.getColumnIndex(COL_COMMENT)));
                if ((mCursor.getLong(mCursor.getColumnIndex(COL_DEADLINE))) > 0) {
                    ((ToDo) anItem).setDueDate(new Date(mCursor.getLong(mCursor.getColumnIndex(COL_DEADLINE))));
                }
                ((ToDo)anItem).setDone(mCursor.getInt(mCursor.getColumnIndex(COL_DONE)) != 0);
            }else if (alist.getCategory().equals("Book")) {
                anItem = new Book(alist,
                        mCursor.getInt(mCursor.getColumnIndex(COL_ID)),
                        mCursor.getString(mCursor.getColumnIndex(COL_NAME)),
                        mCursor.getString(mCursor.getColumnIndex(COL_COMMENT)));
                ((Book)anItem).setAutor(mCursor.getString(mCursor.getColumnIndex(COL_AUTOR)));
                ((Book)anItem).setYear(mCursor.getInt(mCursor.getColumnIndex(COL_YEAR)));
                ((Book)anItem).setRead(mCursor.getInt(mCursor.getColumnIndex(COL_READ)) != 0);
            } else if (alist.getCategory().equals("Movie") ) {
                anItem = new Movie(alist,
                        mCursor.getInt(mCursor.getColumnIndex(COL_ID)),
                        mCursor.getString(mCursor.getColumnIndex(COL_NAME)),
                        mCursor.getString(mCursor.getColumnIndex(COL_COMMENT)));
                ((Movie)anItem).setProducer(mCursor.getString(mCursor.getColumnIndex(COL_AUTOR)));
                ((Movie)anItem).setYear(mCursor.getInt(mCursor.getColumnIndex(COL_YEAR)));
                ((Movie)anItem).setViewed(mCursor.getInt(mCursor.getColumnIndex(COL_VIEWED)) != 0);
            } else {
                anItem = new MyListItem(alist,
                        mCursor.getInt(mCursor.getColumnIndex(COL_ID)),
                        mCursor.getString(mCursor.getColumnIndex(COL_NAME)),

                        mCursor.getString(mCursor.getColumnIndex(COL_COMMENT)));
            }
            anItem.setRating(mCursor.getFloat(mCursor.getColumnIndex(COL_RATING)));
            anItem.setUrl(mCursor.getString(mCursor.getColumnIndex(COL_URL)));
            anItem.setDescription(mCursor.getString(mCursor.getColumnIndex(COL_DESCRIPTION)));
            anItem.setPicture(TagEnum.get(mCursor.getString(mCursor.getColumnIndex(COL_PICTURE))));
            anItem.setpriotity(mCursor.getInt(mCursor.getColumnIndex(COL_PRIORITY)));
            anItem.setCategory(alist.getCategory());
            myListItems.add(anItem);
            mCursor.moveToNext();
        }
        // closing connection
        mCursor.close();

        return myListItems;
    }


    public List<MyListItem> fetchListItemsByListid(MyList alist) throws ParseException {

        Cursor mCursor = mDb.query(TBL_LIST_ITEM ,
                new String[]{COL_ID, COL_NAME, COL_COMMENT,COL_DESCRIPTION, COL_RATING,COL_URL, COL_PICTURE,
                        COL_PRIORITY, COL_DEADLINE, COL_AUTOR,COL_TITLE,COL_REVIEW,
                        COL_PRODUCER,COL_IMDB_RATING,COL_YEAR,COL_DONE,COL_READ,COL_VIEWED},
                COL_LISTID + "=?",
                new String[]{String.valueOf(alist.getId())}, null, null, null, null
        );

        List<MyListItem> myListItems = new ArrayList<MyListItem>();
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        MyListItem anItem ;

        while (mCursor.isAfterLast() == false) {
            if (alist.getCategory().equals("Todo")) {
                anItem = new ToDo(alist,
                        mCursor.getInt(mCursor.getColumnIndex(COL_ID)),
                        mCursor.getString(mCursor.getColumnIndex(COL_NAME)),
                        mCursor.getString(mCursor.getColumnIndex(COL_COMMENT)));
                if ((mCursor.getLong(mCursor.getColumnIndex(COL_DEADLINE))) > 0) {
                    ((ToDo) anItem).setDueDate(new Date(mCursor.getLong(mCursor.getColumnIndex(COL_DEADLINE))));
                }
                ((ToDo)anItem).setDone(mCursor.getInt(mCursor.getColumnIndex(COL_DONE)) != 0);
            }else if (alist.getCategory().equals("Book")) {
                anItem = new Book(alist,
                        mCursor.getInt(mCursor.getColumnIndex(COL_ID)),
                        mCursor.getString(mCursor.getColumnIndex(COL_NAME)),
                        mCursor.getString(mCursor.getColumnIndex(COL_COMMENT)));
                ((Book)anItem).setAutor(mCursor.getString(mCursor.getColumnIndex(COL_AUTOR)));
                ((Book)anItem).setYear(mCursor.getInt(mCursor.getColumnIndex(COL_YEAR)));
                ((Book)anItem).setRead(mCursor.getInt(mCursor.getColumnIndex(COL_READ)) != 0);
            } else if (alist.getCategory().equals("Movie") ) {
                anItem = new Movie(alist,
                        mCursor.getInt(mCursor.getColumnIndex(COL_ID)),
                        mCursor.getString(mCursor.getColumnIndex(COL_NAME)),
                        mCursor.getString(mCursor.getColumnIndex(COL_COMMENT)));
                ((Movie)anItem).setProducer(mCursor.getString(mCursor.getColumnIndex(COL_AUTOR)));
                ((Movie)anItem).setYear(mCursor.getInt(mCursor.getColumnIndex(COL_YEAR)));
                ((Movie)anItem).setViewed(mCursor.getInt(mCursor.getColumnIndex(COL_VIEWED)) != 0);
            } else {
                anItem = new MyListItem(alist,
                        mCursor.getInt(mCursor.getColumnIndex(COL_ID)),
                        mCursor.getString(mCursor.getColumnIndex(COL_NAME)),
                        mCursor.getString(mCursor.getColumnIndex(COL_COMMENT)));
            }
            anItem.setRating(mCursor.getFloat(mCursor.getColumnIndex(COL_RATING)));
            anItem.setUrl(mCursor.getString(mCursor.getColumnIndex(COL_URL)));
            anItem.setDescription(mCursor.getString(mCursor.getColumnIndex(COL_DESCRIPTION)));
            anItem.setPicture(TagEnum.get(mCursor.getString(mCursor.getColumnIndex(COL_PICTURE))));
            anItem.setpriotity(mCursor.getInt(mCursor.getColumnIndex(COL_PRIORITY)));
            anItem.setCategory(alist.getCategory());
            myListItems.add(anItem);
            mCursor.moveToNext();
        }
        // closing connection
        mCursor.close();

        return myListItems;
    }

    //UPDATE
    public void updateList(MyList myList) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, myList.getName());
        values.put(COL_PRIORITY, myList.getPriority());
        values.put(COL_CATEGORY, myList.getCategory());
        mDb.update(TBL_LIST, values,
                COL_ID + "=?", new String[]{String.valueOf(myList.getId())});
    }
    //DELETE
    public void deleteListById(int nId) {
        mDb.delete(TBL_LIST_ITEM, COL_LISTID + "=?", new String[]{String.valueOf(nId)});
        mDb.delete(TBL_LIST, COL_ID + "=?", new String[]{String.valueOf(nId)});
    }

    public void deleteListItemById(int nId) {
        mDb.delete(TBL_LIST_ITEM, COL_ID + "=?", new String[]{String.valueOf(nId)});
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
