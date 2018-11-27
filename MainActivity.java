package com.example.sophi.booklist;

import android.content.Intent;
import android.os.Bundle;import android.database.Cursor;
import android.app.ListActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity {
    /** Called when the activity is first created. */
    Database db;
    ListView list;
    SimpleCursorAdapter myAdapter;
    Cursor cursor;
    Button button;
    public static Cursor bookCursor;
    // FloatingActionButton addButton;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new Database(this);

        db.open();
        db.deleteAllBooks();
        addRows();

        cursor = db.getAllBooks();

        /*
        addButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        addButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InputActivity.class);
                startActivity(intent);
            }
        });
        */

        button = (Button) findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InputActivity.class);
                startActivity(intent);
            }
        });

        Log.i("test", "number of rows returned are" + cursor.getCount());
        String[] col = new String[] {"title"};
        int[] id = new int[] {R.id.title};
        myAdapter = new SimpleCursorAdapter(this, R.layout.row, cursor, col, id);
        setListAdapter(myAdapter);

        db.close();
    }

    public void onListItemClick(ListView list, View v, int position, long id){
        super.onListItemClick(list,v,position,id);
        Cursor cursor = (Cursor) myAdapter.getItem(position);
        String book = cursor.getString(1);
        bookCursor = cursor;
        Toast.makeText(this, book, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, BookActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void addRows()
    {
        long id;
        id = db.insertBook(
                "Paper Towns",
                "John Green",
                "Young adult",
                "2008",
                "13.11.2018",
                "",
                "");

        /*id = db.insertBook(
                "The Invisible Man",
                "H. G. Wells",
                "Horror",
                "1897",
                "07.11.2018",
                "12.11.2018");

        id = db.insertBook(
                "After the snow",
                "S. D. Crockett",
                "Young adult",
                "2012",
                "27.10.2018",
                "05.11.2018");
                */
    }
}
