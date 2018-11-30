package com.example.sophi.booklist;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class OverviewActivity extends AppCompatActivity {
    Database db;
    TextView title;
    TextView author;
    TextView wikiLink;
    ImageView picture;
    RatingBar rating;
    Button showBooks;
    Cursor cursor;
    Bitmap bitmap;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        db = new Database(this);
        db.open();
        cursor = db.getLatestBook();

        title = (TextView) findViewById(R.id.title) ;
        author = (TextView) findViewById(R.id.author);
        picture = (ImageView) findViewById(R.id.picture);
        wikiLink = (TextView) findViewById(R.id.wikiLink);
        showBooks = (Button) findViewById(R.id.showBook);
        rating = (RatingBar) findViewById(R.id.ratingBar);

        title.setText(cursor.getString(1));
        author.setText(cursor.getString(2));
        picture.setImageBitmap(BitmapFactory.decodeFile(cursor.getString(7)));
        rating.setRating(Float.parseFloat(cursor.getString(8)));

        String titleLink = cursor.getString(1);
        String link = titleLink.replaceAll(" ", "_").toLowerCase();
        try {
            wikiLink.setText("https://en.wikipedia.org/wiki/" + link + "?wprov=sfla1");
        } catch (Exception e)   {
            titleLink = cursor.getString(2);
            link = titleLink.replaceAll(" ", "_").toLowerCase();
            wikiLink.setText("https://en.wikipedia.org/wiki/" + link + "?wprov=sfla1");
    }

        showBooks.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(OverviewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}