package com.example.penta.memo;

import static com.example.penta.DataBaseHelper.C_ID;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.penta.DataBaseHelper;
import com.example.penta.R;

public class MemoView extends AppCompatActivity {

    SQLiteDatabase db;
    DataBaseHelper dbHelper;
    TextView title;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_view);
        final long id = getIntent().getExtras().getLong(getString(R.string.row_id));

        dbHelper = new DataBaseHelper(this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from comments_table where " + C_ID + "=" + id, null);
        title = (TextView) findViewById(R.id.title);
        TextView detail = (TextView) findViewById(R.id.detail);
        TextView notetype = (TextView) findViewById(R.id.note_type_ans);
        TextView time = (TextView) findViewById(R.id.alertvalue);
        TextView date = (TextView) findViewById(R.id.datevalue);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                title.setText(cursor.getString(cursor.getColumnIndex(dbHelper.TITLE)));
                detail.setText(cursor.getString(cursor.getColumnIndex(dbHelper.DETAIL)));
                notetype.setText(cursor.getString(cursor.getColumnIndex(dbHelper.TYPE)));
                time.setText(cursor.getString(cursor.getColumnIndex(dbHelper.TIME)));
                date.setText(cursor.getString(cursor.getColumnIndex(dbHelper.DATE)));

            }
            cursor.close();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final long id = getIntent().getExtras().getLong(getString(R.string.rowID));

        switch (item.getItemId()) {
            case R.id.action_back:
                finish();
                return true;
            case R.id.action_edit:
                Intent openEditNote = new Intent(getApplicationContext(), Edit_Note.class);
                openEditNote.putExtra(getString(R.string.intent_row_id), id);
                startActivity(openEditNote);
                return true;

            case R.id.action_discard:
                AlertDialog.Builder builder = new AlertDialog.Builder(MemoView.this);
                builder
                        .setTitle(getString(R.string.delete_title))
                        .setMessage(getString(R.string.delete_message))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.delete(DataBaseHelper.TABLE_NAME, DataBaseHelper.C_ID + "=" + id, null);
                                db.close();
                                Log.e("데이터 베이스","delete comments_table where _id == "+id);
//                                db.execSQL("delete from comments_table where title = '"+ title.getText().toString() +"'");
//                                db.close();
                                finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), null)                        //Do nothing on no
                        .show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}