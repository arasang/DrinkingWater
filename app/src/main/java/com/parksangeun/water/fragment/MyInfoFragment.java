package com.parksangeun.water.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parksangeun.water.R;
import com.parksangeun.water.common.data.Metrics;
import com.parksangeun.water.common.data.UserData;

/**
 * Created by parksangeun on 2017. 9. 21..
 */

public class MyInfoFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "MyInfoFragment";

    private ImageView imageProfile;
    private TextView textName;
    private TextView textEmail;
    private TextView textGoal;
    private LinearLayout buttonName;
    private LinearLayout buttonGoal;

    private String userName = "";
    private String userEmail = "";
    private String dailyGoal = "";

    private setChangeFragment changeListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userName = UserData.getDisplayName();
        userEmail = UserData.getUserEmail();
        dailyGoal = UserData.getDailyGoal();

        changeListener = (setChangeFragment) context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myinfo, container, false);
        imageProfile = (ImageView) view.findViewById(R.id.imageProfile);
        textName = (TextView) view.findViewById(R.id.textName);
        textEmail = (TextView) view.findViewById(R.id.textEmail);
        textGoal = (TextView) view.findViewById(R.id.textGoal);
        buttonName = (LinearLayout) view.findViewById(R.id.buttonName);
        buttonGoal = (LinearLayout) view.findViewById(R.id.buttonGoal);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buttonName.setOnClickListener(this);
        buttonGoal.setOnClickListener(this);

        textName.setText(userName);
        textEmail.setText(userEmail);
        textGoal.setText(dailyGoal);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonName) {
            changeListener.OnChangeFragment(Metrics.CLICK_NAME);
        }

        if (v == buttonGoal) {
            changeListener.OnChangeFragment(Metrics.CLICK_GOAL);

        }
    }

    public interface setChangeFragment {
        public void OnChangeFragment(int position);
    }
}
