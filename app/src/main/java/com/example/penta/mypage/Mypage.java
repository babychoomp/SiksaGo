package com.example.penta.mypage;//오브젝트 파일명

import static com.example.penta.login.LoginActivity.id_login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.penta.DataBaseHelper;
import com.example.penta.R;
import com.example.penta.login.DeleteRequest;
import com.example.penta.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Mypage extends Fragment {
    private View view;
    TextView Profile;
    Button btnPedometer, btnWeight, btnEnquiry,btnBMI, btnLogOut, btnWithdrawal;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mypage, container, false);
        btnPedometer = (Button) view.findViewById(R.id.btnPedometer);
        btnWeight = (Button) view.findViewById(R.id.btngoal);
        btnEnquiry = (Button) view.findViewById(R.id.btnEnquiry);
        btnBMI = (Button) view.findViewById(R.id.btnBMI);
        btnLogOut = (Button) view.findViewById(R.id.btnLogOut);
        btnWithdrawal = (Button) view.findViewById(R.id.btnWithdrawal);
        Profile= (TextView) view.findViewById(R.id.profile);

        btnPedometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Pedometer.class);
                startActivity(i);
            }
        });
        btnBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), bmi.class);
                startActivity(i);
            }
        });
        btnEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Question.class);
                startActivity(i);
            }
        });
        btnWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Goal.class);
                startActivity(i);
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                DeleteRequest deleteRequest = new DeleteRequest(id_login, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(deleteRequest);

                Toast.makeText(getActivity(), "다음에 또 봐요!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        getname();
        return view;
    }

    public void getname() {
        DataBaseHelper dbHelper = new DataBaseHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM logindb", null);
        if (cursor.moveToNext()) {
            String val = cursor.getString(0);
            cursor.close();
            Profile.setText(val + "님 환영합니다.");
        }
    }
}


