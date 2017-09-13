package com.parksangeun.water.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.parksangeun.water.R;
import com.parksangeun.water.common.firebase.FireAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    /** UI **/
    private Button buttonDrink;
    private EditText editDrink;

    /** Variable **/
    private int amount = 0;
    private String date = ""; // 물을 마신 시간을 저장할 변수

    private Handler handler;

    /** FirebaseAuth User **/
    private FireAuth fireAuth; // 공통으로 사용하는 파이어 베이스 인증 클래스
    private FirebaseUser user;

    /** FirebaseDatabase **/
    private FirebaseDatabase fireDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initFire();

    }

    private void initView(){
        buttonDrink = (Button)findViewById(R.id.buttonDrink);
        editDrink = (EditText)findViewById(R.id.editDrink);

        buttonDrink.setOnClickListener(this);
    }

    // TODO: Initiate Firebase Database
    private void initFire(){
        fireAuth = new FireAuth(this);
        fireDB = FirebaseDatabase.getInstance();

        user = fireAuth.getAuth().getCurrentUser();
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
    public void onClick(View view) {
        if (view == buttonDrink) {
            amount = Integer.parseInt(editDrink.getText().toString());

            date = getTime();

            if (amount <= 0) {
                Toast.makeText(this, "올바른 값을 입력해주세요", Toast.LENGTH_SHORT).show();
                editDrink.setText("");

            } else {
                DatabaseReference ref1 = fireDB.getReference("Drink/Uid");
                ref1.setValue(user.getUid());

                DatabaseReference ref2 = fireDB.getReference("Drink/Date");
                ref2.setValue(date);

                DatabaseReference ref3 = fireDB.getReference("Drink/Amount");
                ref3.setValue(amount);

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
}
