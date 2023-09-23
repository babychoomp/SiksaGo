package com.example.penta.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.penta.DataBaseHelper;
import com.example.penta.R;
import com.opencsv.CSVReader;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MorningCamera extends AppCompatActivity {

    TextView foodname;
    TextView calories;
    String food_name = null;
    DataBaseHelper myDb;
    private static final String TAG = "CameraActivity";

    public static final int REQUEST_TAKE_PHOTO = 10;
    public static final int REQUEST_PERMISSION = 11;
    private Context context;
    private ArrayList<String> category = new ArrayList<String>();
    private ArrayList<String> calory =new ArrayList<String>();
    private Button btnCamera, btnSave, btnAdd;
    private ImageView ivCapture;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_morning_camera);

        //타이틀 바 삭제
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        myDb = new DataBaseHelper(this);
        foodname = (TextView)findViewById(R.id.textview1);
        foodname.setText("음식이름");
        calories = (TextView)findViewById(R.id.textview2);
        calories.setText("칼로리");

        //csv 불러오기
        AssetManager csv = getResources().getAssets();
        InputStream is = null;
        String [] splitResult = new String[4];

        try {
            is = csv.open("calorie_data.csv");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String tmp = null;

            while((tmp = br.readLine()) != null) {
                splitResult = tmp.split(",");
                category.add(splitResult[0]);
                calory.add(splitResult[1]);
            }

            //for(int x=0;x<category.size();x++) {
            //    Log.d("category", category.get(x));
            //    Log.d("calory", calory.get(x));
            //}


        } catch (Exception e) {
            e.printStackTrace();
        }

        checkPermission(); //권한체크

        ivCapture = findViewById(R.id.ivCapture); //ImageView 선언
        btnCamera = findViewById(R.id.btnCapture); //Button 선언
        btnSave = findViewById(R.id.btnSave); //Button 선언

        loadImgArr();

        //촬영
        btnCamera.setOnClickListener(v -> captureCamera());

        //저장
        btnSave.setOnClickListener(v -> {

            try {

                BitmapDrawable drawable = (BitmapDrawable) ivCapture.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                //찍은 사진이 없으면
                if (bitmap == null) {
                    Toast.makeText(this, "저장할 사진이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    //저장
                    saveImg();
                    mCurrentPhotoPath = ""; //initialize
                }

            } catch (Exception e) {
                Log.w(TAG, "SAVE ERROR!", e);
            }
        });
    }
    private void captureCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 인텐트를 처리 할 카메라 액티비티가 있는지 확인
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // 촬영한 사진을 저장할 파일 생성
            File photoFile = null;

            try {
                //임시로 사용할 파일이므로 경로는 캐시폴더로
                File tempDir = getCacheDir();

                //임시촬영파일 세팅
                String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
                String imageFileName = "Capture_" + timeStamp + "_"; //ex) Capture_20201206_

                File tempImage = File.createTempFile(
                        imageFileName,  /* 파일이름 */
                        ".jpg",         /* 파일형식 */
                        tempDir      /* 경로 */
                );

                // ACTION_VIEW 인텐트를 사용할 경로 (임시파일의 경로)
                mCurrentPhotoPath = tempImage.getAbsolutePath();

                photoFile = tempImage;

            } catch (IOException e) {
                //에러 로그는 이렇게 관리하는 편이 좋다.
                Log.w(TAG, "파일 생성 에러!", e);
            }

            //파일이 정상적으로 생성되었다면 계속 진행
            if (photoFile != null) {
                //Uri 가져오기
                Uri photoURI = FileProvider.getUriForFile(this,
                        getPackageName() + ".fileprovider",
                        photoFile);
                //인텐트에 Uri담기
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                //인텐트 실행
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public static float[][][][] bitmapData = new float[1][224][224][3];

    //이미지저장 메소드
    private void saveImg() {

        int x, y, pixel;
        String filename = "캡쳐파일" + ".jpg";
        File storageDir = new File(getFilesDir() + "/capture");
        try {
            //저장할 파일 경로
            if (!storageDir.exists()) //폴더가 없으면 생성.
                storageDir.mkdirs();


            // 기존에 있다면 삭제
            File file = new File(storageDir, filename);
            boolean deleted = file.delete();
            Log.w(TAG, "Delete Dup Check : " + deleted);
            FileOutputStream output = null;

            try {
                output = new FileOutputStream(file);
                BitmapDrawable drawable = (BitmapDrawable) ivCapture.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                //Bitmap bitmap = BitmapFactory.decodeResource(drawable.getResources(), Integer.parseInt("캡쳐파일" + ".jpg"));
                BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                bitmap = bitmap.createScaledBitmap(bitmap, 224, 224, true);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output); //해상도에 맞추어 Compress


//                Log.d("test", String.valueOf(bitmap.getHeight()));
//                Log.d("test", String.valueOf(bitmap.getWidth()));
                for (y = 0; y < bitmap.getHeight(); y++) {
                    for (x = 0; x < bitmap.getWidth(); x++) {
                        pixel = bitmap.getPixel(x, y);
//                        Log.d("test", String.valueOf(Color.red(pixel)));
//                        Log.d("test", String.valueOf(Color.green(pixel)));
//                        Log.d("test", String.valueOf(Color.blue(pixel)));
                        bitmapData[0][x][y][0] = Color.red(pixel) / 1.0f;
                        bitmapData[0][x][y][1] = Color.green(pixel) / 1.0f;
                        bitmapData[0][x][y][2] = Color.blue(pixel) / 1.0f;
                    }
                }

                //arrayImg = bitmap2Array(resizeBitmapImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    assert output != null;
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Log.e(TAG, "Captured Saved");
            food_name = foodname.getText().toString();
            DataBaseHelper db = new DataBaseHelper(this.getApplicationContext());
            db.insertItems("morning", "contents", food_name);
            Toast.makeText(this, "음식이 저장되었습니다",  Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.w(TAG, "Capture Saving Error!", e);
            Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();

        }
    }

    private void loadImgArr() {
        try {

            File storageDir = new File(getFilesDir() + "/capture");
            String filename = "캡쳐파일" + ".jpg";

            File file = new File(storageDir, filename);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

            //arrayImg = bitmap2Array(bitmap);
            ivCapture.setImageBitmap(bitmap);

        } catch (Exception e) {
            Log.w(TAG, "Capture loading Error!", e);
            Toast.makeText(this, "load failed", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        float[][] arrayOutput = new float[1][101];

        int x, y;
        Interpreter lite = getTfliteInterpreter("converted_model.tflite");


//               for(y=0;y<224;y++) {
//            for(x=0;x<224;x++) {
//                Log.d("test", String.valueOf(bitmapData[x][y][0]));
//                Log.d("test", String.valueOf(bitmapData[x][y][1]));
//                Log.d("test", String.valueOf(bitmapData[x][y][2]));
//                bitmapData[x][y][0] = Color.red(pixel);
//                bitmapData[x][y][1] = Color.green(pixel);
//                bitmapData[x][y][2] = Color.blue(pixel);
//           }
//        }

        if (lite == null) {
            Log.d("Warning", "No Fucking FIle");
        } else {
            lite.run(bitmapData, arrayOutput);
        }

        //맥스 뽑고 여기에 해당하는 인덱스 뽑아와
        float maxVal = -1 * (Float.MAX_VALUE);
        int indexVal = -1;
        for(x=0;x<101;x++) {
            if(maxVal < arrayOutput[0][x]) {
                maxVal = arrayOutput[0][x];
                indexVal = x;
            }

            if(maxVal < 0.01) {
                indexVal = -1;
            }
        }

        Log.d("index", String.valueOf(indexVal));
        if(indexVal == -1) {
            //음식이 아니다.
            //음식물 쓰레기다
        } else {
            Log.d("category", category.get(indexVal));
            foodname.setText(category.get(indexVal));
            Log.d("calory", calory.get(indexVal));
            calories.setText(calory.get(indexVal)+" kal");
          /*  food_name = foodname.getText().toString();
            DataBaseHelper db = new DataBaseHelper(context.getApplicationContext());
            db.insertItems("dinner", "contents", food_name);


            Toast.makeText(context.getApplicationContext(), "사진값이 저장되었습니다",  Toast.LENGTH_SHORT).show();*/
            //뽑아온 인덱스와 csv 파일에 해당하는 인덱스 출력
        }

        //if(resultVal.)

        try {
            //after capture
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: {
                    if (resultCode == RESULT_OK) {

                        File file = new File(mCurrentPhotoPath);
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(getContentResolver(), Uri.fromFile(file));

                        if (bitmap != null) {
                            ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_UNDEFINED);

//                            //사진해상도가 너무 높으면 비트맵으로 로딩
//                            BitmapFactory.Options options = new BitmapFactory.Options();
//                            options.inSampleSize = 8; //8분의 1크기로 비트맵 객체 생성
//                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

                            Bitmap rotatedBitmap = null;
                            switch (orientation) {

                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    rotatedBitmap = rotateImage(bitmap, 90);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    rotatedBitmap = rotateImage(bitmap, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    rotatedBitmap = rotateImage(bitmap, 270);
                                    break;

                                case ExifInterface.ORIENTATION_NORMAL:
                                default:
                                    rotatedBitmap = bitmap;
                            }

                            //Rotate한 bitmap을 ImageView에 저장
                            ivCapture.setImageBitmap(rotatedBitmap);

                        }
                    }
                    break;
                }
            }

        } catch (Exception e) {
            Log.w(TAG, "onActivityResult Error !", e);
        }
    }

    //카메라에 맞게 이미지 로테이션
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermission(); //권한체크
    }

    //권한 확인
    public void checkPermission() {
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //권한이 없으면 권한 요청
        if (permissionCamera != PackageManager.PERMISSION_GRANTED
                || permissionRead != PackageManager.PERMISSION_GRANTED
                || permissionWrite != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "이 앱을 실행하기 위해 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // 권한이 취소되면 result 배열은 비어있다.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "권한 확인", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }

    }


    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(MorningCamera.this, modelPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


    public static void main(String[] args) throws IOException{
        readCsvData("./calorie_data.csv");
    }

    private static void readCsvData (String path) throws IOException {

        CSVReader reader = new CSVReader(new FileReader(path));
        String[] nextLine;


    }



}



