package com.example.tecamp;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.tecamp.config.Config;
import com.example.tecamp.sql.DataCenter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static com.example.tecamp.config.Config.SharedPreferences_Frag05;

//予約一覧
public class Frag05 extends Fragment implements
        Frag01DialogFragment.OnInputSelected
        , TextWatcher {

    ViewPager viewPager;

    public Frag05(ViewPager _viewPager){
        viewPager=_viewPager;

    }

    //簡単記録で使うデータ名前  String group_by = "ordernum";
    public static final String[] pSimpleDataSqlNames = {
            "date",
            "days",
            "username||username2",
            "(count_adult + count_child) as count",
            "count(siteid) as countsiteid",
            "way",
            "memo",
            //Frag05の mShowColumns

            "canceltime",
            "ordernum",
            "count_child",
    };

    private static final String TAG = "Frag05";
    public static  DateManager pDateManager = new DateManager();
    /*private static CalendarDay selectDate=CalendarDay.from(
            mDateManager.getYear()
            , mDateManager.getMonth()
            , mDateManager.getDay()
            );*/


    private static int mCurrentPage = 1;

    TableLayout mTableLayoutBookingList;
    TableLayout mTableLayoutFrag05_TableLayout_All;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*timeCreate();*/
    }

    private String LogTokenGet(String tokenVal) {

        //SharedPreferences対象
        SharedPreferences sp = Objects.requireNonNull(getActivity()).getSharedPreferences(SharedPreferences_Frag05, MODE_PRIVATE);

        //データLOAD
        return sp.getString("token", "none");
        //if NOT_EXIST return "none"
    }


    //Show rows 宿泊初日	泊数	代表者	人数	サイト	乗物	備考
    private final int mShowColumns = 7;


    //詳細内容を見る関数
    void setTextView2DialogFragment(@NotNull TextView _textView, int _row_id) {

        _textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ordernum = null;
                try {
                    ordernum = DataCenter.pData.getSimpleData(_row_id, "ordernum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Frag01DialogFragment frag01DialogFragment = new Frag01DialogFragment(ordernum, "20201230");
                frag01DialogFragment.setTargetFragment(Frag05.this, 1);
                assert getFragmentManager() != null;
                frag01DialogFragment.show(getFragmentManager(), "frag01DialogFragment");

            }

        });

    }

    //サイト内容を見る関数
    void setTextView2SiteFragment(@NotNull TextView _textView, int _row_id) {

        _textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //バーチャル：予約済みの部屋 begin
                String ordernum = null;
                try {
                    ordernum = DataCenter.pData.getSimpleData(_row_id, "ordernum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //バーチャル：予約済みの部屋 end
                Frag01SiteFragment frag01SiteFragment = new Frag01SiteFragment(ordernum, "20201230");
                frag01SiteFragment.setTargetFragment(Frag05.this, 1);
                assert getFragmentManager() != null;
                frag01SiteFragment.show(getFragmentManager(), "frag01SiteFragment");


            }

        });

    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag05_layout, container, false);
        ButterKnife.bind(this, view);


        mTableLayoutBookingList = (TableLayout) view.findViewById(R.id.frag05_予約一覧);
        mTableLayoutFrag05_TableLayout_All = (TableLayout) view.findViewById(R.id.Frag05_TableLayout_All);


        //簡単記録
        ArrayList<TableRow> arrayListTableRow = new ArrayList<TableRow>();


        //記録更新
        //表示画面更新
        return view;
    }

    TextView CreateStyleTextView(String strText) {
        TextView textView = new TextView(getActivity());
        textView.setText(strText);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }


    /*private Timer timer;
    // 'Handler()' is deprecated as of API 30: Android 11.0 (R)
    private final Handler handler = new Handler(Looper.getMainLooper());


    private void timeCreate() {
        long count, second, minute;
        // タイマーが走っている最中にボタンをタップされたケース
        if (null != timer) {
            timer.cancel();
            timer = null;
        }

        // Timer インスタンスを生成
        timer = new Timer();

        // TimerTask インスタンスを生成
        CountUpTimerTask timerTask = new CountUpTimerTask();

        second = 1000;  //1000では、遅延１秒
        timer.schedule(timerTask, second, Config.minuteFrag05DataUpdate);
    }*/

    /**
     * @param view       the picker associated with the dialog
     * @param year       the selected year
     * @param month      the selected month (0-11 for compatibility with
     * @param dayOfMonth the selected day of the month (1-31, depending on
     */


    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * are about to be replaced by new text with length <code>after</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * have just replaced old text that had length <code>before</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */

    /**
    * Frag05とFrag05SelectFragment関連する変数。
    * */
    public static boolean bAfterTextChanged=false;

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(bAfterTextChanged){
            bAfterTextChanged=false;
        }
    }

    /**
     * This method is called to notify you that, somewhere within
     * <code>s</code>, the text has been changed.
     * It is legitimate to make further changes to <code>s</code> from
     * this callback, but be careful not to get yourself into an infinite
     * loop, because any changes you make will cause this method to be
     * called again recursively.
     * (You are not told where the change took place because other
     * afterTextChanged() methods may already have made other changes
     * and invalidated the offsets.  But if you need to know here,
     * you can use {@link Spannable#setSpan} in {@link #onTextChanged}
     * to mark your place and then look up from here where the span
     * ended up.
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
    }


    //データ更新task
    /*class CountUpTimerTask extends TimerTask {
        @Override
        public void run() {
            // handlerを使って処理をキューイングする
            handler.post(new Runnable() {
                public void run() {
                    Log.d(TAG, "CountUpTimerTask class  毎60秒更新一回");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DataCenter.UpdateData();
                        }
                    }).start();
                }
            });
        }
    }*/

    @Override
    public void sendInput(String input) {
        Log.d(TAG, "Frag05 from Frag05DialogFragment get input: " + input);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

//特定日期 按钮 end