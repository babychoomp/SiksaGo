package com.example.penta.memo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.penta.DataBaseHelper;
import com.example.penta.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateMemo extends AppCompatActivity {

    SQLiteDatabase db;
    DataBaseHelper mDbHelper;
    EditText mTitleText;
    EditText mDescriptionText;
    Spinner mSpinner;
    DatePicker pickerDate;
    TimePicker pickerTime;
    TextView time;
    TextView date;
    CheckBox checkBoxAlarm;
    Button saveNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_create);
        mDbHelper = new DataBaseHelper(this);
        db = mDbHelper.getWritableDatabase();

        mTitleText = (EditText) findViewById(R.id.txttitle);
        mDescriptionText = (EditText) findViewById(R.id.description);
        mSpinner = (Spinner) findViewById(R.id.spinnerNoteType);
        pickerDate = (DatePicker) findViewById(R.id.datePicker);
        pickerTime = (TimePicker) findViewById(R.id.timePicker);
        time = (TextView) findViewById(R.id.txtTime);
        date = (TextView) findViewById(R.id.txtDate);
        checkBoxAlarm = (CheckBox) findViewById(R.id.checkBox);

        pickerDate.setVisibility(View.INVISIBLE);
        pickerTime.setVisibility(View.INVISIBLE);
        time.setVisibility(View.INVISIBLE);
        date.setVisibility(View.INVISIBLE);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.note_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView parent, View view, int position, long id) {
                if(id == 2){
                    showToast(getString(R.string.added_alert));
                    checkBoxAlarm.setEnabled(true);
                }
                else {
                    checkBoxAlarm.setEnabled(false);
                    checkBoxAlarm.setChecked(false);
                }
            }

            public void onNothingSelected(AdapterView parent) {
            }
        });
        checkBoxAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true){
                    pickerDate.setVisibility(View.VISIBLE);
                    pickerTime.setVisibility(View.VISIBLE);
                    time.setVisibility(View.VISIBLE);
                    date.setVisibility(View.VISIBLE);
                }
                else{
                    pickerDate.setVisibility(View.INVISIBLE);
                    pickerTime.setVisibility(View.INVISIBLE);
                    time.setVisibility(View.INVISIBLE);
                    date.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_save:
                String title = mTitleText.getText().toString();
                String detail = mDescriptionText.getText().toString();
                String type =  mSpinner.getSelectedItem().toString();
                ContentValues cv = new ContentValues();
                cv.put(mDbHelper.TITLE, title);
                cv.put(mDbHelper.DETAIL, detail);
                cv.put(mDbHelper.TYPE, type);
                cv.put(mDbHelper.TIME, getString(R.string.Not_Set));

                if (checkBoxAlarm.isChecked()){
                    Calendar calender = Calendar.getInstance();
                    calender.clear();
                    calender.set(Calendar.MONTH, pickerDate.getMonth());
                    calender.set(Calendar.DAY_OF_MONTH, pickerDate.getDayOfMonth());
                    calender.set(Calendar.YEAR, pickerDate.getYear());
                    calender.set(Calendar.HOUR, pickerTime.getCurrentHour());
                    calender.set(Calendar.MINUTE, pickerTime.getCurrentMinute());
                    calender.set(Calendar.SECOND, 00);

                    SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
                    String timeString = formatter.format(new Date(calender.getTimeInMillis()));
                    SimpleDateFormat dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
                    String dateString = dateformatter.format(new Date(calender.getTimeInMillis()));

                    AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(this, AlamReceiver.class);

                    String alertTitle = mTitleText.getText().toString();
                    intent.putExtra(getString(R.string.alert_title), alertTitle);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                                                                                                            // 이게 있어야 안드로이드 12 이상에서 사용 가능

                    alarmMgr.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
                    cv.put(mDbHelper.TIME, timeString);
                    cv.put(mDbHelper.DATE, dateString);

                }

                db.insert("comments_table", null, cv);
                finish();
                return true;

            case R.id.action_back:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}