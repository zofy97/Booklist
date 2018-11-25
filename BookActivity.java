package com.example.sophi.booklist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BookActivity extends Activity {
    Database db;
    Cursor cursor;
    Context context = this;
    TextView titleField;
    TextView authorField;
    TextView genreField;
    TextView yearField;
    TextView startOfReadingField;
    TextView endOfReadingField;
    Button editButton;
    Button deleteButton;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        db = new Database(this);

        db.open();

        titleField = (TextView) findViewById(R.id.title);
        authorField = (TextView) findViewById(R.id.author);
        genreField = (TextView) findViewById(R.id.genre);
        yearField = (TextView) findViewById(R.id.year);
        startOfReadingField = (TextView) findViewById(R.id.startOfReading);
        endOfReadingField = (TextView) findViewById(R.id.endOfReading);

        editButton = (Button) findViewById(R.id.editButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        cursor = MainActivity.bookCursor;

        String[] col = new String[] {"title", "author", "genre", "year", "startOfReading", "endOfReading"};
        int[] id = new int[] {R.id.title, R.id.author, R.id.genre, R.id.year, R.id.startOfReading, R.id.endOfReading};

        editButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(BookActivity.this, UpdateActivity.class);
                finish();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        //https://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
        deleteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this entry?");

                builder.setPositiveButton("Yes, please", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteBook(cursor.getInt(0));
                        Intent intent = new Intent(BookActivity.this, MainActivity.class);
                        finish();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                //creating alert dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        titleField.setText(cursor.getString(1));
        authorField.setText(cursor.getString(2));
        genreField.setText(cursor.getString(3));
        yearField.setText(cursor.getString(4));
        startOfReadingField.setText(cursor.getString(5));
        endOfReadingField.setText(cursor.getString(6));
    }
}