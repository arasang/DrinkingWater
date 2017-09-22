package com.parksangeun.water.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parksangeun.water.R;
import com.parksangeun.water.common.data.Metrics;
import com.parksangeun.water.fragment.MyInfoFragment;
import com.parksangeun.water.fragment.NameFragment;

/**
 * Created by parksangeun on 2017. 9. 18..
 */

public class MyInfoActivity extends AppCompatActivity implements MyInfoFragment.setChangeFragment {
    private static final String TAG = "MyInfoActivity";

    private NameFragment nameFragment;
    private MyInfoFragment myinfoFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        /** TitleBar 설정 **/
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();

        myinfoFragment = new MyInfoFragment();
        nameFragment = new NameFragment();

        transaction.add(R.id.container, myinfoFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.myinfo,menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                Log.d(TAG, "submit Click");
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnChangeFragment(int position) {
        // TODO: MyInfoFragment Event
        switch (position) {
            case Metrics.CLICK_NAME:
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, nameFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case Metrics.CLICK_GOAL:
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, nameFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
