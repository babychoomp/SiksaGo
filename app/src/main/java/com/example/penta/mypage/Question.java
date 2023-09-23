package com.example.penta.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.penta.R;

public class Question extends AppCompatActivity {
    EditText etAddr, etTitle, etMessage;
    Button btSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_question);

        btSend = (Button) findViewById(R.id.bt_send);
        etAddr = (EditText) findViewById(R.id.et_addr);
        etTitle = (EditText) findViewById(R.id.et_title);
        etMessage = (EditText) findViewById(R.id.et_message);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSend(v);
            }
        });
    }
    public void onClickSend(View v){
        sendEmail();}
    protected void sendEmail(){
        String to = etAddr.getText().toString();
        String subject = etTitle.getText().toString();
        String message = etMessage.getText().toString();
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, message);
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "이메일 클라이언트 선택하기 :"));
    }

}