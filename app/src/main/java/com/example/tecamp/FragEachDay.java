package com.example.tecamp;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.tecamp.sql.DataCenter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

//予約一覧List
public class FragEachDay extends Fragment implements
        Frag01DialogFragment.OnInputSelected
        , TextWatcher {

    ViewPager viewPager;

    public FragEachDay(ViewPager _viewPager) {
        viewPager = _viewPager;

    }

    private static final String TAG = "FragEachDay";


    //Show rows 宿泊初日	泊数	代表者	人数	サイト	乗物	備考
    String[] dataArray = {
            "min(firstymd)"
            , "days"
            , "username||' '||username2"
            , "CASE WHEN count_child >0 THEN (count_adult + count_child) ||'('||count_child||')' ELSE (count_adult + count_child) END"
            , "count(siteid)"
            , "way"
            , "memo"
            , "ordernum"
    };
    ArrayList<HashMap<String, String>> mSqlGetArrayMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static DateManager pDateManager = new DateManager();
    private TextView mTextViewBookingDate;


    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_each_day_layout, container, false);
        ButterKnife.bind(this, view);

        //「検索日　選択」セット begin
        String mStringBookingDate = "";
        mTextViewBookingDate = view.findViewById(R.id.textView_FragEachDay_booking_date);
        if (mStringBookingDate.isEmpty()) {
            String str = pDateManager.getYMD("-");
            mStringBookingDate = str;
            mTextViewBookingDate.setText(str);

        }
        mTextViewBookingDate.setText(mStringBookingDate);
        mTextViewBookingDate.addTextChangedListener(this);

        TableLayout mTableLayoutBookingList;
        TableLayout mTableLayoutFrag01_TableLayout_All;
        TableLayout mTableLayoutPageSelect;

        mTableLayoutBookingList = (TableLayout) view.findViewById(R.id.fragEachDay_予約一覧);
        mTableLayoutFrag01_TableLayout_All = (TableLayout) view.findViewById(R.id.FragEachDay_TableLayout_All);
        mTableLayoutPageSelect = (TableLayout) view.findViewById(R.id.FragEachDay_page_select);



        //「翌日」ボタン
        Button buttonNext = (Button) mTableLayoutPageSelect.findViewById(R.id.FragEachDayButtonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FragEachDayDataUpdate(1, view);
                } catch (JSONException e) {
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });

        //「前日」ボタン
        Button buttonPre = (Button) mTableLayoutPageSelect.findViewById(R.id.FragEachDayButtonPrev);
        buttonPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FragEachDayDataUpdate(-1,view);
                } catch (JSONException e) {
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });


        Button mOpenDialog_booking_date = view.findViewById(R.id.FragEachDay_date_search_date);
        mOpenDialog_booking_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Frag01SelectFragment frag01SelectFragment = new Frag01SelectFragment(mTextViewBookingDate);
                frag01SelectFragment.setTargetFragment(FragEachDay.this, 1);
                assert getFragmentManager() != null;
                frag01SelectFragment.show(getFragmentManager(), "frag01SelectFragment");

            }
        });

        updateView(view);

        return view;
    }


    @SuppressLint("SetTextI18n")
    public void FragEachDayDataUpdate(int _addDay,View view) throws JSONException {
        try {
            //データのday更新
            pDateManager.addDay(_addDay);

            //view update
            String str = pDateManager.getYMD("-");
            TextView mTextViewBookingDate = view.findViewById(R.id.textView_FragEachDay_booking_date);
            mTextViewBookingDate.setText(str);

            //テスト


            //表示画面更新
            updateView(view);

        } catch (Exception e) {
            Log.e(TAG, "currentPageUpdate: ", e);

        }
    }

    private void updateView(View view) {
        try {

            //データ処理
            String selectData = Arrays.toString(dataArray);
            selectData = selectData.substring(1, selectData.length() - 1);
            //Log.e(TAG, "onClick: selectData:" + selectData);

            String sql = "select " + selectData + " from etcamp_order where canceltime=='' ";
            sql += " and date == "+pDateManager.getYMD();
            sql += " group by ordernum order by date desc";
            Log.e(TAG, "updateView: sql:" + sql);


            mSqlGetArrayMap = DataCenter.pData.SqlGetArrayMap(sql);

            //TableLayout処理
            TableLayout mTableLayout = view.findViewById(R.id.fragEachDay_予約一覧);

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
                        case "min(firstymd)":
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
                                        Frag01DialogFragment frag01DialogFragment = new Frag01DialogFragment(ordernum, map.get("min(firstymd)"));
                                        frag01DialogFragment.setTargetFragment(FragEachDay.this, 1);
                                        assert getFragmentManager() != null;
                                        frag01DialogFragment.show(getFragmentManager(), "frag01DialogFragment");

                                    }

                                });

                            }
                            mTableRow.addView(textView, j);
                            break;
                        case "count(siteid)":
                            String result = "";
                            String tOrdernum = map.get("ordernum");
                            //Log.e(TAG, "updateView: tOrdernum:"+tOrdernum );
                            ArrayList<Integer> nListGetSiteRooms = DataCenter.pData.getSiteRoomsID(tOrdernum);
                            //Log.e(TAG, "updateView: nListGetSiteRooms:"+nListGetSiteRooms );

                            StringBuilder allRoomName = new StringBuilder();
                            boolean bFind = false;
                            for (int id : nListGetSiteRooms) {
                                for (Map.Entry<Integer, String> entry : Rooms.mapRoomNames.entrySet()) {

                                    int nGetKey = entry.getKey();
                                    if (id == nGetKey) {
                                        bFind = true;

                                        allRoomName.append(Rooms.mapRoomNames.get(id)).append(" ");

                                    }
                                }
                            }
                            if (bFind) {
                                result = allRoomName.toString();
                            }
                            TextView textView2 = makeTextView(result, 15);
                            textView2.setTextColor(Color.RED);
                            if (bFind) {

                                textView2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Frag01SiteFragment frag01SiteFragment = new Frag01SiteFragment(ordernum, map.get("min(firstymd)"));
                                        frag01SiteFragment.setTargetFragment(FragEachDay.this, 1);
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
     * FragEachDayとFragEachDaySelectFragment関連する変数。
     */
    public static boolean bAfterTextChanged = false;

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.e(TAG, "onTextChanged: s:"+s );
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
        Log.d(TAG, "FragEachDay from FragEachDayDialogFragment get input: " + input);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

//特定日期 按钮 end