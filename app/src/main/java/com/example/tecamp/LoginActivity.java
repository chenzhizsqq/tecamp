package com.example.tecamp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.tecamp.sql.DataCenter;
import com.example.tecamp.sql.JsonFile;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

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
        DataCenter.setContext(this);
        setContentView(R.layout.activity_login);

        //Rooms.onCreate();

        Button loginButton = (Button) findViewById(R.id.button);
        loginButton.setOnClickListener(this);

        editTextTextEmailAddress = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextTextEmailAddress.setText("root@root.root");
        editTextTextPassword = (EditText) findViewById(R.id.editTextTextPassword);
        editTextTextPassword.setText("root");

        // ログイン画面表示処理

        // 「pref_data」という設定データファイルを読み込み
        SharedPreferences prefData = getSharedPreferences(SharedPreferences_Login, MODE_PRIVATE);
        String account = prefData.getString("account", "");

        // 空チェック
        if (account != null && account.length() > 0) {
            // 保存済の情報をログインID欄に設定
            this.editTextTextEmailAddress.setText(account);
        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                //Login("root@root.root", "root");
                Login("demo1@xieyi.co.jp", "root");
            }
        }).start();

    }

    private void Login(String mail,String pw) {
        String result = DataCenter.pData.LoginInit(mail,pw);
        if (result.equals("0")) {

            String status = DataCenter.UpdateData();

            if (status.equals("0")) {

                JsonFile jsonFile=new JsonFile(getApplication());
                jsonFile.saveFile(DataCenter.pData.getJsonObject().toString());
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
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            //テスト使う！！！！！！！！

        }else{

            JsonFile jsonFile=new JsonFile(getApplication());
            try {
                DataCenter.pData.setJsonObject(new JSONObject(jsonFile.readFile()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //テスト使う！！！！！！！！
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            //テスト使う！！！！！！！！
        }
    }


    @Override
    public void setContentView(View view) {

    }

    private String LoginResult(String result) {
        String r = "";
        /*  0	正常
                    1	ログイン失敗
                    2	アクセス権限がありません
                    3	データ登録エラー
                    4	プログラム処理エラー
                    5	タイムアウトエラー
                    6	パラメータエラー
                    99	例外エラー
                * */
        switch (result) {
            case "0":
                r = "正常";
                break;
            case "1":
                r = "ログイン失敗";
                break;
            case "2":
                r = "アクセス権限がありません";
                break;
            case "3":
                r = "データ登録エラー";
                break;
            case "4":
                r = "プログラム処理エラー";
                break;
            case "5":
                r = "タイムアウトエラー";
                break;
            case "6":
                r = "パラメータエラー";
                break;
            case "99":
                r = "例外エラー";
                break;
            default:
                r = "null";
                break;

        }
        return r;
    }

    //EtCampLogin、json登録
    private void postRequest(View view) {

        String mEditTextTextEmailAddress = editTextTextEmailAddress.getText().toString();
        String mEditTextTextPassword = editTextTextPassword.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = DataCenter.pData.LoginInit(mEditTextTextEmailAddress, mEditTextTextPassword);

                if (result.equals("0")) {

                    // 「pref_data」という設定データファイルを読み込み
                    SharedPreferences prefData = getSharedPreferences(SharedPreferences_Login, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefData.edit();

                    // 入力されたログインIDとログインパスワード
                    editor.putString("account", editTextTextEmailAddress.getText().toString());

                    // 保存
                    editor.apply();


                    //DataCenter.UpdateData();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (result.isEmpty()) {

                    Snackbar.make(view, "連接ありません。", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                } else {

                    Snackbar.make(view, "result:" + LoginResult(result), Snackbar.LENGTH_LONG)
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