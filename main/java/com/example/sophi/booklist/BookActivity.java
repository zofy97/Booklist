package com.example.sophi.booklist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class BookActivity extends AppCompatActivity {
    Database db;
    Cursor cursor;
    Context context = this;
    ImageView pictureField;
    TextView titleField;
    TextView authorField;
    TextView genreField;
    TextView yearField;
    TextView startOfReadingField;
    TextView endOfReadingField;
    TextView wikiLinkField;
    RatingBar rating;
    Button editButton;
    Button deleteButton;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        db = new Database(this);

        db.open();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        pictureField = (ImageView) findViewById(R.id.picture);
        titleField = (TextView) findViewById(R.id.title);
        authorField = (TextView) findViewById(R.id.author);
        genreField = (TextView) findViewById(R.id.genre);
        yearField = (TextView) findViewById(R.id.year);
        startOfReadingField = (TextView) findViewById(R.id.startOfReading);
        endOfReadingField = (TextView) findViewById(R.id.endOfReading);
        wikiLinkField = (TextView) findViewById(R.id.wikiLink);
        rating = (RatingBar) findViewById(R.id.ratingBar);

        editButton = (Button) findViewById(R.id.editButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        cursor = MainActivity.bookCursor;

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
        pictureField.setImageBitmap(BitmapFactory.decodeFile(cursor.getString(7)));

        String titleLink = cursor.getString(1);
        String link = titleLink.replaceAll(" ", "_").toLowerCase();
        wikiLinkField.setText("https://en.wikipedia.org/wiki/" + link + "?wprov=sfla1");

        rating.setRating(Float.parseFloat(cursor.getString(8)));
    }
}