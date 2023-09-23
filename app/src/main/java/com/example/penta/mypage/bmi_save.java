package com.example.penta.mypage;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.penta.R;

public class bmi_save extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_bmi_save);

        TextView profilesex = findViewById(R.id.profilesex);
        TextView profilebirth = findViewById(R.id.profilebirth);
        TextView profilehieght = findViewById(R.id.profilehieght);
        TextView profileweight = findViewById(R.id.profileweight);
        TextView BMIresult = findViewById(R.id.BMIresult);
        TextView BMIresult2 = findViewById(R.id.BMIresult2);
        ProgressBar bprobar = findViewById(R.id.BMIProgressBar);

        Intent i = getIntent();
        String sex = i.getStringExtra("sex");
        String height = i.getStringExtra("height");
        String weight = i.getStringExtra("weight");
        String birth = i.getStringExtra("birth");
        double BMIresulta =Double.parseDouble(weight)/((Double.parseDouble(height)/100)*(Double.parseDouble(height)/100));
        if(BMIresulta>=25)
        {
            BMIresult2.setText("비만");
                bprobar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
        }else if(BMIresulta>=23)
        {
            BMIresult2.setText("과체중");
            bprobar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FF8C00")));
        }else if(BMIresulta>=18.5)
        {
            BMIresult2.setText("정상");
            bprobar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#2196F3")));
        }else if(BMIresulta<18.5)
        {
            BMIresult2.setText("저체중");
            bprobar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#CDDC39")));
        }
        String Bmiformat= String.format("%.0f",BMIresulta);
        bprobar.setProgress(Integer.parseInt(Bmiformat));
        BMIresult.setText(Bmiformat);
        profilehieght.setText(height+"cm");
        profileweight.setText(weight+"kg");
        profilebirth.setText(birth+"살");
        profilesex.setText(sex);
    }
}