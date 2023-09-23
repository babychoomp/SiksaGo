package com.example.penta.Insert;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.penta.DataBaseHelper;
import com.example.penta.Main;
import com.example.penta.R;
import com.example.penta.home.Home;


public class InsertHomeMorning extends AppCompatActivity {
    EditText korean, grams, calories, carbs, proteins, fats;
    Button insert, view;
    DataBaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_insert_morning);

        //타이틀 바 삭제
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        korean = findViewById(R.id.korean);
        grams = findViewById(R.id.grams);
        calories = findViewById(R.id.calories);
        carbs = findViewById(R.id.carbs);
        proteins = findViewById(R.id.proteins);
        fats = findViewById(R.id.fats);
        insert = findViewById(R.id.btnInsert);

        DB = new DataBaseHelper(this);

        insert.setOnClickListener(view -> {
            DataBaseHelper dbHelper = new DataBaseHelper(this);
            String koreanText = korean.getText().toString();
            int gramsText = Integer.parseInt(grams.getText().toString());
            int caloriesText = Integer.parseInt(calories.getText().toString());
            int carbsText = Integer.parseInt(carbs.getText().toString());
            int proteinsText = Integer.parseInt(proteins.getText().toString());
            int fatsText = Integer.parseInt(fats.getText().toString());

            Boolean insertFood = DB.insertData("morning",koreanText, gramsText, caloriesText, carbsText, proteinsText, fatsText, Home.Time());
            if(insertFood == true){
                Toast.makeText(InsertHomeMorning.this, "새로운 식단 정보가 입력되었습니다", Toast.LENGTH_SHORT).show();
                Log.d("dbInsert","식단 정보가 입력되었습니다.");
            }
            else{
                Toast.makeText(InsertHomeMorning.this, "식단 정보 입력에 실패하였습니다", Toast.LENGTH_SHORT).show();
                Log.d("dbInsert","입력을 실패했습니다.");
            }
            Intent i = new Intent(InsertHomeMorning.this, Main.class);
            startActivity(i);
        });
    }
}

