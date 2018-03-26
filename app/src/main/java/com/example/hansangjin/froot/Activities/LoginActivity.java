package com.example.hansangjin.froot.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.PropertyManager;
import com.example.hansangjin.froot.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private InputMethodManager imm;

    private EditText editText_id, editText_password;
    private ImageView image_exit, image_google, image_facebook, image_kakao, iamge_login;
    private Button button_login;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;


    private CallbackManager facebookCallbackManager;
    private LoginManager facebookLoginManager;

    private KakaoSessionCallback kakaoCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();



    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onResume() {

        init();
        setUpUI();
        setUpListener();

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(kakaoCallback);
    }

    private void init() {
        setUpGoogleLogin();
        setUpFacebookLogin();
        setUpKakaoLogin();

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        editText_id = findViewById(R.id.editText_id);
        editText_password = findViewById(R.id.editText_password);
        image_google = findViewById(R.id.google_login_button);
        image_facebook = findViewById(R.id.facebook_login_button);
        image_kakao = findViewById(R.id.kakao_login_button);
        image_exit = findViewById(R.id.toolbar_button_second);
        button_login = findViewById(R.id.button_login);
    }

    private void setUpListener() {
        findViewById(R.id.google_login_button).setOnClickListener(this);
        findViewById(R.id.facebook_login_button).setOnClickListener(this);
        findViewById(R.id.kakao_login_button).setOnClickListener(this);
        findViewById(R.id.toolbar_button_second).setOnClickListener(this);
        findViewById(R.id.button_login).setOnClickListener(this);
    }

    private void setUpUI(){


//        image_exit.setImageBitmap(setUpImage(R.drawable.button_exit_2));
        image_facebook.setImageBitmap(setUpImage(R.drawable.button_facebook_login_9));
        image_kakao.setImageBitmap(setUpImage(R.drawable.button_kakao_login_11));
        image_google.setImageBitmap(setUpImage(R.drawable.button_google_login_10));
    }

    //이미지 크기 변환
    private Bitmap setUpImage(int resID){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        double ratio = (double) (metrics.density / 4);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resID);


        int width = (int) (bitmap.getWidth() * ratio);
        int height = (int) (bitmap.getHeight() * ratio);

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       //구글
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

                goToNextActivity();

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }

        //페이스북
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);

        //카카오
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View v) {
        int resID = v.getId();

        switch (resID) {
            case R.id.google_login_button:
                googleLoginClicked();
                break;
            case R.id.facebook_login_button:
                facebookLoginClicked();
                break;
            case R.id.kakao_login_button:
//                findViewById(R.id.fake_kakao_login_button).performClick();
                kakaoLoginClicked();
                break;
            case R.id.toolbar_button_second:
                finish();
                break;
            case R.id.button_login:
                loginButtonClcked();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        imm.hideSoftInputFromWindow(editText_id.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editText_password.getWindowToken(), 0);

        return super.onTouchEvent(event);
    }

    //구글 로그인 관련
    private void setUpGoogleLogin(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void googleLoginClicked(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    //구글 로그인 관련

    //페이스북 로그인 관련 시작
    private void setUpFacebookLogin() {
        facebookCallbackManager = CallbackManager.Factory.create();
        facebookLoginManager = LoginManager.getInstance();
    }

    private void facebookLoginClicked() {
        facebookLoginManager.setDefaultAudience(DefaultAudience.FRIENDS);
        facebookLoginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);

        List<String> permissionNeeds = Arrays.asList("email", "public_profile", "user_birthday");

        facebookLoginManager.logInWithReadPermissions(LoginActivity.this, permissionNeeds);

        facebookLoginManager.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess");

                ApplicationController.facebookAccessToken = loginResult.getAccessToken();

                setResult(RESULT_OK);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    Log.d("user profile", object.toString());
                                    Log.d("user profile", object.getString("name"));
                                    Log.d("user profile", object.getString("user_birthday"));
                                    Log.d("user profile", object.getString("locale"));


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();



                PropertyManager.getInstance(getApplicationContext()).setFacebookToken(loginResult.getAccessToken().toString());


                startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
                finish();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }


        });
    }

    //페이스북 로그인 관련 끝

    //카카오 로그인 관련
    private void setUpKakaoLogin(){
        kakaoCallback = new KakaoSessionCallback();
        Session.getCurrentSession().addCallback(kakaoCallback);

    }

    private void kakaoLoginClicked(){
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
    }
    //카카오 로그인 관련

    //이메일 로그인 관련
    private void loginButtonClcked(){
        String message = "";

        button_login.setPressed(true);

        if(editText_id.getText().toString().isEmpty()){
            message = message + "E-mail";
            if(editText_password.getText().toString().isEmpty()){
                message = message + "과 비밀번호";
            }
            message += "를 입력해주세요";

            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
        else if(editText_password.getText().toString().isEmpty()){
            message = message + "비밀번호를 입력해주세요";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }

        else{
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToNextActivity();

                }
            },1000);
        }
//        button_login.setPressed(false);
    }
    //이메일 로그인 관련

    private void buttonAnimation(View v){
        int startColor = 0xffffffff;
        int endColor = getResources().getColor(R.color.logoColor);

//        ValueAnimator colorAnim = ObjectAnimator.of(v, "background", startColor, endColor);

    }

    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, KakaoSignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void goToNextActivity(){
        Intent intent = new Intent(LoginActivity.this, CategoryActivity.class);
        startActivity(intent);
        finish();
    }

    //카톡 세션 클래스
    class KakaoSessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {

            String kakaoAccessToken = Session.getCurrentSession().getAccessToken();


//            Log.d(kakaoAccessToken);

            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Log.e("Error", exception.getMessage());
            }

//            setContentView(R.layout.activity_login);
        }
    }

}
