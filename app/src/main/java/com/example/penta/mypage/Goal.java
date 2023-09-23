package com.example.penta.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.penta.DataBaseHelper;
import com.example.penta.Main;
import com.example.penta.R;

public class Goal extends AppCompatActivity {
    EditText setcalories, setcarbs, setproteins, setfats;
    Button savegoal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_goal);

        setcalories=(EditText) findViewById(R.id.goalCalories);
        setcarbs=(EditText) findViewById(R.id.goalCarbs);
        setproteins=(EditText) findViewById(R.id.goalProteins);
        setfats=(EditText) findViewById(R.id.goalFats);

        savegoal=(Button) findViewById(R.id.Savegoal);

        savegoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataBaseHelper DB = new DataBaseHelper(Goal.this);

                int caloriesText = Integer.parseInt(setcalories.getText().toString());
                int carbsText = Integer.parseInt(setcarbs.getText().toString());
                int proteinsText = Integer.parseInt(setproteins.getText().toString());
                int fatsText = Integer.parseInt(setfats.getText().toString());

                Boolean updateGoalItem = DB.updateGoalItem("goal", caloriesText, carbsText, proteinsText, fatsText);
                if(updateGoalItem == true){
                    Toast.makeText(Goal.this, "목표가 설정되었습니다!", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(Goal.this, "목표 입력에 실패하였습니다", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}