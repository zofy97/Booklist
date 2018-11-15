package com.example.d18123347.booklist;

import android.os.Bundle;import android.database.Cursor;
import android.app.ListActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity {
    /** Called when the activity is first created. */
    Database db;
    ListView list;
    SimpleCursorAdapter myAdapter;
    Cursor cursor;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new Database(this);

        db.open();

        //addRows();

        cursor = db.getAllBooks();

        Log.i("test", "number of rows returned are" + cursor.getCount());
        String[] col = new String[] {"title","author","genre","year","startOfReading","endOfReading"};
        int[] id = new int[] {R.id.title,R.id.author,R.id.genre,R.id.year,R.id.startOfReading,R.id.endOfReading};
        myAdapter = new SimpleCursorAdapter(this, R.layout.row, cursor, col, id);
        setListAdapter(myAdapter);

        db.close();
    }

    public void onListItemClick(ListView list, View v, int position, long id){
        super.onListItemClick(list,v,position,id);
        Cursor cursor = (Cursor) myAdapter.getItem(position);
        String book = cursor.getString(1);
        Log.i("test", "the value of mystring is " + book);
        Toast.makeText(this, book, Toast.LENGTH_LONG).show();
    }

    //---add some people---
    public void addRows()
    {
        long id;
        id = db.insertBook(
                "PaperTowns",
                "JohnGreen",
                "Youngadult",
                "2008",
                "13.11.2018",
                "");

        id = db.insertBook(
                "The_Invisible_Man",
                "H._G._Wells",
                "Horror",
                "1897",
                "07.11.2018",
                "12.11.2018");

        id = db.insertBook(
                "After_the_snow",
                "S._D._Crockett",
                "Young_adult",
                "2012",
                "27.10.2018",
                "05.11.2018");
    }
}
