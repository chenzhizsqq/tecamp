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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static com.example.tecamp.config.Config.SharedPreferences_Frag05;

//予約一覧List
public class Frag05 extends Fragment implements
        Frag01DialogFragment.OnInputSelected
        , TextWatcher {

    ViewPager viewPager;

    public Frag05(ViewPager _viewPager) {
        viewPager = _viewPager;

    }

    private static final String TAG = "Frag05";


    //Show rows 宿泊初日	泊数	代表者	人数	サイト	乗物	備考
    String[] dataArray = {
            "firstymd"
            , "days"
            , "username||' '||username2"
            , "CASE WHEN count_child >0 THEN (count_adult + count_child) ||'('||count_child||')' ELSE (count_adult + count_child) END"
            , "site_count"
            , "way"
            , "memo"
            , "ordernum"
    };
    ArrayList<HashMap<String, String>> mSqlGetArrayMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        updateView(view);

        return view;
    }

    private void updateView(View view) {
        try {

            //データ処理
            String selectData = Arrays.toString(dataArray);
            selectData = selectData.substring(1, selectData.length() - 1);
            //Log.e(TAG, "onClick: selectData:" + selectData);

            String sql = "select " + selectData + " from etcamp_order where canceltime=='' ";
            //sql += " group by ordernum order by date desc";
            sql += " order by firstymd desc";
            Log.e(TAG, "updateView: sql:" + sql);


            mSqlGetArrayMap = DataCenter.pData.SqlGetArrayMap(sql);

            //TableLayout処理
            TableLayout mTableLayout = view.findViewById(R.id.frag05_予約一覧);

            mTableLayout.removeViewsInLayout(1, mTableLayout.getChildCount() - 1);

            //TableLayout データ各行処理
            for (int i = 0; i < mSqlGetArrayMap.size(); i++) {
                //Log.e(TAG, "onClick: mSqlGetArrayMap:"+i+":"+mSqlGetArrayMap.get(i) );

                TableRow mTableRow = new TableRow(getActivity());

                HashMap<String, String> map = mSqlGetArrayMap.get(i);
                //Log.e(TAG, "onClick: map:" + map.toString());

                for (int j = 0; j < dataArray.length; j++) {
                    String data = map.get(dataArray[j]);
                    String ordernum = map.get("ordernum");

                    switch (dataArray[j]) {
                        case "ordernum":
                            break;
                        case "firstymd":
                            String srcDate=Tools.dataChange(data,"/");
                            mTableRow.addView(makeTextView(srcDate, 15), j);
                            break;
                        case "username||' '||username2":
                            TextView textView = makeTextView(data, 15);
                            if (data.trim().length() > 0) {
                                //Log.e(TAG, "updateView: data:"+data );

                                textView.setTextColor(Color.RED);
                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Frag01DialogFragment frag01DialogFragment = new Frag01DialogFragment(ordernum, map.get("firstymd"));
                                        frag01DialogFragment.setTargetFragment(Frag05.this, 1);
                                        assert getFragmentManager() != null;
                                        frag01DialogFragment.show(getFragmentManager(), "frag01DialogFragment");

                                    }

                                });

                            }
                            mTableRow.addView(textView, j);
                            break;
                        case "site_count":
                            String tOrdernum = map.get("ordernum");

                            ArrayList<String> nListGetSiteRoomsName = DataCenter.pData.getSiteRoomsName(tOrdernum);
                            String s=nListGetSiteRoomsName.toString();
                            String s2=s.substring(1,s.length()-1);

                            TextView textView2 = makeTextView(s2, 15);
                            textView2.setTextColor(Color.RED);
                            if (!s2.isEmpty()) {

                                textView2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Frag01SiteFragment frag01SiteFragment = new Frag01SiteFragment(ordernum, map.get("firstymd"));
                                        frag01SiteFragment.setTargetFragment(Frag05.this, 1);
                                        assert getFragmentManager() != null;
                                        frag01SiteFragment.show(getFragmentManager(), "frag01SiteFragment");

                                    }

                                });
                            }
                            mTableRow.addView(textView2, j);
                            break;
                        default:
                            mTableRow.addView(makeTextView(data, 15), j);
                            break;

                    }
                }

                mTableRow.setBackgroundColor(0xFF4CD7C9);
                mTableLayout.addView(mTableRow, mTableLayout.getChildCount());
            }

        } catch (Exception e) {
            Log.e(TAG, "onCreateView: ", e);
        } finally {
        }
    }

    @SuppressLint("SetTextI18n")
    @NotNull
    private TextView makeTextView(String text) {
        TextView textView = new TextView(getActivity());
        textView.setText(text);
        textView.setTextSize(15);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        //textView.setTextSize(20);
        textView.setPadding(5, 10, 5, 10);
        //Log.e(TAG, "makeTextView: text:"+text );
        return textView;
    }

    @SuppressLint("SetTextI18n")
    @NotNull
    private TextView makeTextView(String text, int maxLength) {
        String mText = text.substring(0, Math.min(text.length(), maxLength));
        if (text.length() > maxLength) {
            mText += "...";
        }
        //Log.e(TAG, "makeTextView: mText:"+mText );
        return makeTextView(mText);
    }

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
     */
    public static boolean bAfterTextChanged = false;

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (bAfterTextChanged) {
            bAfterTextChanged = false;
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