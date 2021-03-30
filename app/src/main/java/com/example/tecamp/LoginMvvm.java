package com.example.tecamp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.tecamp.databinding.ActivityLoginMvvmBinding;
import com.example.tecamp.mvvm.User;
import com.example.tecamp.sql.DataCenter;
import com.example.tecamp.sql.JsonFile;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginMvvm extends AppCompatActivity {
    private static final String TAG = "LoginMvvm";
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login_mvvm);

        DataCenter.setContext(this);
        user=new User();
        ActivityLoginMvvmBinding bing= DataBindingUtil.setContentView(LoginMvvm.this,R.layout.activity_login_mvvm);
        bing.setLifecycleOwner(this);

        bing.setLoginMvvm(this);


        //テスト使う！！！！！！！！
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Login("root@root.root", "root");
                Login("demo1@xieyi.co.jp", "root");
            }
        }).start();
        //テスト使う！！！！！！！！
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    public void click(View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                login();
            }
        },0);
    }


    //登录
    public void login() {
        try {

            Log.e(TAG, "status:" + user.status.get());
            Log.e(TAG, "username:" + user.userName.get());
            Log.e(TAG, "password:" + user.password.get());


            new Thread(new Runnable() {
                @Override
                public void run() {
                    Login(user.userName.get(),user.password.get());
                }
            }).start();
        } catch (Exception e) {
            Log.e(TAG, "login: ", e);
        }
    }

    private void Login(String mail,String pw) {
        try {

            String result = DataCenter.pData.LoginInit(mail,pw);
            if (result.equals("0")) {

                String status = DataCenter.UpdateData();

                if (status.equals("0")) {

                    JsonFile jsonFile=new JsonFile(getApplication());
                    jsonFile.saveFile(DataCenter.pData.getJsonObject().toString());
                    Log.e(TAG, "Login: 初回登録成功" );
                    //Log.e(TAG, "run: jsonFile.readFile():"+jsonFile.readFile() );

                }else{
                    JsonFile jsonFile=new JsonFile(getApplication());
                    try {
                        DataCenter.pData.setJsonObject(new JSONObject(jsonFile.readFile()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //テスト使う！！！！！！！！
                Intent intent = new Intent(LoginMvvm.this, MainActivity.class);
                startActivity(intent);
                finish();
                //テスト使う！！！！！！！！

            }else{
                Log.e(TAG, "jsonサバと連接できません。" );

                JsonFile jsonFile=new JsonFile(getApplication());
                try {
                    DataCenter.pData.setJsonObject(new JSONObject(jsonFile.readFile()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //テスト使う！！！！！！！！
                Intent intent = new Intent(LoginMvvm.this, MainActivity.class);
                startActivity(intent);
                finish();
                //テスト使う！！！！！！！！
            }
        }catch (Exception e){
            Log.e(TAG, "Login: ",e );
        }
    }
}