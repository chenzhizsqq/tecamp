package com.example.tecamp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.tecamp.sql.DataCenter;
import com.google.android.material.snackbar.Snackbar;

import okhttp3.OkHttpClient;

import static com.example.tecamp.config.Config.SharedPreferences_Login;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextTextEmailAddress;
    private EditText editTextTextPassword;
    final OkHttpClient client = new OkHttpClient();
    String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Rooms.onCreate();

        Button loginButton = (Button) findViewById(R.id.button);
        loginButton.setOnClickListener(this);

        editTextTextEmailAddress = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = (EditText) findViewById(R.id.editTextTextPassword);

        // ログイン画面表示処理

        // 「pref_data」という設定データファイルを読み込み
        SharedPreferences prefData = getSharedPreferences(SharedPreferences_Login, MODE_PRIVATE);
        String account = prefData.getString("account", "");

        // 空チェック
        if (account != null && account.length() > 0) {
            // 保存済の情報をログインID欄に設定
            this.editTextTextEmailAddress.setText(account);
        }


    }


    @Override
    public void setContentView(View view){

        new Thread(new Runnable() {
            @Override
            public void run() {
                DataCenter.UpdateData();
            }
        }).start();
    }

    //EtCampLogin、json登録
    private void postRequest(View view) {

        String mEditTextTextEmailAddress = editTextTextEmailAddress.getText().toString();
        String mEditTextTextPassword = editTextTextPassword.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (DataCenter.pData.LoginInit(mEditTextTextEmailAddress, mEditTextTextPassword)) {

                    // 「pref_data」という設定データファイルを読み込み
                    SharedPreferences prefData = getSharedPreferences(SharedPreferences_Login, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefData.edit();

                    // 入力されたログインIDとログインパスワード
                    editor.putString("account", editTextTextEmailAddress.getText().toString());

                    // 保存
                    editor.apply();


                    DataCenter.UpdateData();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    Snackbar.make(view, "「アカウント」や「パスウード」が間違います。", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                }

            }
        }).start();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            Snackbar.make(view, "登録中....", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            //Toast.makeText(this, "登録中....", Toast.LENGTH_LONG).show();
            postRequest(view);
        }
    }
}