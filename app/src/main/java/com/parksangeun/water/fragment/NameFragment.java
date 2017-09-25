package com.parksangeun.water.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.parksangeun.water.R;
import com.parksangeun.water.common.data.Metrics;
import com.parksangeun.water.common.data.UserData;
import com.parksangeun.water.common.firebase.FireAuth;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by parksangeun on 2017. 9. 21..
 */

public class NameFragment extends Fragment {

    private static final String TAG = "NameFragment";

    private EditText editName;
    private ImageButton buttonDelete;

    private String name = "";
    private int textLength = 0;

    private Context context;
    private FireAuth auth;

    /** View **/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_name, container, false);

        editName = (EditText)view.findViewById(R.id.editName);
        buttonDelete = (ImageButton)view.findViewById(R.id.buttonDelete);

        setHasOptionsMenu(true); // 메뉴 구성

        return view;
    }

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
                String temp = editName.getText().toString();

                if (temp.length() != 0) {
                    name = temp;
                    auth.changeProfile(name, null);
                    getFragmentManager().popBackStack();

                    UserData.setDisplayName(name);
                }

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        name = UserData.getDisplayName();

        editName.setText(name);

        if (name.length() != 0)
            buttonDelete.setVisibility(GONE);
        else
            buttonDelete.setVisibility(VISIBLE);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Delete text in edittext
                editName.setText("");
                buttonDelete.setVisibility(GONE);
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
        auth = new FireAuth(context);
    }
}
