package com.example.d18123347.booklist;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class BookActivity extends Activity {
    Database db;
    SimpleCursorAdapter myAdapter;
    Cursor cursor;
    TextView titleField;
    public void onCreate(Bundle savedInstanceState)
    {
        db = new Database(this);

        db.open();

        titleField = (TextView) findViewById(R.id.title);
        cursor = db.getBook(1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        String[] col = new String[] {"title", "author", "genre", "year", "startOfReading", "endOfReading"};
        int[] id = new int[] {R.id.title, R.id.author, R.id.genre, R.id.year, R.id.startOfReading, R.id.endOfReading};

        titleField.setText("hasladk");
    }
}
