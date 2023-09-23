package com.example.penta;

import static com.example.penta.home.Home.mDay;
import static com.example.penta.home.Home.mMonth;
import static com.example.penta.home.Home.mYear;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.penta.home.Home;
import com.example.penta.home.WaterItem;
import com.example.penta.memo.MemoItem;
import com.example.penta.search.SikdanItem;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHelper extends SQLiteAssetHelper {

    private static final String DB_NAME = "sikdan.db";
    private static final int DB_VER = 1;
    private static String DB_PATH = "";
    private Context mContext;
    private final static String TAG = "DataBaseHelper";
    private SQLiteDatabase mDataBase;
    public static final String C_ID = "_id";
    public static final String TITLE = "title";
    public static final String TYPE = "type";
    public static final String DETAIL = "description";
    public static final String TIME = "time";
    public static final String DATE = "date";
    public static final String TABLE_NAME="comments_table";

    public static String time = (mYear+"."+(mMonth+1)+"."+mDay);

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
        dataBaseCheck();
    }

    private void dataBaseCheck() {
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbFile.exists()) {
            dbCopy();
            Log.d(TAG,"Database is copied.");
        }
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null) {
            mDataBase.close();
        }
        super.close();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //Toast.makeText(mContext,"onOpen()",Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onOpen() : DB Opening!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 테이블 삭제하고 onCreate() 다시 로드시킨다.
        Log.d(TAG,"onUpgrade() : DB Schema Modified and Excuting onCreate()");
    }

    // db를 assets에서 복사해온다.
    private void dbCopy() {

        try {
            File folder = new File(DB_PATH);
            if (!folder.exists()) {
                folder.mkdir();
            }

            InputStream inputStream = mContext.getAssets().open(DB_NAME);
            String out_filename = DB_PATH + DB_NAME;
            OutputStream outputStream = new FileOutputStream(out_filename);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = inputStream.read(mBuffer)) > 0) {
                outputStream.write(mBuffer,0,mLength);
            }
            outputStream.flush();;
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("dbCopy","IOException 발생함");
        }
    }

    // Detail.java에 사용되는 함수입니다
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }
    //-----------------------------------------------memo---------------------------------------------------------------------------//
    @SuppressLint("Range")
    public List<MemoItem> getMemo() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "title", "description", "type", "time", "date"};
        String tableName = "comments_table";

        qb.setTables(tableName);
        Cursor cursor = qb.query(db, sqlSelect, null, null, null, null, null);
        List<MemoItem> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                MemoItem memoItem = new MemoItem();
                memoItem.setMemoTitle(cursor.getString(cursor.getColumnIndex("_id")));
                memoItem.setMemoTitle(cursor.getString(cursor.getColumnIndex("title")));
                memoItem.setMemoDescription(cursor.getString(cursor.getColumnIndex("description")));
                memoItem.setMemoType(cursor.getString(cursor.getColumnIndex("type")));
                memoItem.setMemoTime(cursor.getString(cursor.getColumnIndex("time")));
                memoItem.setDate(cursor.getString(cursor.getColumnIndex("date")));

                result.add(memoItem);
            }
            while (cursor.moveToNext());
        }
        return result;
    }
    @SuppressLint("Range")
    public List<SikdanItem> getSikdan() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"korean", "grams", "calories", "carbs", "proteins", "fats"};
        String tableName = "contents";

        qb.setTables(tableName);
        Cursor cursor = qb.query(db, sqlSelect, null, null, null, null, null);
        List<SikdanItem> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                SikdanItem sikdanItem = new SikdanItem();
                sikdanItem.setKorean(cursor.getString(cursor.getColumnIndex("korean")));
                sikdanItem.setGrams(cursor.getString(cursor.getColumnIndex("grams")));
                sikdanItem.setCalories(cursor.getString(cursor.getColumnIndex("calories")));
                sikdanItem.setCarbs(cursor.getString(cursor.getColumnIndex("carbs")));
                sikdanItem.setProteins(cursor.getString(cursor.getColumnIndex("proteins")));
                sikdanItem.setFats(cursor.getString(cursor.getColumnIndex("fats")));

                result.add(sikdanItem);
            }
            while (cursor.moveToNext());
        }
        return result;
    }

    @SuppressLint("Range")
    public List<String> getKorean() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"korean"};
        String tableName = "contents";

        qb.setTables(tableName);
        Cursor cursor = qb.query(db, sqlSelect, null, null, null, null, null);
        List<String> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getString(cursor.getColumnIndex("korean")));
            }
            while (cursor.moveToNext());
        }
        return result;
    }

    @SuppressLint("Range")
    public List<SikdanItem> getSikdanByKorean(String korean){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"korean", "grams", "calories", "carbs", "proteins", "fats"};
        String tableName = "contents";

        qb.setTables(tableName);
        Cursor cursor = qb.query(db, sqlSelect, "korean LIKE ?", new String[]{"%"+korean+"%"}, null, null, null);
        List<SikdanItem> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                SikdanItem sikdanItem = new SikdanItem();
                sikdanItem.setKorean(cursor.getString(cursor.getColumnIndex("korean")));
                sikdanItem.setGrams(cursor.getString(cursor.getColumnIndex("grams")));
                sikdanItem.setCalories(cursor.getString(cursor.getColumnIndex("calories")));
                sikdanItem.setCarbs(cursor.getString(cursor.getColumnIndex("carbs")));
                sikdanItem.setProteins(cursor.getString(cursor.getColumnIndex("proteins")));
                sikdanItem.setFats(cursor.getString(cursor.getColumnIndex("fats")));

                result.add(sikdanItem);
            }
            while (cursor.moveToNext());
        }
        return result;
    }
    public boolean updatename(String table, String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("update  " + table + " set name == '" + name+ "'");
        if (table == null) {
            Log.d("Fail", "업뎃에 실패 하였습니다.");
            return false;
        } else {
            Log.d("Success", "업뎃에 성공하였습니다.");
            return true;

        }
    }
    public void insertItems(String InsertTable, String CopyTable, String food_name){
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("INSERT INTO "+InsertTable+"(korean, grams, calories, carbs, proteins, fats, date) " +
                "SELECT korean, grams, calories, carbs, proteins, fats, date FROM "+CopyTable+" WHERE korean == '"+food_name+"'");

        if(InsertTable == null){
            Log.d("Fail", "입력에 실패하였습니다.");
        }else{
            Log.d("Success","입력에 성공하였습니다.");
        }
    }
    public void UpdateDate(String InsertTable, String CopyTable, String food_name, String date){
        insertItems(InsertTable,CopyTable,food_name);
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("UPDATE "+InsertTable+" SET date = '"+date+"' where date is null");
        if (InsertTable == null) {
            Log.d("Fail", "날짜 추가가 실패하였습니다.");
        } else {
            Log.d("Success", "날짜 추가가 성공하였습니다.");
        }
    }
    public void deleteItemsPosition(String DeleteTable, String date, String food_name, int id) {
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("DELETE FROM "+DeleteTable+" WHERE date == '"+date+"' and korean == '"+food_name+"' and _id =="+id);
        if(DeleteTable == null){
            Log.d("Fail", "삭제에 실패하였습니다.");
        }else{
            Log.d("Success","삭제에 성공하였습니다.");
        }
        DB.close();
    }
    public int CountList(String Table, String date){
        SQLiteDatabase DB = this.getReadableDatabase();
        int result = 0;
        Cursor cursor = DB.rawQuery("Select korean from "+Table+" where date == '"+date+"'",null);
        result += cursor.getCount();
        DB.close();
        return result;
    }
    public Boolean insertData(String Table, String korean, int grams, int calories, int carbs, int proteins, int fats, String date) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("korean", korean);
        contentValues.put("grams", grams);
        contentValues.put("calories", calories);
        contentValues.put("carbs", carbs);
        contentValues.put("proteins", proteins);
        contentValues.put("fats", fats);
        contentValues.put("date", date);

        long result = DB.insert(Table, null, contentValues);
        while (true) {
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
    }

    // 목표 설정 코드
    public boolean UpdateWater(int ml) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ml", ml);

        long result = DB.update("water", contentValues, null, null);
        while (true) {
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
    }
    public int getWater(){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select ml from water",null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        Log.e("목표 칼로리 값은 ",String.valueOf(result));
        DB.close();
        return result;
    }
    // 목표 설정 코드
    public boolean updateGoalItem(String Table, int calories, int carbs, int proteins, int fats) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("calories", calories);
        contentValues.put("carbs", carbs);
        contentValues.put("proteins", proteins);
        contentValues.put("fats", fats);

        long result = DB.update(Table, contentValues, "ID = 1", null);
        while (true) {
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
    }

    public int getGoalCalories(){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select calories from goal where ID == 1",null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        Log.e("목표 칼로리 값은 ",String.valueOf(result));
        DB.close();
        return result;
    }
    public int getGoalCarbs(){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select carbs from goal where ID == 1",null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        Log.e("목표 탄수화물 값은 ",String.valueOf(result));
        DB.close();
        return result;
    }
    public int getGoalProteins(){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select proteins from goal where ID == 1",null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        Log.e("목표 단백질 값은 ",String.valueOf(result));
        DB.close();
        return result;
    }
    public int getGoalFats(){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select fats from goal where ID == 1",null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        Log.e("목표 지방 값은 ",String.valueOf(result));
        DB.close();
        return result;
    }

    @SuppressLint("Range")
    public List<SikdanItem> getMorningSikdan() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.d");
        String getTime = dateFormat.format(date);
        String[] sqlSelect = {"korean", "grams", "calories", "carbs", "proteins", "fats", "date", "_id"};
        String tableName = "morning";
        String data = "date = '"+ Home.Time()+"'"; // dialog를 통해서 연월일을 받아오면 select가 가능해진다. -> 새로고침

        qb.setTables(tableName);
        qb.appendWhere(data);
        // Select * from morning where date == getTime
        Cursor cursor = qb.query(db, sqlSelect, null, null, "korean", null, "korean DESC");
