package com.example.sophi.booklist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.app.PendingIntent.getActivity;

// https://stackoverflow.com/questions/21872789/how-to-take-pic-with-camera-and-save-it-to-database-and-show-in-listview-in-andr
public class InputActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Database db;
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
    boolean buttonChoice;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Context context = this;
    Bitmap bitmap;
    File photoFile;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        bitmap = BitmapFactory.decodeFile("/storage/emulated/0/Android/data/com.example.sophi.booklist/files/Pictures/JPEG_20181127_162812_5379513410261349874.jpg");


        db = new Database(this);

        db.open();

        startButton = (Button) findViewById(R.id.picDateStart);
        endButton = (Button) findViewById(R.id.picDateEnd);
        picButton = (Button) findViewById(R.id.takePicture);
        picture = (ImageView) findViewById(R.id.takePictureField);

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
                photoFile = null;
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
                //Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/Android/data/com.example.sophi.booklist/files/Pictures/JPEG_20181127_162812_5379513410261349874.jpg");

            }
        });

        title = (EditText) findViewById(R.id.titleField);
        author = (EditText) findViewById(R.id.authorField);
        genre = (EditText) findViewById(R.id.genreField);
        year = (EditText) findViewById(R.id.yearField);
        startOfReading = (EditText) findViewById(R.id.startOfReadingField);
        endOfReading = (EditText) findViewById(R.id.endOfReadingField);

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
                    String pictureInput;

                    if(mCurrentPhotoPath == null)   {
                        db.insertBook(titleInput, authorInput, genreInput, yearInput, startOfReadingInput, endOfReadingInput, "");
                    } else {
                        db.insertBook(titleInput, authorInput, genreInput, yearInput, startOfReadingInput, endOfReadingInput, mCurrentPhotoPath);
                    }
                    String message = "Saved";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(InputActivity.this, MainActivity.class);
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
        galleryAddPic();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
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