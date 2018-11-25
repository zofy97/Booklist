package com.example.sophi.booklist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class InputActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Database db;
    Button saveButton;
    Button startButton;
    Button endButton;
    EditText title;
    EditText author;
    EditText genre;
    EditText year;
    EditText startOfReading;
    EditText endOfReading;
    boolean buttonChoice;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        db = new Database(this);

        db.open();

        startButton = (Button) findViewById(R.id.picDateStart);
        endButton = (Button) findViewById(R.id.picDateEnd);

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

                    db.insertBook(titleInput, authorInput, genreInput, yearInput, startOfReadingInput, endOfReadingInput);
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