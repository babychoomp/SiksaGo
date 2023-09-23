package com.example.penta.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

;import com.example.penta.R;

public class bmi extends AppCompatActivity {
    Button SaveProfile;
    RadioGroup SexGroup;
    EditText height,weight,birth;
    String a1,a2;
    double result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_bmi);

        SaveProfile = (Button) findViewById(R.id.SaveProfile);
        SexGroup=(RadioGroup) findViewById(R.id.SexGroup);
        height=(EditText) findViewById(R.id.height);
        weight=(EditText) findViewById(R.id.Weight);
        birth =(EditText)findViewById(R.id.birth);
        SaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), bmi_save.class);
                a2=height.getText().toString();
                a1=weight.getText().toString();
                result=Double.parseDouble(a1)/((Double.parseDouble(a2)/100)*(Double.parseDouble(a2)/100));
                i.putExtra("bmi",result);
                i.putExtra("height",height.getText().toString());
                i.putExtra("weight",weight.getText().toString());
                i.putExtra("birth",birth.getText().toString());
                RadioButton rdoButton = findViewById(SexGroup.getCheckedRadioButtonId());
                String strPgmId = rdoButton.getText().toString().toUpperCase();
                i.putExtra("sex",strPgmId);

                startActivity(i);
            }
        });
    }
}