package com.parksangeun.water.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.john.waveview.WaveView;
import com.parksangeun.water.R;
import com.parksangeun.water.common.ConvertDate;
import com.parksangeun.water.common.Metrics;
import com.parksangeun.water.common.Water;
import com.parksangeun.water.common.firebase.FireAuth;
import com.parksangeun.water.common.firebase.FireDB;
import com.parksangeun.water.common.task.GetPhotoTask;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";

    /** UI **/
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private WaveView waveView;

    private Button buttonDrink;
    private EditText editDrink;

    private ImageView imagePhoto;
    private TextView textName;
    private TextView textEmail;

    /** Variable **/
    private int amount = 0;
    private String uid = "";
    private String name = "";
    private String email = "";
    // 날짜 변수 //
    private String year = "";
    private String month = "";
    private String day = "";
    private String time = "";

    // 사용자 프로필 변수 //
    private String userFamily = "";
    private String userGiven = "";
    private String dailyGoal = "";
    private String userEmail = "";

    private Bitmap photo = null;
    private HashMap<String,String> date = new HashMap<String,String>(); // 물을 마신 시간을 저장할 변수

    // 사용자가 섭취한 물을 저장할 변수
    private HashMap<String, String> drinkedWater = new HashMap<String,String>();

    /** FirebaseAuth User **/
    private FireAuth fireAuth; // 공통으로 사용하는 파이어 베이스 인증 클래스
    private FirebaseUser fireUser;

    /** FirebaseDatabase **/
    private FireDB fireDB;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case Metrics.GET_FAILED:
                    Toast.makeText(MainActivity.this, R.string.photo_error, Toast.LENGTH_SHORT).show();
                    break;

                case Metrics.GET_SUCCESS:
                    Bundle bundle = msg.getData();

                    byte[] bytePhoto = bundle.getByteArray(Metrics.BYTE_ARRAY_PHOTO);

                    photo = BitmapFactory.decodeByteArray(bytePhoto, 0, bytePhoto.length);

                    imagePhoto.setImageBitmap(photo);

                    imagePhoto.setBackground(new ShapeDrawable(new OvalShape()));
                    imagePhoto.setClipToOutline(true);

                    break;

                // TODO: 회원 정보 가져오기
                case Metrics.GET_USER_SUCCESS:
                    bundle = msg.getData();

                    Log.d(TAG, "Handler Success");
                    userEmail = bundle.getString("userEmail");
                    userFamily = bundle.getString("userFamily");
                    userGiven = bundle.getString("userGiven");
                    dailyGoal = bundle.getString("dailyGoal");

                    calcWater();
                    break;

                case Metrics.GET_USER_FAILED:
                    Toast.makeText(MainActivity.this, "회원 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        try {
            initFire();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "프로필 사진을 가져오지 못했습니다.");
        }
    }


    // TODO: Initiate View
    private void initView(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        waveView = (WaveView)findViewById(R.id.waveView);

        buttonDrink = (Button)findViewById(R.id.buttonDrink);
        editDrink = (EditText)findViewById(R.id.editDrink);

        // TODO: NavigationView에 포함되어 있는 View 생성
        navigationView = (NavigationView)findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        imagePhoto = (ImageView)header.findViewById(R.id.imagePhoto);
        textName = (TextView)header.findViewById(R.id.textName);
        textEmail = (TextView)header.findViewById(R.id.textEmail);

        buttonDrink.setOnClickListener(this);
    }

    // TODO: 목표 양을 표현하기 위한 비율 계산
    private void calcWater(){

        // 로그인 사용자의 uid를 통해 정보 가져오기

        float ratio = 50 / Float.parseFloat(dailyGoal);


        drinkedWater = Water.getToday();

        Iterator<String> iter = drinkedWater.keySet().iterator();

        int totalToday = 0;

        while (iter.hasNext()) {
            String key = iter.next();
            int value = Integer.parseInt(drinkedWater.get(key).toString());
            totalToday += value;
        }

        float tt = ratio * totalToday;

        Log.d(TAG, "tt : " + tt);


    }

    // TODO: Initiate Firebase Database
    private void initFire() throws IOException {
        fireAuth = new FireAuth(this);
        fireDB = new FireDB(handler);

        fireUser = fireAuth.getAuth().getCurrentUser();
        uid = fireUser.getUid();

        getUserInfo();
    }

    private void getUserInfo() throws IOException {
        fireDB.readUserInfo("User", uid);

        name = fireUser.getDisplayName();
        email = fireUser.getEmail();

        Uri photoURI = fireUser.getPhotoUrl();
        URL photoURL = new URL(photoURI.toString());

        new GetPhotoTask(photoURL.toString(), handler).execute();

        textName.setText(name);
        textEmail.setText(email);

    }

    // TODO: 현재 시간 가져오기
    public HashMap<String,String> getTime(){

        ConvertDate convertDate = new ConvertDate();
        HashMap<String,String> currentDate = new HashMap<String, String>();

        year = convertDate.getCurrent(Metrics.YEAR);
        month = convertDate.getCurrent(Metrics.MONTH);
        day = convertDate.getCurrent(Metrics.DAY);
        time = convertDate.getCurrent(Metrics.TIME);
        String dayTime = convertDate.getCurrent(Metrics.DAYTIME);

        currentDate.put("year", year);
        currentDate.put("month", month);
        currentDate.put("day", day);
        currentDate.put("time", time);
        currentDate.put("dayTime", dayTime);

        return currentDate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonDrink) {
            if (editDrink.length() != 0) {
                amount = Integer.parseInt(editDrink.getText().toString());

                date = getTime();

                if (amount <= 0) {
                    Toast.makeText(this, "올바른 값을 입력해주세요", Toast.LENGTH_SHORT).show();
                    editDrink.setText("");

                } else {
                    HashMap<String,String> param = new HashMap<String,String>();
                    param.put(date.get("dayTime"), String.valueOf(amount));

                    fireDB.insertStringDB(Metrics.WATER, uid, date.get("year"),
                            date.get("month"),date.get("day"), param);

                }
            } else {
                Toast.makeText(this, "올바른 값을 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navi_my:
                Intent intent = new Intent(MainActivity.this, MyInfoActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        }
    }
}
