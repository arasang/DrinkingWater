package com.parksangeun.water.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.parksangeun.water.R;
import com.parksangeun.water.common.adapter.GridAdapter;

/**
 * Created by parksangeun on 2017. 9. 25..
 */

public class IntakeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "IntakeActivity";

    private Toolbar toolbar;
    private GridView gridView;
    private GridAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intake);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridView = (GridView)findViewById(R.id.grid);
        adapter = new GridAdapter(this);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.onItemClick(position, this);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
