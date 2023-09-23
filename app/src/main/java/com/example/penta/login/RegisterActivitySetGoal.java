package com.example.penta.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.penta.DataBaseHelper;
import com.example.penta.R;
import com.example.penta.home.Home;

public class RegisterActivitySetGoal extends AppCompatActivity {

    EditText edtRcalories, edtRcarbs, edtRproteins, edtRfats;
    Button btnConfirm, Loginxml;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_setgoal);
        ActionBar actionBar = getSupportActionBar();


        edtRcalories = (EditText) findViewById(R.id.EdtRCalories);
        edtRcarbs = (EditText) findViewById(R.id.EdtRCarbs);
        edtRproteins = (EditText) findViewById(R.id.EdtRProteins);
        edtRfats = (EditText) findViewById(R.id.EdtRFats);

        btnConfirm = findViewById(R.id.btn_confirm);

        btnConfirm.setOnClickListener(view -> {
            DataBaseHelper DB = new DataBaseHelper(this);
            int caloriesText = Integer.parseInt(edtRcalories.getText().toString());
            int carbsText = Integer.parseInt(edtRcarbs.getText().toString());
            int proteinsText = Integer.parseInt(edtRproteins.getText().toString());
            int fatsText = Integer.parseInt(edtRfats.getText().toString());

            Boolean updateGoalItem = DB.updateGoalItem("goal", caloriesText, carbsText, proteinsText, fatsText);
            if(updateGoalItem == true){
                Toast.makeText(RegisterActivitySetGoal.this, "목표가 설정되었습니다!", Toast.LENGTH_SHORT).show();
                Intent intenta = new Intent(RegisterActivitySetGoal.this, LoginActivity.class);   // 목표 설정 페이지에서 홈 화면으로 이동
                startActivity(intenta);
            }
            else{
                Toast.makeText(RegisterActivitySetGoal.this, "목표 입력에 실패하였습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
