package com.parksangeun.water.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.parksangeun.water.R;
import com.parksangeun.water.common.data.UserData;
import com.parksangeun.water.common.firebase.FireDB;

import java.util.HashMap;

/**
 * Created by parksangeun on 2017. 9. 25..
 */

public class GoalFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "GoalFragment";

    private RelativeLayout buttonEdit;
    private TextView textGoal;
    private SeekBar seekbar;

    private String goal = "";

    private Context context;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.myinfo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                String goal = this.goal;

                FireDB db = new FireDB(context);
                String uid = UserData.getUid();

                HashMap<String,String> params = new HashMap<String,String>();
                params.put("DailyGoal", goal);

                db.insertStringDB("User", uid, "", "", "", params);

                UserData.setDailyGoal(goal);

                getFragmentManager().popBackStack();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal, container, false);

        buttonEdit = (RelativeLayout) view.findViewById(R.id.buttonEdit);
        textGoal = (TextView) view.findViewById(R.id.textGoal);
        seekbar = (SeekBar) view.findViewById(R.id.seekbar);

        int progress = seekbar.getProgress();

        textGoal.setText(String.valueOf(progress));

        buttonEdit.setOnClickListener(this);
        seekbar.setOnSeekBarChangeListener(this);

        setHasOptionsMenu(true); // 메뉴 구성

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        if (v == buttonEdit) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            final EditText edit = new EditText(context);

            dialog.setTitle("목표량 변경")
                    .setMessage("하루 목표를 입력하세요!(0 ~ 5000mL)")
                    .setView(edit)
                    .setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            goal = edit.getText().toString();

                            textGoal.setText(goal);

                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int interval = 50;

        float temp = (progress / interval) * interval;

        textGoal.setText(String.valueOf((int)temp));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
