package com.example.penta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.example.penta.home.Home;
import com.example.penta.memo.MemoFragment;
import com.example.penta.mypage.Mypage;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class

Main extends AppCompatActivity
{
    long backKeyPressedTime = 0;
    private BottomNavigationView bottomNavigationView;//바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Home home_frag;
    private Mypage mypage_frag;
    private MemoFragment memo_frag;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.action_home:
                        setFrag(0);
                        break;
                    case R.id.action_memo:
                        setFrag(1);
                        break;
                    case R.id.action_mypage:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });

        home_frag = new Home();
        memo_frag = new MemoFragment();
        mypage_frag=new Mypage();
        setFrag(0);//첫 프래그먼트 화면 지정
    }

    //프레그먼트 교체
    private void setFrag(int n)
    {
        fm = getSupportFragmentManager();
        ft= fm.beginTransaction();
        switch (n)
        {
            case 0:
                ft.replace(R.id.Main_Frame,home_frag);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.Main_Frame,memo_frag);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.Main_Frame,mypage_frag);
                ft.commit();
                break;
        }
    }
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() > backKeyPressedTime + 2500){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2500){
            finishAffinity();
        }
    }
}