package com.example.sophi.booklist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database   {

    // database columns
    private static final String KEY_ROWID 	    = "_id";
    private static final String KEY_TITLE 	    = "title";
    private static final String KEY_AUTHOR   	= "author";
    private static final String KEY_GENRE	    = "genre";
    private static final String KEY_YEAR	    = "year";
    private static final String KEY_START       = "startOfReading";
    private static final String KEY_END 	    = "endOfReading";
    private static final String KEY_IMAGEPATH   = "image_path";
    private static final String KEY_RATING      = "rating";
    private static final String DATABASE_NAME 	= "Books";
    private static final String DATABASE_TABLE 	= "Book_Details";
    private static final int DATABASE_VERSION 	= 1;

    // SQL statement to create the database
    private static final String DATABASE_CREATE =
            "create table Book_Details (_id integer primary key autoincrement, " +
                    "title text not null, " +
                    "author text not null, "  +
                    "genre text not null, " +
                    "year text not null, " +
                    "startOfReading text not null, " +
                    "endOfReading text not null, " +
                    "image_path text, " +
                    "rating text not null);";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public Database(Context ctx)
    {
        this.context 	= ctx;
        DBHelper 		= new DatabaseHelper(context);
    }

    public Database open() throws SQLException
    {
        db     = DBHelper.getWritableDatabase();
        return this;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
            // dB structure change..
        }
    }

    public void close()
    {
        DBHelper.close();
    }

    public long insertBook(String title, String author, String genre, String year, String startOfReading, String endOfReading, String image_path, String rating)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_AUTHOR, author);
        initialValues.put(KEY_GENRE, genre);
        initialValues.put(KEY_YEAR, year);
        initialValues.put(KEY_START, startOfReading);
        initialValues.put(KEY_END, endOfReading);
        initialValues.put(KEY_IMAGEPATH, image_path);
        initialValues.put(KEY_RATING, rating);

        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteBook(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID +
                "=" + rowId, null) > 0;
    }

    public boolean deleteAllBooks() {
        return db.delete(DATABASE_TABLE, null, null)   > 0;
    }

    public Cursor getAllBooks()
    {
        return db.query(DATABASE_TABLE, new String[]
                        {
                                KEY_ROWID,
                                KEY_TITLE,
                                KEY_AUTHOR,
                                KEY_GENRE,
                                KEY_YEAR,
                                KEY_START,
                                KEY_END,
                                KEY_IMAGEPATH,
                                KEY_RATING
                        },
                null, null, null, null, null);
    }

    public Cursor getLatestBook() throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[]
                                {
                                        KEY_ROWID,
                                        KEY_TITLE,
                                        KEY_AUTHOR,
                                        KEY_GENRE,
                                        KEY_YEAR,
                                        KEY_START,
                                        KEY_END,
                                        KEY_IMAGEPATH,
                                        KEY_RATING
                                },
                        null, null, null, null, KEY_ROWID + " DESC", null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getBook(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[]
                                {
                                        KEY_ROWID,
                                        KEY_TITLE,
                                        KEY_AUTHOR,
                                        KEY_GENRE,
                                        KEY_YEAR,
                                        KEY_START,
                                        KEY_END,
                                        KEY_IMAGEPATH,
                                        KEY_RATING
                                },
                        KEY_ROWID + "=" + rowId,  null, null, null, null, null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateBook(long rowId,String title, String author, String genre, String year, String startOfReading, String endOfReading, String image_path, String rating)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_AUTHOR, author);
        args.put(KEY_GENRE, genre);
        args.put(KEY_YEAR, year);
        args.put(KEY_START, startOfReading);
        args.put(KEY_END, endOfReading);
        args.put(KEY_IMAGEPATH, image_path);
        args.put(KEY_RATING, rating);
        return db.update(DATABASE_TABLE, args,
                KEY_ROWID + "=" + rowId, null) > 0;
    }
}