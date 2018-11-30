package com.example.sophi.booklist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.sophi.booklist.InputActivity.REQUEST_IMAGE_CAPTURE;

public class UpdateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Database db;
    Cursor cursor;
    Button saveButton;
    Button startButton;
    Button endButton;
    Button picButton;
    EditText title;
    EditText author;
    EditText genre;
    EditText year;
    EditText startOfReading;
    EditText endOfReading;
    ImageView picture;
    RatingBar rating;
    String txtRating;
    boolean buttonChoice;
    File photoFile;
    Context context;
    Bitmap bitmap;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        db = new Database(this);

        db.open();

        cursor = MainActivity.bookCursor;

        txtRating = cursor.getString(8);

        startButton = (Button) findViewById(R.id.picDateStart);
        endButton = (Button) findViewById(R.id.picDateEnd);
        picButton = (Button) findViewById(R.id.takePicture);
        rating = (RatingBar) findViewById(R.id.ratingBar);
        rating.setRating(Float.parseFloat(txtRating));

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonChoice = true;
                datePicker(v);
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonChoice = false;
                datePicker(v);
            }
        });

        picButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {

                }
                if (photoFile != null)  {
                    Uri photoURI = FileProvider.getUriForFile(context, "com.example.sophi.fileprovider", photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
                Log.v("TEST CAM", photoFile.getPath());
                bitmap = BitmapFactory.decodeFile(photoFile.getPath());

            }
        });

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                txtRating = String.valueOf(rating);
            }
        });

        title = (EditText) findViewById(R.id.titleField);
        title.setText(cursor.getString(1));
        author = (EditText) findViewById(R.id.authorField);
        author.setText(cursor.getString(2));
        genre = (EditText) findViewById(R.id.genreField);
        genre.setText(cursor.getString(3));
        year = (EditText) findViewById(R.id.yearField);
        year.setText(cursor.getString(4));
        startOfReading = (EditText) findViewById(R.id.startOfReadingField);
        startOfReading.setText(cursor.getString(5));
        endOfReading = (EditText) findViewById(R.id.endOfReadingField);
        endOfReading.setText(cursor.getString(6));
        picture = (ImageView) findViewById(R.id.takePictureField);
        picture.setImageBitmap(BitmapFactory.decodeFile(cursor.getString(7)));


        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.i("test", title.getText().toString());
                if(title.getText().toString().equals(""))  {
                    String message = "Please enter a title";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    String titleInput = title.getText().toString();
                    String authorInput = author.getText().toString();
                    String genreInput = genre.getText().toString();
                    String yearInput = year.getText().toString();
                    String startOfReadingInput = startOfReading.getText().toString();
                    String endOfReadingInput = endOfReading.getText().toString();
                    String pictureInput = cursor.getString(7);
                    if(photoFile != null) {
                        pictureInput = photoFile.getPath();
                    }

                    db.updateBook(cursor.getInt(0), titleInput, authorInput, genreInput, yearInput, startOfReadingInput, endOfReadingInput, pictureInput, txtRating);
                    String message = "Edited";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                    finish();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK)   {
            picture.setImageBitmap(BitmapFactory.decodeFile(photoFile.getAbsolutePath()));
        }
    }

    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void datePicker(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        if(buttonChoice == true) {
            ((EditText) findViewById(R.id.startOfReadingField)).setText(dateFormat.format(calendar.getTime()));
        } else if(buttonChoice == false)    {
            ((EditText) findViewById(R.id.endOfReadingField)).setText(dateFormat.format(calendar.getTime()));
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    //https://www.numetriclabz.com/android-date-picker-tutorial/
    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);
        }
    }
}