//        Cursor cursor = qb.query(db, sqlSelect, null, null, null, null, null);
        List<SikdanItem> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                SikdanItem sikdanItem = new SikdanItem();
                sikdanItem.setKorean(cursor.getString(cursor.getColumnIndex("korean")));
                sikdanItem.setGrams(cursor.getString(cursor.getColumnIndex("grams")));
                sikdanItem.setCalories(cursor.getString(cursor.getColumnIndex("calories")));
                sikdanItem.setCarbs(cursor.getString(cursor.getColumnIndex("carbs")));
                sikdanItem.setProteins(cursor.getString(cursor.getColumnIndex("proteins")));
                sikdanItem.setFats(cursor.getString(cursor.getColumnIndex("fats")));
                sikdanItem.setId((cursor.getInt(7)));

                result.add(sikdanItem);
            }
            while (cursor.moveToNext());
        }
        return result;
    }

    @SuppressLint("Range")
    public List<SikdanItem> getLunchSikdan() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"korean", "grams", "calories", "carbs", "proteins", "fats", "date", "_id"};
        String tableName = "lunch";
        String data = "date = '"+Home.Time()+"'";
        qb.setTables(tableName);
        qb.appendWhere(data);
        Cursor cursor = qb.query(db, sqlSelect, null, null, null, null, "korean DESC");
        List<SikdanItem> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                SikdanItem sikdanItem = new SikdanItem();
                sikdanItem.setKorean(cursor.getString(cursor.getColumnIndex("korean")));
                sikdanItem.setGrams(cursor.getString(cursor.getColumnIndex("grams")));
                sikdanItem.setCalories(cursor.getString(cursor.getColumnIndex("calories")));
                sikdanItem.setCarbs(cursor.getString(cursor.getColumnIndex("carbs")));
                sikdanItem.setProteins(cursor.getString(cursor.getColumnIndex("proteins")));
                sikdanItem.setFats(cursor.getString(cursor.getColumnIndex("fats")));
                sikdanItem.setFats(cursor.getString(cursor.getColumnIndex("date")));
                sikdanItem.setId((cursor.getInt(7)));

                result.add(sikdanItem);
            }
            while (cursor.moveToNext());
        }
        return result;
    }

    @SuppressLint("Range")
    public List<SikdanItem> getDinnerSikdan() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"korean", "grams", "calories", "carbs", "proteins", "fats", "date", "_id"};
        String tableName = "dinner";
        String data = "date = '"+Home.Time()+"'";
        qb.setTables(tableName);
        qb.appendWhere(data);
        Cursor cursor = qb.query(db, sqlSelect, null, null, null, null, "korean DESC");
        List<SikdanItem> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                SikdanItem sikdanItem = new SikdanItem();
                sikdanItem.setKorean(cursor.getString(cursor.getColumnIndex("korean")));
                sikdanItem.setGrams(cursor.getString(cursor.getColumnIndex("grams")));
                sikdanItem.setCalories(cursor.getString(cursor.getColumnIndex("calories")));
                sikdanItem.setCarbs(cursor.getString(cursor.getColumnIndex("carbs")));
                sikdanItem.setProteins(cursor.getString(cursor.getColumnIndex("proteins")));
                sikdanItem.setFats(cursor.getString(cursor.getColumnIndex("fats")));
                sikdanItem.setFats(cursor.getString(cursor.getColumnIndex("date")));
                sikdanItem.setId((cursor.getInt(7)));

                result.add(sikdanItem);
            }
            while (cursor.moveToNext());
        }
        return result;
    }

    @SuppressLint("Range")
    public List<SikdanItem> getSnackSikdan() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"korean", "grams", "calories", "carbs", "proteins", "fats", "date", "_id"};
        String tableName = "snack";
        String data = "date = '"+Home.Time()+"'";
        qb.setTables(tableName);
        qb.appendWhere(data);
        Cursor cursor = qb.query(db, sqlSelect, null, null, null, null, "korean DESC");
        List<SikdanItem> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                SikdanItem sikdanItem = new SikdanItem();
                sikdanItem.setKorean(cursor.getString(cursor.getColumnIndex("korean")));
                sikdanItem.setGrams(cursor.getString(cursor.getColumnIndex("grams")));
                sikdanItem.setCalories(cursor.getString(cursor.getColumnIndex("calories")));
                sikdanItem.setCarbs(cursor.getString(cursor.getColumnIndex("carbs")));
                sikdanItem.setProteins(cursor.getString(cursor.getColumnIndex("proteins")));
                sikdanItem.setFats(cursor.getString(cursor.getColumnIndex("fats")));
                sikdanItem.setFats(cursor.getString(cursor.getColumnIndex("date")));
                sikdanItem.setId((cursor.getInt(7)));

                result.add(sikdanItem);
            }
            while (cursor.moveToNext());
        }
        return result;
    }
}
