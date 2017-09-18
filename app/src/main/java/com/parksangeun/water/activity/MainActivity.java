package com.parksangeun.water.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.parksangeun.water.R;
import com.parksangeun.water.common.Metrics;
import com.parksangeun.water.common.firebase.FireAuth;
import com.parksangeun.water.common.firebase.FireDB;
import com.parksangeun.water.common.task.GetPhotoTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";

    /** UI **/
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Button buttonDrink;
    private EditText editDrink;

    private ImageView imagePhoto;
    private TextView textName;
    private TextView textEmail;

    /** Variable **/
    private int amount = 0;
    private String date = ""; // 물을 마신 시간을 저장할 변수
    private String name = "";
    private String email = "";
    private Bitmap photo = null;

    /** FirebaseAuth User **/
    private FireAuth fireAuth; // 공통으로 사용하는 파이어 베이스 인증 클래스
    private FirebaseUser user;

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
        toggle.syncState();


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

    // TODO: Initiate Firebase Database
    private void initFire() throws IOException {
        fireAuth = new FireAuth(this);
        fireDB = new FireDB();

        user = fireAuth.getAuth().getCurrentUser();

        getUserInfo();
    }

    private void getUserInfo() throws IOException {
        name = user.getDisplayName();
        email = user.getEmail();

        Uri photoURI = user.getPhotoUrl();
        URL photoURL = new URL(photoURI.toString());

        new GetPhotoTask(photoURL.toString(), handler).execute();

        textName.setText(name);
        textEmail.setText(email);

    }

    // TODO: 현재 시간 가져오기
    private String getTime(){
        long longCurrent = System.currentTimeMillis();

        Date date = new Date(longCurrent);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String current = format.format(date);

        return current;
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
                    String uid = fireAuth.getUser().getUid();
                    String nameAmount = "amount";

                    HashMap<String,String> params = new HashMap<>();
                    params.put(date, String.valueOf(amount));

                    fireDB.insertAmount(uid, nameAmount, params);

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
        return false;
    }
}
