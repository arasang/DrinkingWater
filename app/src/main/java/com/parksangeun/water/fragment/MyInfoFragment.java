package com.parksangeun.water.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parksangeun.water.R;
import com.parksangeun.water.common.data.Metrics;
import com.parksangeun.water.common.data.UserData;
import com.parksangeun.water.common.dialog.PhotoDialog;
import com.parksangeun.water.common.task.GetPhotoTask;

import java.net.MalformedURLException;
import java.net.URL;

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
    private RelativeLayout buttonPhoto;
    private RelativeLayout buttonCamera;

    private Uri imagePath = null;
    private String userName = "";
    private String userEmail = "";
    private String dailyGoal = "";

    private setChangeFragment changeListener;
    private startCamera cameraListener;

    private Context context;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Metrics.GET_IMAGE_SUCCESS:
                    byte[] array = msg.getData().getByteArray(Metrics.BYTE_ARRAY_PHOTO);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
                    imageProfile.setImageBitmap(bitmap);

                    imageProfile.setBackground(new ShapeDrawable(new OvalShape()));
                    imageProfile.setClipToOutline(true);
                    break;

                case Metrics.GET_IMAGE_FAILED:

                    break;

                case Metrics.CAMERA_START:
                    cameraListener.OnStartCamera();
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

        imagePath = UserData.getUserPhoto();
        userName = UserData.getDisplayName();
        userEmail = UserData.getUserEmail();
        dailyGoal = UserData.getDailyGoal();

        changeListener = (setChangeFragment) context;
        cameraListener = (startCamera) context;
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
        buttonPhoto = (RelativeLayout) view.findViewById(R.id.buttonPhoto);
        buttonCamera = (RelativeLayout) view.findViewById(R.id.buttonCamera);

        buttonCamera.setElevation(10);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buttonName.setOnClickListener(this);
        buttonGoal.setOnClickListener(this);
        buttonPhoto.setOnClickListener(this);

        try {
            URL url = new URL(imagePath.toString());
            new GetPhotoTask(url.toString(), handler).execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        textName.setText(userName);
        textEmail.setText(userEmail);
        textGoal.setText(dailyGoal);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonName) {
            changeListener.onChangeFragment(Metrics.CLICK_NAME);
        }

        if (v == buttonGoal) {
            changeListener.onChangeFragment(Metrics.CLICK_GOAL);
        }

        if (v == buttonPhoto) {
//            new PhotoDialog(context, handler).show();
            Toast.makeText(context, "서비스 준비중입니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        textName.setText(UserData.getDisplayName());
    }

    public interface setChangeFragment {
        void onChangeFragment(int position);
    }

    public interface startCamera {
        void OnStartCamera();
    }

    public void changeProfile(Uri uri) throws MalformedURLException {
        URL url = new URL(uri.toString());
        String strUrl = url.toString();
        new GetPhotoTask(strUrl, handler).execute();
    }
}
