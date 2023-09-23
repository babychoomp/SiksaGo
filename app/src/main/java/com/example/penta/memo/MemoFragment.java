package com.example.penta.memo;//오브젝트 파일명

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.penta.DataBaseHelper;
import com.example.penta.R;

public class MemoFragment extends Fragment {
    private View view;
    SQLiteDatabase db;
    DataBaseHelper mDbHelper;
    private ListView list;
    SimpleCursorAdapter adapter;
    String[] from = {mDbHelper.TITLE, mDbHelper.DETAIL, mDbHelper.TYPE, mDbHelper.TIME, mDbHelper.DATE};
    final String[] column = {mDbHelper.C_ID, mDbHelper.TITLE, mDbHelper.DETAIL, mDbHelper.TYPE, mDbHelper.TIME, mDbHelper.DATE};
    int[] to = {R.id.title, R.id.Detail, R.id.type, R.id.time, R.id.date};
    ImageView create;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.memo_main, container, false);
        list = (ListView) view.findViewById(R.id.commentslist);
        mDbHelper = new DataBaseHelper(getActivity());
        db = mDbHelper.getWritableDatabase();
        final ImageView alarmImage = (ImageView) view.findViewById(R.id.alarmImage);

        String[] from = {mDbHelper.TITLE, mDbHelper.DETAIL, mDbHelper.TYPE, mDbHelper.TIME, mDbHelper.DATE};
        final String[] column = {mDbHelper.C_ID, mDbHelper.TITLE, mDbHelper.DETAIL, mDbHelper.TYPE, mDbHelper.TIME, mDbHelper.DATE};
        int[] to = {R.id.title, R.id.Detail, R.id.type, R.id.time, R.id.date};

        final Cursor cursor = db.query("comments_table", column, null, null, null, null, null);
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.memo_list, cursor, from, to, 0);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MemoView.class);
                intent.putExtra(getString(R.string.rodId), id);
                startActivity(intent);
            }
        });
        setHasOptionsMenu(true);
        create = (ImageView) view.findViewById(R.id.memo_plus);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openCreateNote = new Intent(getActivity(), CreateMemo.class);
                startActivity(openCreateNote);
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        final Cursor cursor = db.query("comments_table", column, null, null, null, null, null);
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.memo_list, cursor, from, to, 0);

        list.setAdapter(adapter);
    }
}