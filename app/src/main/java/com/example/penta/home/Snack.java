package com.example.penta.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.penta.DataBaseHelper;
import com.example.penta.Insert.InsertHomeSnack;
import com.example.penta.Main;
import com.example.penta.R;
import com.example.penta.camera.SnackCamera;
import com.example.penta.search.SnackSearchAdapter;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class Snack extends AppCompatActivity {

    RecyclerView recyclerView,snackRe;
    RecyclerView.LayoutManager layoutManager;
    SnackSearchAdapter adapter;
    SnackSearchAdapter snackSearchAdapter;
    ImageButton ibtCamera, ibtKeyboard;
    MaterialSearchBar materialSearchBar;
    List<String> suggestList = new ArrayList<>();

    DataBaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_snack);

        ibtCamera=(ImageButton)findViewById(R.id.ibtCamera);
        ibtKeyboard=(ImageButton)findViewById(R.id.ibtKeyboard);
        recyclerView = (RecyclerView)findViewById(R.id.snack_recycler_search);
        snackRe = (RecyclerView) findViewById(R.id.snack_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        materialSearchBar = (MaterialSearchBar) findViewById(R.id.snack_search_bar);

        database = new DataBaseHelper(this);

        materialSearchBar.setHint("간식 메뉴 검색");
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                List<String> suggest = new ArrayList<>();
                for(String search:suggestList){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){
                        suggest.add(search);
                    }
                    materialSearchBar.setLastSuggestions(suggest);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ibtKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Snack.this, InsertHomeSnack.class);
                startActivity(i);
            }
        });
        ibtCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SnackCamera.class);
                startActivity(i);
            }
        });
        adapter = new SnackSearchAdapter(Snack.this, database.getSikdan());
        recyclerView.setAdapter(adapter);
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    adapter = new SnackSearchAdapter(getBaseContext(), database.getSikdan());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                snackSearchAdapter = new SnackSearchAdapter(getApplicationContext(), database.getLunchSikdan());
                snackSearchAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void startSearch(String text) {
        adapter = new SnackSearchAdapter(this, database.getSikdanByKorean(text));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Snack.this, Main.class);
        startActivity(i);
    }
}