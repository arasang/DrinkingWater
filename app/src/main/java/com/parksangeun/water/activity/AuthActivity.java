package com.parksangeun.water.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.parksangeun.water.R;
import com.parksangeun.water.common.CommonFunction;
import com.parksangeun.water.common.Metrics;

/**
 * Created by parksangeun on 2017. 9. 8..
 */

public class AuthActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "AuthActivity";

    /** View **/
    private SignInButton buttonGoogle;

    private GoogleSignInOptions gso;
    private GoogleApiClient apiClient;

    /** Variable **/

    //Google Login
    private CommonFunction function = new CommonFunction(this);
    private Context context = AuthActivity.this;

    /** Firebase **/
    private FirebaseAuth fireAuth;
    private FirebaseAuth.AuthStateListener fireListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initSign();

        initView();
    }

    // TODO: 로그인을 위한 초기화
    private void initSign(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Firebase Auth 초기화
        fireAuth = FirebaseAuth.getInstance();
        fireListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }

    // TODO: UI 초기화 및 기능 정의
    private void initView(){
        buttonGoogle = (SignInButton) findViewById(R.id.buttonGoogle);

        buttonGoogle.setSize(SignInButton.SIZE_STANDARD);

        buttonGoogle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonGoogle) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
            startActivityForResult(signInIntent, Metrics.RC_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO: 구글 로그인 API 결과 값 반환
        if (requestCode == Metrics.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    private void handleSignInResult(GoogleSignInResult result){
        Log.d(TAG, "GoogleSignInResult : " + result.isSuccess());

        if (result.isSuccess()) {
            // TODO: 로그인 성공

            GoogleSignInAccount acct = result.getSignInAccount();

            // 파이어베이스에 사용자 등록
            firebaseAuthWithGoogle(acct);
            FirebaseDatabase fireDB = FirebaseDatabase.getInstance();

            DatabaseReference ref = fireDB.getReference("member/user_email");
            ref.setValue(acct.getEmail());

            DatabaseReference ref2 = fireDB.getReference("member/family_name");
            ref2.setValue(acct.getFamilyName());

            DatabaseReference ref3 = fireDB.getReference("member/given_name");
            ref3.setValue(acct.getGivenName());

            function.ChangeActivity(context, MainActivity.class);
            finish();
        } else {
            // TODO: 로그인 실패
            Toast.makeText(context, "회원 정보를 확인해 주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        Log.d(TAG, "firebaseWithGoogle : " + account.getIdToken());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        fireAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "SignWithCredential is Success");
                            FirebaseUser user = fireAuth.getCurrentUser();
                            Log.d(TAG, "Authentication user is : " + user.getDisplayName());
                        } else {
                            Log.d(TAG, "SignWithCredential is Failed");
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fireAuth.addAuthStateListener(fireListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        fireAuth.removeAuthStateListener(fireListener);
    }
}
