package com.example.penta.home;//오브젝트 파일명

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.DatePicker;
import android.app.DatePickerDialog;

import java.util.Calendar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.penta.DataBaseHelper;
import com.example.penta.R;
import com.example.penta.mypage.Goal;
import com.example.penta.search.DinnerSearchAdapter;
import com.example.penta.search.LunchSearchAdapter;
import com.example.penta.search.MorningSearchAdapter;
import com.example.penta.search.SnackSearchAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Home extends Fragment{

    public int Water;
    private View view;
    private ImageView Memo;
    private Button Morning;
    private Button Lunch;
    private Button Dinner;
    private Button Snack;
    private ImageView imgCal;
    private ImageButton plus, minus;
    private TextView tvWater, Day, tvtTarget;
    public static ProgressBar probar, fatbar, proteinbar, crabbar;
    public static float result, fat, protein, crab, val, fats, proteins, crabs;

    public static String TargetKal;
    public static String MLDS;
    Integer M = 0;
    Integer L = 0;
    Integer D = 0;
    Integer S = 0;
    RecyclerView morningRe, lunchRe, dinnerRe, snackRe;
    public static TextView MorningCount, LunchCount, DinnerCount, SnackCount, All, tvMorning, tvLunch, tvDinner, tvSnack, tvTanNum, tvProteinNum, tvFatNum;
    MorningSearchAdapter morningSearchAdapter;
    LunchSearchAdapter lunchSearchAdapter;
    DinnerSearchAdapter dinnerSearchAdapter;
    SnackSearchAdapter snackSearchAdapter;

    public static int mYear;
    public static int mMonth;
    public static int mDay;
    public static String time = (mYear+"."+(mMonth+1)+"."+mDay);
    public static long now = System.currentTimeMillis();
    public static Date date = new Date(now);
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.d");
    public static String getTime = dateFormat.format(date);
    DataBaseHelper database;
    private SwipeRefreshLayout swipeRefreshLayout;

    Button btnsetgoal;
    private static Toast toast = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        imgCal = (ImageView) view.findViewById(R.id.imgCal);
        plus = (ImageButton) view.findViewById(R.id.plus);
        minus = (ImageButton) view.findViewById(R.id.minus);

        Morning = (Button) view.findViewById(R.id.btnFirst);
        Lunch = (Button) view.findViewById(R.id.btnSecond);
        Dinner = (Button) view.findViewById(R.id.btnThird);
        Snack = (Button) view.findViewById(R.id.btnFourth);

        Day = (TextView) view.findViewById(R.id.tvDay);
        All = (TextView) view.findViewById(R.id.tvAll);
        tvMorning = (TextView) view.findViewById(R.id.tvMorning);
        tvLunch = (TextView) view.findViewById(R.id.tvLunch);
        tvDinner = (TextView) view.findViewById(R.id.tvDinner);
        tvSnack = (TextView) view.findViewById(R.id.tvSnack);
        tvWater = (TextView) view.findViewById(R.id.tvWater2);

        tvTanNum = (TextView) view.findViewById(R.id.tvtanNum);
        tvProteinNum = (TextView) view.findViewById(R.id.tvproteinNum);
        tvFatNum = (TextView) view.findViewById(R.id.tvfatNum);
        probar = (ProgressBar) view.findViewById(R.id.allProgressbar);
        fatbar = (ProgressBar) view.findViewById(R.id.fatProgress);
        proteinbar = (ProgressBar) view.findViewById(R.id.proteinProgress);
        crabbar = (ProgressBar) view.findViewById(R.id.tanProgress);

        morningRe = (RecyclerView) view.findViewById(R.id.morning_recycler);
        lunchRe = (RecyclerView) view.findViewById(R.id.lunch_recycler);
        dinnerRe = (RecyclerView) view.findViewById(R.id.dinner_recycler);
        snackRe = (RecyclerView) view.findViewById(R.id.snack_recycler);

        MorningCount = (TextView) view.findViewById(R.id.MorningCount);
        LunchCount = (TextView) view.findViewById(R.id.LunchCount);
        DinnerCount = (TextView) view.findViewById(R.id.DinnerCount);
        SnackCount = (TextView) view.findViewById(R.id.SnackCount);

        tvtTarget = (TextView) view.findViewById(R.id.tvtTarget);

        btnsetgoal = (Button) view.findViewById(R.id.btnSetGoal);
        TargetKal = tvtTarget.getText().toString();
        M = parseInt(tvMorning.getText().toString());
        L = parseInt(tvLunch.getText().toString());
        D = parseInt(tvDinner.getText().toString());
        S = parseInt(tvSnack.getText().toString());

        Integer IMLDS = (M+L+D+S);
        MLDS = IMLDS.toString();
        morningRe.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        morningRe.setHasFixedSize(true);
        lunchRe.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        lunchRe.setHasFixedSize(true);
        dinnerRe.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        dinnerRe.setHasFixedSize(true);
        snackRe.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        snackRe.setHasFixedSize(true);

        swipeRefreshLayout = view.findViewById(R.id.swiperefreshlayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                morningSearchAdapter = new MorningSearchAdapter(getContext().getApplicationContext(), database.getMorningSikdan());
                morningSearchAdapter.notifyDataSetChanged();
                morningRe.setAdapter(morningSearchAdapter);

                lunchSearchAdapter = new LunchSearchAdapter(getContext().getApplicationContext(), database.getLunchSikdan());
                lunchSearchAdapter.notifyDataSetChanged();
                lunchRe.setAdapter(lunchSearchAdapter);

                dinnerSearchAdapter = new DinnerSearchAdapter(getContext().getApplicationContext(), database.getDinnerSikdan());
                dinnerSearchAdapter.notifyDataSetChanged();
                dinnerRe.setAdapter(dinnerSearchAdapter);

                snackSearchAdapter = new SnackSearchAdapter(getContext().getApplicationContext(), database.getSnackSikdan());
                snackSearchAdapter.notifyDataSetChanged();
                snackRe.setAdapter(snackSearchAdapter);

                DataBaseHelper db = new DataBaseHelper(getContext().getApplicationContext());
                int SetMorning = db.CountList("morning",Time());
                int SetLunch = db.CountList("lunch",Time());
                int SetDinner = db.CountList("dinner",Time());
                int SetSnack = db.CountList("snack",Time());
                String SetSmorning = String.valueOf(SetMorning);
                String SetSlunch = String.valueOf(SetLunch);
                String SetSdinner = String.valueOf(SetDinner);
                String SetSsnack = String.valueOf(SetSnack);

                MorningCount.setText(SetSmorning+"개");
                LunchCount.setText(SetSlunch+"개");
                DinnerCount.setText(SetSdinner+"개");
                SnackCount.setText(SetSsnack+"개");

                All.setText(MLDS + "/" + TargetKal);
                getVal();
                getGoalValue();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        database = new DataBaseHelper(getContext().getApplicationContext());

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Day.setText((month+1)+"월 "+dayOfMonth+"일");
                mMonth = month;
                mYear = year;
                mDay = dayOfMonth;
                DataBaseHelper db = new DataBaseHelper(getContext().getApplicationContext());
                int SetMorning = db.CountList("morning",Time());
                int SetLunch = db.CountList("lunch",Time());
                int SetDinner = db.CountList("dinner",Time());
                int SetSnack = db.CountList("snack",Time());
                String SetSmorning = String.valueOf(SetMorning);
                String SetSlunch = String.valueOf(SetLunch);
                String SetSdinner = String.valueOf(SetDinner);
                String SetSsnack = String.valueOf(SetSnack);
                MorningCount.setText(SetSmorning+"개");
                LunchCount.setText(SetSlunch+"개");
                DinnerCount.setText(SetSdinner+"개");
                SnackCount.setText(SetSsnack+"개");
                morningSearchAdapter = new MorningSearchAdapter(getContext().getApplicationContext(), database.getMorningSikdan());
                morningSearchAdapter.notifyDataSetChanged();
                morningRe.setAdapter(morningSearchAdapter);
                lunchSearchAdapter = new LunchSearchAdapter(getContext().getApplicationContext(), database.getLunchSikdan());
                lunchSearchAdapter.notifyDataSetChanged();
                lunchRe.setAdapter(lunchSearchAdapter);
                dinnerSearchAdapter = new DinnerSearchAdapter(getContext().getApplicationContext(), database.getDinnerSikdan());
                dinnerSearchAdapter.notifyDataSetChanged();
                dinnerRe.setAdapter(dinnerSearchAdapter);
                snackSearchAdapter = new SnackSearchAdapter(getContext().getApplicationContext(), database.getSnackSikdan());
                snackSearchAdapter.notifyDataSetChanged();
                snackRe.setAdapter(snackSearchAdapter);

            }
        }, mYear, mMonth, mDay);
        imgCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgCal.isClickable()) {
                    datePickerDialog.show();
                }
            }
        });

        btnsetgoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Goal.class);
                startActivity(i);
            }
        });

        Morning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.example.penta.home.Morning.class);
                startActivity(intent);
            }
        });
        DataBaseHelper db = new DataBaseHelper(getContext().getApplicationContext());
        int SetMorning = db.CountList("morning",Time());
        int SetLunch = db.CountList("lunch",Time());
        int SetDinner = db.CountList("dinner",Time());
        int SetSnack = db.CountList("snack",Time());
        String SetSmorning = String.valueOf(SetMorning);
        String SetSlunch = String.valueOf(SetLunch);
        String SetSdinner = String.valueOf(SetDinner);
        String SetSsnack = String.valueOf(SetSnack);

        MorningCount.setText(SetSmorning+"개");
        LunchCount.setText(SetSlunch+"개");
        DinnerCount.setText(SetSdinner+"개");
        SnackCount.setText(SetSsnack+"개");

        morningSearchAdapter = new MorningSearchAdapter(getContext().getApplicationContext(), database.getMorningSikdan());
        morningSearchAdapter.notifyDataSetChanged();
        morningRe.setAdapter(morningSearchAdapter);

        Lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.example.penta.home.Lunch.class);
                startActivity(intent);
            }
        });

        lunchSearchAdapter = new LunchSearchAdapter(getContext().getApplicationContext(), database.getLunchSikdan());
        lunchRe.setAdapter(lunchSearchAdapter);

        Dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.example.penta.home.Dinner.class);
                startActivity(intent);
            }
        });

        dinnerSearchAdapter = new DinnerSearchAdapter(getContext().getApplicationContext(), database.getDinnerSikdan());
        dinnerRe.setAdapter(dinnerSearchAdapter);


        Snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.example.penta.home.Snack.class);
                startActivity(intent);
            }
        });

        snackSearchAdapter = new SnackSearchAdapter(getContext().getApplicationContext(), database.getSnackSikdan());
        snackRe.setAdapter(snackSearchAdapter);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Water > 0) {
                    Water -= 250;
                    tvWater.setText(" "+Water + " ml");
                } else {
                    tvWater.setText(" 0 ml");
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Water += 250;
                tvWater.setText(" "+Water + "ml");
            }
        });
        Day.setText(getMonth()+"월 "+getDay()+"일");
        TargetKal = tvtTarget.getText().toString();
        M = parseInt(tvMorning.getText().toString());
        L = parseInt(tvLunch.getText().toString());
        D = parseInt(tvDinner.getText().toString());
        S = parseInt(tvSnack.getText().toString());
        All.setText(M + L + D + S + "/" + TargetKal);

        getVal();
        getGoalValue();
        return view;
    }
    public String getMonth(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat month = new SimpleDateFormat("MM");
        String getMonth = month.format(date);
        return getMonth;
    }
    public static String getDay(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat day = new SimpleDateFormat("d");
        String getDay = day.format(date);
        return getDay;
    }
    public String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.d");
        String getTime = dateFormat.format(date);
        return getTime;
    }
    public static String Time(){
        String time = (mYear+"."+(mMonth+1)+"."+mDay);
        return time;
    }
    public void getVal() {

        DataBaseHelper dbHelper = new DataBaseHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT sum(calories) FROM morning WHERE date == '"+Time()+"'", null); //mornig 테이블에서 caloreis 컬럼 더한값
        Cursor cursorcarbs = db.rawQuery("SELECT sum(carbs) FROM morning WHERE date == '"+Time()+"'", null);
        Cursor cursorproteins = db.rawQuery("SELECT sum(proteins) FROM morning WHERE date == '"+Time()+"'", null);
        Cursor cursorfats = db.rawQuery("SELECT sum(fats) FROM morning WHERE date == '"+Time()+"'", null);

        if (cursor.moveToNext()) {
            val = cursor.getFloat(0);
        }
        if (cursorcarbs.moveToNext()) {
            crabs = cursorcarbs.getFloat(0);
        }
        if (cursorproteins.moveToNext()) {
            proteins = cursorproteins.getFloat(0);

        }
        if (cursorfats.moveToNext()) {
            fats = cursorfats.getFloat(0);
        }
        tvMorning.setText(val + "kcal"); // tvmornig 텍스트 검색값으로 출력
        result += val; // 총칼로리 계산
        crab += crabs; // 탄수환물 계산
        protein += proteins; //단백질 계산
        fat += fats;// 지방 계산

        cursor.close();
        cursorcarbs.close();
        cursorproteins.close();
        cursorfats.close();

        Cursor cursor2 = db.rawQuery("SELECT sum(calories) FROM lunch WHERE date == '"+Time()+"'", null); //mornig 테이블에서 caloreis 컬럼 더한값
        Cursor cursorcarbs2 = db.rawQuery("SELECT sum(carbs) FROM lunch WHERE date == '"+Time()+"'", null);
        Cursor cursorproteins2 = db.rawQuery("SELECT sum(proteins) FROM lunch WHERE date == '"+Time()+"'", null);
        Cursor cursorfats2 = db.rawQuery("SELECT sum(fats) FROM lunch WHERE date == '"+Time()+"'", null);
        if (cursor2.moveToNext()) {
            val = cursor2.getFloat(0);
        }
        if (cursorcarbs2.moveToNext()) {
            crabs = cursorcarbs2.getFloat(0);
        }
        if (cursorproteins2.moveToNext()) {
            proteins = cursorproteins2.getFloat(0);

        }
        if (cursorfats2.moveToNext()) {
            fats = cursorfats2.getFloat(0);
        }
        tvLunch.setText(val + "kcal");
        result += val;
        crab += crabs;
        protein += proteins;
        fat += fats;
        cursor2.close();
        cursorcarbs2.close();
        cursorproteins2.close();
        cursorfats2.close();

        Cursor cursor3 = db.rawQuery("SELECT sum(calories) FROM dinner WHERE date == '"+Time()+"'", null); //mornig 테이블에서 caloreis 컬럼 더한값
        Cursor cursorcarbs3 = db.rawQuery("SELECT sum(carbs) FROM dinner WHERE date == '"+Time()+"'", null);
        Cursor cursorproteins3 = db.rawQuery("SELECT sum(proteins) FROM dinner WHERE date == '"+Time()+"'", null);
        Cursor cursorfats3 = db.rawQuery("SELECT sum(fats) FROM dinner WHERE date == '"+Time()+"'", null);
        if (cursor3.moveToNext()) {
            val = cursor3.getFloat(0);
        }
        if (cursorcarbs3.moveToNext()) {
            crabs = cursorcarbs3.getFloat(0);
        }
        if (cursorproteins3.moveToNext()) {
            proteins = cursorproteins3.getFloat(0);

        }
        if (cursorfats3.moveToNext()) {
            fats = cursorfats3.getFloat(0);
        }
        tvDinner.setText(val + "kcal");
        result += val;
        crab += crabs;
        protein += proteins;
        fat += fats;
        cursor3.close();
        cursorcarbs3.close();
        cursorproteins3.close();
        cursorfats3.close();

        Cursor cursor4 = db.rawQuery("SELECT sum(calories) FROM snack WHERE date == '"+Time()+"'", null); //mornig 테이블에서 caloreis 컬럼 더한값
        Cursor cursorcarbs4 = db.rawQuery("SELECT sum(carbs) FROM snack WHERE date == '"+Time()+"'", null);
        Cursor cursorproteins4 = db.rawQuery("SELECT sum(proteins) FROM snack WHERE date == '"+Time()+"'", null);
        Cursor cursorfats4 = db.rawQuery("SELECT sum(fats) FROM snack WHERE date == '"+Time()+"'", null);
        if (cursor4.moveToNext()) {
            val = cursor4.getFloat(0);
        }
        if (cursorcarbs4.moveToNext()) {
            crabs = cursorcarbs4.getFloat(0);
        }
        if (cursorproteins4.moveToNext()) {
            proteins = cursorproteins4.getFloat(0);

        }
        if (cursorfats4.moveToNext()) {
            fats = cursorfats4.getFloat(0);
        }
        tvSnack.setText(val + "kcal");
        result += val;
        crab += crabs;
        protein += proteins;
        fat += fats;
        cursor4.close();
        cursorcarbs4.close();
        cursorproteins4.close();
        cursorfats4.close();
        dbHelper.close();
    }

    private void showToast(String message){
        if(toast != null){
            toast.cancel();
            toast = Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            toast = Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void getGoalValue() {

        // 프로그래스바 애니메이션
        ProgressBarAnimation anim1 = new ProgressBarAnimation(probar, 0, result);
        anim1.setDuration(3000);
        probar.startAnimation(anim1);
        ProgressBarAnimation anim2 = new ProgressBarAnimation(crabbar, 0, crab);
        anim2.setDuration(4000);
        crabbar.startAnimation(anim2);
        ProgressBarAnimation anim3 = new ProgressBarAnimation(proteinbar, 0, protein);
        anim3.setDuration(4000);
        proteinbar.startAnimation(anim3);
        ProgressBarAnimation anim4 = new ProgressBarAnimation(fatbar, 0, fat);
        anim4.setDuration(4000);
        fatbar.startAnimation(anim4);

        // 프로그래스바 퍼센트별 색깔
        if (result >= (database.getGoalCalories()))
            probar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
        else if (result >= (database.getGoalCalories() * 0.6))
            probar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FF8C00")));
        else if (result >= (database.getGoalCalories() * 0.3))
            probar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#CDDC39")));
        else
            probar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#2196F3")));

        if (crab >= (database.getGoalCarbs()))
            crabbar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FF0080")));
        else if (crab >= (database.getGoalCarbs() / 2))
            crabbar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
        else
            crabbar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FFA500")));


        if (protein >= (database.getGoalProteins()))
            proteinbar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FF0080")));
        else if (protein >= (database.getGoalProteins() / 2))
            proteinbar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
        else
            proteinbar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FFA500")));


        if (fat >= (database.getGoalFats()))
            fatbar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FF0080")));
        else if (fat >= (database.getGoalFats() / 2))
            fatbar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
        else
            fatbar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FFA500")));


        tvtTarget.setText(database.getGoalCalories()+"kcal");

        probar.setProgress((int) result); // 프로그레스바 probar 세팅값 result로 설정
        All.setText(result + " / " + database.getGoalCalories() + " kcal"); // 총칼로리 출력
        probar.setMax(database.getGoalCalories());

        crabbar.setProgress((int) crab); //
        tvTanNum.setText((int) crab + " / " + database.getGoalCarbs() + "g");
        crabbar.setMax(database.getGoalCarbs());

        proteinbar.setProgress((int) protein);
        tvProteinNum.setText((int) protein + " / " + database.getGoalProteins() + "g");
        proteinbar.setMax(database.getGoalProteins());

        fatbar.setProgress((int) fat);
        tvFatNum.setText((int) fat + " / " + database.getGoalFats() + "g");
        fatbar.setMax(database.getGoalFats());

        result = 0;
        crab = 0;
        protein = 0;
        fat = 0;
    }
    public class ProgressBarAnimation extends Animation {
        private ProgressBar progressBar;
        private float from;
        private float to;

        public ProgressBarAnimation(ProgressBar progressBar, float from, float to){
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        morningSearchAdapter = new MorningSearchAdapter(getContext().getApplicationContext(), database.getMorningSikdan());
        morningSearchAdapter.notifyDataSetChanged();
        morningRe.setAdapter(morningSearchAdapter);

        lunchSearchAdapter = new LunchSearchAdapter(getContext().getApplicationContext(), database.getLunchSikdan());
        lunchSearchAdapter.notifyDataSetChanged();
        lunchRe.setAdapter(lunchSearchAdapter);

        dinnerSearchAdapter = new DinnerSearchAdapter(getContext().getApplicationContext(), database.getDinnerSikdan());
        dinnerSearchAdapter.notifyDataSetChanged();
        dinnerRe.setAdapter(dinnerSearchAdapter);

        snackSearchAdapter = new SnackSearchAdapter(getContext().getApplicationContext(), database.getSnackSikdan());
        snackSearchAdapter.notifyDataSetChanged();
        snackRe.setAdapter(snackSearchAdapter);

        DataBaseHelper db = new DataBaseHelper(getContext().getApplicationContext());
        int SetMorning = db.CountList("morning",Time());
        int SetLunch = db.CountList("lunch",Time());
        int SetDinner = db.CountList("dinner",Time());
        int SetSnack = db.CountList("snack",Time());
        String SetSmorning = String.valueOf(SetMorning);
        String SetSlunch = String.valueOf(SetLunch);
        String SetSdinner = String.valueOf(SetDinner);
        String SetSsnack = String.valueOf(SetSnack);

        MorningCount.setText(SetSmorning+"개");
        LunchCount.setText(SetSlunch+"개");
        DinnerCount.setText(SetSdinner+"개");
        SnackCount.setText(SetSsnack+"개");
        getVal();
        getGoalValue();
    }
}