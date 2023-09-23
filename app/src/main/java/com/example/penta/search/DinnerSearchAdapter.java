package com.example.penta.search;


import static com.example.penta.home.Home.All;
import static com.example.penta.home.Home.DinnerCount;
import static com.example.penta.home.Home.LunchCount;
import static com.example.penta.home.Home.MLDS;
import static com.example.penta.home.Home.MorningCount;
import static com.example.penta.home.Home.SnackCount;
import static com.example.penta.home.Home.TargetKal;
import static com.example.penta.home.Home.crab;
import static com.example.penta.home.Home.crabbar;
import static com.example.penta.home.Home.fat;
import static com.example.penta.home.Home.fatbar;
import static com.example.penta.home.Home.getTime;
import static com.example.penta.home.Home.mDay;
import static com.example.penta.home.Home.mMonth;
import static com.example.penta.home.Home.mYear;
import static com.example.penta.home.Home.probar;
import static com.example.penta.home.Home.protein;
import static com.example.penta.home.Home.proteinbar;
import static com.example.penta.home.Home.tvFatNum;
import static com.example.penta.home.Home.tvProteinNum;
import static com.example.penta.home.Home.tvTanNum;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.metrics.Event;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.penta.DataBaseHelper;
import com.example.penta.R;
import com.example.penta.home.Dinner;
import com.example.penta.home.Home;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DinnerSearchAdapter extends RecyclerView.Adapter<DinnerSearchAdapter.SearchViewHolder>{
    private Context context;
    private List<SikdanItem> sikdans;
    Button delete;
    public static int id;

    public DinnerSearchAdapter(Context context, List<SikdanItem> sikdans){
        this.context = context;
        this.sikdans = sikdans;
    }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.home_items, parent, false);

        SearchViewHolder viewHolder = new SearchViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.korean.setText(sikdans.get(position).getKorean());
        holder.grams.setText(sikdans.get(position).getGrams());
        holder.calories.setText(sikdans.get(position).getCalories());
        holder.txt_id.setText(""+sikdans.get(position).getId());
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return sikdans.size();
    }
    public class SearchViewHolder extends RecyclerView.ViewHolder {
        public TextView korean, grams, calories;
        public EditText txt_id;
        String food_name = null;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_id = (EditText) itemView.findViewById(R.id.text_id);
            korean = (TextView) itemView.findViewById(R.id.text_name);
            food_name = korean.getText().toString();
            grams = (TextView) itemView.findViewById(R.id.text_gram);
            calories = (TextView) itemView.findViewById(R.id.text_cal);
            String time = (mYear+"."+(mMonth+1)+"."+mDay);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count, checked;

                    count = getItemCount();
                    if(count > 0) {
                        checked = getAdapterPosition();
                        if (checked > -1 && checked < count) {
                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.d");
                            String getTime = dateFormat.format(date);

                            food_name = korean.getText().toString();
                            DataBaseHelper db = new DataBaseHelper(context.getApplicationContext());
                            db.UpdateDate("dinner", "contents", food_name, time);
                            Toast.makeText(context.getApplicationContext(), food_name+"이(가) \n저녁 메뉴에 추가되었습니다", Toast.LENGTH_SHORT).show();
                            sikdans.add(sikdans.get(checked));
                            notifyDataSetChanged();
                        }
                    }
                }
            });

            delete = (Button) itemView.findViewById(R.id.btn_delete);
            delete.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    int checked;
                    int id = Integer.parseInt(txt_id.getText().toString());
                    checked = getBindingAdapterPosition();
                    DataBaseHelper db = new DataBaseHelper(context.getApplicationContext());
                    korean = (TextView) itemView.findViewById(R.id.text_name);
                    food_name = korean.getText().toString();
                    db.deleteItemsPosition("dinner", time, food_name, id);

                    Log.e("음식 이름",food_name);

                    checked = getAdapterPosition();
                    sikdans.remove(checked);

                    Toast.makeText(context.getApplicationContext(), food_name+"이(가) 삭제 되었습니다",  Toast.LENGTH_SHORT).show();

                    int SetDinner = db.CountList("dinner",time);

                    String SetSdinner = String.valueOf(SetDinner);

                    DinnerCount.setText(SetSdinner+"개");
                    All.setText(MLDS + "/" + TargetKal);


                    DataBaseHelper dbHelper = new DataBaseHelper(context.getApplicationContext());
                    SQLiteDatabase DB = dbHelper.getReadableDatabase();
                    Cursor cursor = DB.rawQuery("SELECT sum(calories) FROM dinner WHERE date == '"+time+"'", null); //mornig 테이블에서 caloreis 컬럼 더한값
                    Cursor cursorcarbs = DB.rawQuery("SELECT sum(carbs) FROM dinner WHERE date == '"+time+"'", null);
                    Cursor cursorproteins = DB.rawQuery("SELECT sum(proteins) FROM dinner WHERE date == '"+time+"'", null);
                    Cursor cursorfats = DB.rawQuery("SELECT sum(fats) FROM dinner WHERE date == '"+time+"'", null);

                    if (cursor.moveToNext()) {
                        Home.val = cursor.getFloat(0);
                    }
                    if (cursorcarbs.moveToNext()) {
                        Home.crabs = cursorcarbs.getFloat(0);
                    }
                    if (cursorproteins.moveToNext()) {
                        Home.proteins = cursorproteins.getFloat(0);

                    }
                    if (cursorfats.moveToNext()) {
                        Home.fats = cursorfats.getFloat(0);
                    }
                    Home.tvDinner.setText(Home.val + "kcal"); // tvmornig 텍스트 검색값으로 출력
                    Home.result += Home.val; // 총칼로리 계산
                    crab += Home.crabs; // 탄수환물 계산
                    protein += Home.proteins; //단백질 계산
                    fat += Home.fats;// 지방 계산

                    cursor.close();
                    cursorcarbs.close();
                    cursorproteins.close();
                    cursorfats.close();

                    probar.setProgress((int) Home.result); // 프로그레스바 probar 세팅값 result로 설정
                    All.setText(Home.result + "/kcal"); // 총칼로리 출력
                    if(probar == null){
                        probar.setMax(6000);
                    }/*else{
            fatbar.setMax(Integer.parseInt(String.valueOf(txtFats)));
        }*/

                    crabbar.setProgress((int) crab); //
                    tvTanNum.setText((int) crab + "g");

                    proteinbar.setProgress((int) protein);
                    tvProteinNum.setText((int) protein + "g");

                    fatbar.setProgress((int) fat);
                    tvFatNum.setText((int) fat + "g");
                    Home.result=0;
                    crab=0;
                    protein=0;
                    fat=0;
                    notifyDataSetChanged();
                    // listview 갱신.
                }
            }) ;
//            plus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int count, checked;
//
//                    count = getItemCount();
//                    if (count > 0) {
//                        checked = getAdapterPosition();
//                        if (checked > -1 && checked < count) {
//                            long now = System.currentTimeMillis();
//                            Date date = new Date(now);
//                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.d");
//                            String getTime = dateFormat.format(date);
//
//                            food_name = korean.getText().toString();
//                            DataBaseHelper db = new DataBaseHelper(context.getApplicationContext());
//                            db.UpdateDate("morning", "contents", food_name, time);
//                            Toast.makeText(context.getApplicationContext(), food_name+"이 추가되었습니다", Toast.LENGTH_SHORT).show();
//                            count_item = db.CountItems("morning",food_name,time);
//                            if(counting==null){
//                                counting.setText("0개");
//                            }else{
//                                counting.setText(count_item+"개");
//                            }
//                            notifyDataSetChanged();
//                        }
//                    }
//                }
//            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    private OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

}


