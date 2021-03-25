package com.example.tecamp;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.ButterKnife;

/**
* 予約一覧 ALL
* */
public class FragSelect extends Fragment implements TextWatcher {

    ViewPager viewPager;

    public FragSelect(ViewPager _viewPager) {
        viewPager = _viewPager;

    }

    private static final String TAG = "FragSelect";
    private int mDataOffSet;

    private final DateManager pDateManager = new DateManager();

    DateManager pDateManager1 = new DateManager();
    DateManager pDateManager2 = new DateManager();

    //Show rows 宿泊初日	泊数	代表者	人数	サイト	乗物	備考
    String[] dataArray = {
            "firstymd"
            , "days"
            , "username||' '||username2"
            , "CASE WHEN count_child >0 THEN (count_adult + count_child) ||'('||count_child||')' ELSE (count_adult + count_child) END"
            , "site_count"
            , "way"
            , "memo"
            , "canceltime"
            , "ordernum"
    };
    ArrayList<HashMap<String, String>> mSqlGetArrayMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private String mSelectName = "";
    private int nSelectView = 0;


    //TextView  fragSelect_textView_stay_date
    String mStayDate = "";
    String mSrcStayDate = "";

    //TextView  fragSelect_textView_booking_date

    String mBookingDate = "";
    String mSrcBookingDate = "";


    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_select_layout, container, false);
        ButterKnife.bind(this, view);

        mDataOffSet = 0;

        TableLayout mTableLayout = view.findViewById(R.id.frag_select_予約一覧);

        mTableLayout.removeViewsInLayout(1, mTableLayout.getChildCount() - 1);


        //TextView  fragSelect_textView_stay_date
        //TextView textViewStayDate = view.findViewById(R.id.fragSelect_textView_stay_date);

        //TextView  fragSelect_textView_booking_date
        //TextView textViewBookingDate = view.findViewById(R.id.fragSelect_textView_booking_date);

        //EditText  fragSelectEditTextName
        EditText SelectNameEditText = (EditText) view.findViewById(R.id.fragSelectEditTextName);


        //Button    fragSelect_button_search

        Button buttonSearch = (Button) view.findViewById(R.id.fragSelect_button_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "buttonSearch onClick: begin");


                TableLayout mTableLayout = view.findViewById(R.id.frag_select_予約一覧);
                mTableLayout.removeViewsInLayout(1, mTableLayout.getChildCount() - 1);

                /*mSelectName = SelectNameEditText.getText().toString();

                TextView t1 = view.findViewById(R.id.frag_select_result_名前);
                t1.setText(mSelectName);

                TextView t2 = view.findViewById(R.id.frag_select_result_宿泊日);
                t2.setText(mSrcStayDate);

                TextView t3 = view.findViewById(R.id.frag_select_result_予約日);
                t3.setText(mSrcBookingDate);*/


                //textViewStayDate.setText("選択");

                //textViewBookingDate.setText("選択");


                /*SelectNameEditText.requestFocus();
                InputMethodManager manager =
                        (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);*/


                updateView(view, mSelectName);
            }
        });


        //Button    fragSelect_name_enter
        Button mSelectNameEnter = view.findViewById(R.id.fragSelect_name_enter);
        mSelectNameEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectName = SelectNameEditText.getText().toString();

                TextView t1 = view.findViewById(R.id.frag_select_result_名前);
                t1.setText(mSelectName);
            }

        });

        //Button    fragSelect_clear
        Button mFragSelectClear = view.findViewById(R.id.fragSelect_clear);
        mFragSelectClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSelectName = "";
                mStayDate = "";
                mSrcStayDate = "";
                mBookingDate = "";
                mSrcBookingDate = "";

                SelectNameEditText.setText("");

                TextView t1 = view.findViewById(R.id.frag_select_result_名前);
                t1.setText(mSelectName);

                TextView t2 = view.findViewById(R.id.frag_select_result_宿泊日);
                t2.setText(mSrcStayDate);

                TextView t3 = view.findViewById(R.id.frag_select_result_予約日);
                t3.setText(mSrcBookingDate);
            }
        });

        //Button    fragSelect_pick_date_stay_date
        Button mOpenDialog_stay_date = view.findViewById(R.id.fragSelect_pick_date_stay_date);
        mOpenDialog_stay_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //textViewStayDate.setText("選択");

                DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        Log.d(TAG, "onDateSet: selectedYear:" + selectedYear
                                + ",  selectedMonth:" + selectedMonth
                                + ",  selectedDay:" + selectedDay);

                        pDateManager1.setDate(selectedYear, selectedMonth + 1, selectedDay);
                        //textViewStayDate.setText(pDateManager1.getYMD("/"));
                        mSrcStayDate = pDateManager1.getYMD("/");
                        mStayDate = pDateManager1.getYMD();

                        SelectNameEditText.requestFocus();
                        InputMethodManager manager =
                                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);


                        TextView t2 = view.findViewById(R.id.frag_select_result_宿泊日);
                        t2.setText(mSrcStayDate);
                    }
                }, pDateManager1.getYear(), pDateManager1.getMonth() - 1, pDateManager1.getDay());
                mDatePicker.show();
            }
        });


        //Button    fragSelect_pick_date_booking_date
        Button mOpenDialog_booking_date = view.findViewById(R.id.fragSelect_pick_date_booking_date);
        mOpenDialog_booking_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //textViewBookingDate.setText("選択");

                DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        Log.d(TAG, "onDateSet: selectedYear:" + selectedYear
                                + ",  selectedMonth:" + selectedMonth
                                + ",  selectedDay:" + selectedDay);

                        pDateManager2.setDate(selectedYear, selectedMonth + 1, selectedDay);
                        //textViewBookingDate.setText(pDateManager2.getYMD("/"));
                        mSrcBookingDate = pDateManager2.getYMD("/");
                        mBookingDate = pDateManager2.getYMD();


                        SelectNameEditText.requestFocus();
                        InputMethodManager manager =
                                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        TextView t3 = view.findViewById(R.id.frag_select_result_予約日);
                        t3.setText(mSrcBookingDate);
                    }
                }, pDateManager2.getYear(), pDateManager2.getMonth() - 1, pDateManager2.getDay());
                mDatePicker.show();

            }
        });

        //TableRow_result
        TableRow tableRow_result = view.findViewById(R.id.TableRow_result);
        /*tableRow_result.setVisibility(View.INVISIBLE);*/


        return view;
    }


    private void updateView(View view, String name) {
        try {
            Log.d(TAG, "updateView: name:" + name);
            String sql = getSql(name);

            //限定数量sql追加
            String sqlLimit = sql + " limit " + Config.maxSrcCount + " OFFSET " + mDataOffSet;
            Log.d(TAG, "updateView: sql:" + sqlLimit);


            mSqlGetArrayMap = DataCenter.pData.SqlGetArrayMap(sqlLimit);

            //TableLayout処理
            TableLayout mTableLayout = view.findViewById(R.id.frag_select_予約一覧);

            //mTableLayout.removeViewsInLayout(1, mTableLayout.getChildCount() - 1);

            //TableLayout データ各行処理
            for (int i = 0; i < mSqlGetArrayMap.size(); i++) {
                //Log.d(TAG, "onClick: mSqlGetArrayMap:"+i+":"+mSqlGetArrayMap.get(i) );

                TableRow mTableRow = new TableRow(getActivity());

                HashMap<String, String> map = mSqlGetArrayMap.get(i);
                //Log.d(TAG, "onClick: map:" + map.toString());

                for (int j = 0; j < dataArray.length; j++) {
                    String data = map.get(dataArray[j]);
                    String ordernum = map.get("ordernum");

                    switch (dataArray[j]) {
                        case "ordernum":
                            break;
                        case "firstymd":
                            String srcDate = Tools.dataChange(data, "/");
                            mTableRow.addView(makeTextView(srcDate /*+":"+mTableLayout.getChildCount()*/
                                    , 15), j);
                            break;
                        case "username||' '||username2":
                            TextView textView = makeTextView(data, 15);
                            if (data.trim().length() > 0) {

                                textView.setTextColor(Color.RED);
                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        OrderUserFragment orderUserFragment = new OrderUserFragment(ordernum, map.get("firstymd"));
                                        orderUserFragment.setTargetFragment(FragSelect.this, 1);
                                        assert getFragmentManager() != null;
                                        orderUserFragment.show(getFragmentManager(), "orderUserFragment");

                                    }

                                });

                            }
                            mTableRow.addView(textView, j);
                            break;
                        case "site_count":
                            if (!map.get("canceltime").trim().equals("")) {
                                mTableRow.addView(makeTextView("キャンセル："), j);
                            } else {
                                String tOrdernum = map.get("ordernum");

                                ArrayList<String> nListGetSiteRoomsName = DataCenter.pData.getSiteRoomsName(tOrdernum);
                                String s = nListGetSiteRoomsName.toString();
                                String s2 = s.substring(1, s.length() - 1);

                                TextView textView2 = makeTextView(s2, 15);
                                textView2.setTextColor(Color.RED);
                                if (!s2.isEmpty()) {

                                    textView2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            OrderSiteFragment orderSiteFragment = new OrderSiteFragment(ordernum, map.get("firstymd"));
                                            orderSiteFragment.setTargetFragment(FragSelect.this, 1);
                                            assert getFragmentManager() != null;
                                            orderSiteFragment.show(getFragmentManager(), "orderSiteFragment");

                                        }

                                    });
                                }
                                mTableRow.addView(textView2, j);
                            }
                            break;
                        case "canceltime":
                            break;
                        case "memo":
                            if (!map.get("canceltime").trim().equals("")) {
                                mTableRow.addView(makeTextView(map.get("canceltime"), 15), j);
                            } else {
                                mTableRow.addView(makeTextView(data, 15), j);
                            }
                            break;
                        default:
                            mTableRow.addView(makeTextView(data, 15), j);
                            break;

                    }
                }

                if (!map.get("canceltime").trim().equals("")) {

                    mTableRow.setBackgroundColor(Color.GRAY);
                } else {

                    mTableRow.setBackgroundColor(0xFF4CD7C9);
                }
                mTableLayout.addView(mTableRow, mTableLayout.getChildCount());
            }

            //最大の記録表示
            viewMoreSrc(view, mSelectName);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: ", e);
        } finally {
        }
    }

    @NotNull
    private String getSql(String name) {
        //データ処理
        String selectData = Arrays.toString(dataArray);
        selectData = selectData.substring(1, selectData.length() - 1);
        Log.d(TAG, "onClick: selectData:" + selectData);

        String sql = "select " + selectData + " from etcamp_order where username||' '||username2 like '%" + name + "%' ";
        if (!mStayDate.equals("")) {
            sql += " and firstymd like '" + mStayDate + "%' ";
        }
        if (!mBookingDate.equals("")) {
            sql += " and createtime like '" + mBookingDate + "%' ";
        }
        sql += " order by firstymd desc";
        return sql;
    }

    //最大の表示数表示 表示数:もっと
    private void viewMoreSrc(View view, String name) {
        TableLayout mTableLayout = view.findViewById(R.id.frag_select_予約一覧);

        String sql = getSql(name);
        Log.d(TAG, "viewMoreSrc: sql:" + sql);

        //最大表示数
        int getSqlMaxCount = DataCenter.pData.getSqlCount(sql);

        TableRow mTableRow = new TableRow(getActivity());
        if (getSqlMaxCount > mDataOffSet + Config.maxSrcCount) {
            int nowCount = mDataOffSet + Config.maxSrcCount;
            mTableRow.addView(makeTextView("表示数：" + nowCount));
            mTableRow.addView(makeTextView("/" + getSqlMaxCount + "   ", 15));

            Button updateButton = new Button(getActivity());

            updateButton.setText("もっと");
            updateButton.setTextSize(15);
            updateButton.setTextColor(Color.BLACK);
            updateButton.setGravity(Gravity.CENTER);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTableLayout.removeViewsInLayout(mTableLayout.getChildCount() - 1, 1);
                    mDataOffSet += Config.maxSrcCount;
                    updateView(view, mSelectName);
                    Log.d(TAG, "onClick: 更新");

                }

            });
            mTableRow.addView(updateButton);
        } else {
            mTableRow.addView(makeTextView("表示数：" + getSqlMaxCount));
            mTableRow.addView(makeTextView("/" + getSqlMaxCount + "   "));

            TextView textViewMax = new TextView(getActivity());

            textViewMax.setText("   最大の記録を示しました。");
            textViewMax.setTextSize(15);
            textViewMax.setTextColor(Color.BLACK);
            textViewMax.setGravity(Gravity.CENTER);
            mTableRow.addView(textViewMax);
        }
        mTableRow.setGravity(Gravity.CENTER);
        mTableLayout.addView(mTableRow, mTableLayout.getChildCount());
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
        //Log.d(TAG, "makeTextView: text:"+text );
        return textView;
    }

    @SuppressLint("SetTextI18n")
    @NotNull
    private TextView makeTextView(String text, int maxLength) {
        String mText = text.substring(0, Math.min(text.length(), maxLength));
        if (text.length() > maxLength) {
            mText += "...";
        }
        //Log.d(TAG, "makeTextView: mText:"+mText );
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


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
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
    public void onDestroy() {
        super.onDestroy();
    }

}

//特定日期 按钮 end