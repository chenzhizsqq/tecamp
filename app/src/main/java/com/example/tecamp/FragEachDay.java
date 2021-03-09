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

import butterknife.ButterKnife;

//予約一覧 ALL
public class FragEachDay extends Fragment implements
        Frag01DialogFragment.OnInputSelected {

    ViewPager viewPager;

    public FragEachDay(ViewPager _viewPager) {
        viewPager = _viewPager;

    }

    private static final String TAG = "FragEachDay";
    private int mDataOffSet;
    public static boolean bAfterTextChanged=false;


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
    ArrayList<HashMap<String, String>> mDataArrayMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View view=null;
    private String lastSelectTime="";

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        //Log.e(TAG, "onCreateView: begin" );
        view = inflater.inflate(R.layout.frag_each_day_layout, container, false);
        ButterKnife.bind(this, view);

        mDataOffSet = 0;


        updateView(view);

        //TableLayout処理
        TableLayout mTableLayout = view.findViewById(R.id.FragEachDay_予約一覧);

        //「前日」ボタン
        Button buttonPre = (Button) view.findViewById(R.id.FragEachDayButtonPrev);
        buttonPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e(TAG, "buttonPre onClick: begin" );
                pDateManager.addDay(-1);

                TextView textViewDate=view.findViewById(R.id.textView_FragEachDay_booking_date);
                String srcSelectTime = pDateManager.getYMD("/");
                textViewDate.setText(""+srcSelectTime);
            }
        });

        //「翌日」ボタン
        Button buttonNext = (Button) view.findViewById(R.id.FragEachDayButtonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "buttonNext onClick: begin" );
                pDateManager.addDay(1);

                TextView textViewDate=view.findViewById(R.id.textView_FragEachDay_booking_date);
                String srcSelectTime = pDateManager.getYMD("/");
                textViewDate.setText(""+srcSelectTime);
            }
        });

        TextView textViewDate=view.findViewById(R.id.textView_FragEachDay_booking_date);
        String srcSelectTime = pDateManager.getYMD("/");
        textViewDate.setText(""+srcSelectTime);
        textViewDate.addTextChangedListener(new TextWatcher(){

            // 変化する前.
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //Log.e(TAG, "beforeTextChanged: " );

            }

            // 変化した時.
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Log.e(TAG, "onTextChanged: " );

            }

            @Override
            public void afterTextChanged(Editable s) {

                //Log.e(TAG, "afterTextChanged: " );
                updateView(view);


            }

        });


        Button mOpenDialog_booking_date = view.findViewById(R.id.pick_FragEachDay_date_search_date);
        mOpenDialog_booking_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Frag01SelectFragment frag01SelectFragment = new Frag01SelectFragment(textViewDate,pDateManager);
                frag01SelectFragment.setTargetFragment(FragEachDay.this, 1);
                assert getFragmentManager() != null;
                frag01SelectFragment.show(getFragmentManager(), "frag01SelectFragment");

            }
        });
        return view;
    }

    public static  DateManager pDateManager = new DateManager();

    private void updateView(View view) {
        try {

            //TableLayout処理
            TableLayout mTableLayout = view.findViewById(R.id.FragEachDay_予約一覧);
            mTableLayout.removeViewsInLayout(1, mTableLayout.getChildCount() - 1);

            //データ処理
            String selectData = Arrays.toString(dataArray);
            selectData = selectData.substring(1, selectData.length() - 1);
            //Log.e(TAG, "onClick: selectData:" + selectData);

            /*String sql = "select " + selectData + " from etcamp_order where firstymd=='"+pDateManager.getYMD()+"' ";
            sql += " order by firstymd desc";*/
            String sql = "select firstymd, days, username||' '||username2,\n" +
                    " CASE WHEN count_child >0 THEN (count_adult + count_child) ||'('||count_child||')' ELSE (count_adult + count_child) END, \n" +
                    "site_count, way, memo,canceltime, a.ordernum as ordernum\n" +
                    " from etcamp_order as a,etcamp_SiteList as b \n" +
                    " where a.ordernum=b.ordernum and b.ymd=='"+pDateManager.getYMD()+"'   group by b.orderid order by firstymd ";
            Log.e(TAG, "updateView: sql:"+sql );


            mDataArrayMap = DataCenter.pData.SqlGetArrayMap(sql);


            //mTableLayout.removeViewsInLayout(1, mTableLayout.getChildCount() - 1);

            //TableLayout データ各行処理
            for (int i = 0; i < mDataArrayMap.size(); i++) {
                //Log.e(TAG, "onClick: mSqlGetArrayMap:"+i+":"+mSqlGetArrayMap.get(i) );

                TableRow mTableRow = new TableRow(getActivity());

                HashMap<String, String> map = mDataArrayMap.get(i);
                //Log.e(TAG, "onClick: map:" + map.toString());

                for (int j = 0; j < dataArray.length; j++) {
                    String data = map.get(dataArray[j]);
                    String ordernum = map.get("ordernum");
                    //Log.e(TAG, "updateView: dataArray[j]:"+dataArray[j] );
                    //Log.e(TAG, "updateView: nDays:"+nDays );
                    switch (dataArray[j]) {
                        case "ordernum":
                            break;
                        case "canceltime":
                            break;
                        case "days":

                            if(Integer.parseInt(data)>1){
                                int nDays=0;
                                DateManager pDateManagerFirstDay = new DateManager();

                                pDateManagerFirstDay.setDate( map.get("firstymd"));
                                nDays=Tools.differentDays(pDateManagerFirstDay.getDate(),pDateManager.getDate());
                                int t=nDays+1;

                                mTableRow.addView(makeTextView(data+"("+t+")"), j);
                            }else{

                                mTableRow.addView(makeTextView(data), j);
                            }
                            break;
                        case "firstymd":
                            String srcDate = Tools.dataChange(data, "/");
                            mTableRow.addView(makeTextView(srcDate /*+":"+mTableLayout.getChildCount()*/), j);
                            break;
                        case "username||' '||username2":
                            TextView textView = makeTextView(data);
                            if (data.trim().length() > 0) {

                                textView.setTextColor(Color.RED);
                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Frag01DialogFragment frag01DialogFragment = new Frag01DialogFragment(ordernum, map.get("firstymd"));
                                        frag01DialogFragment.setTargetFragment(FragEachDay.this, 1);
                                        assert getFragmentManager() != null;
                                        frag01DialogFragment.show(getFragmentManager(), "frag01DialogFragment");

                                    }

                                });

                            }
                            mTableRow.addView(textView, j);
                            break;
                        case "site_count":
                            if(!map.get("canceltime").trim().equals("")){
                                mTableRow.addView(makeTextView("キャンセル："), j);
                            }else {
                                String orderNum = map.get("ordernum");

                                ArrayList<String> nListGetSiteRoomsName = DataCenter.pData.getSiteRoomsName(orderNum);
                                String s = nListGetSiteRoomsName.toString();
                                String s2 = s.substring(1, s.length() - 1);

                                TextView textView2 = makeTextView(s2, 15);
                                textView2.setTextColor(Color.RED);
                                if (!s2.isEmpty()) {

                                    textView2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Frag01SiteFragment frag01SiteFragment = new Frag01SiteFragment(ordernum, pDateManager.getYMD());
                                            frag01SiteFragment.setTargetFragment(FragEachDay.this, 1);
                                            assert getFragmentManager() != null;
                                            frag01SiteFragment.show(getFragmentManager(), "frag01SiteFragment");

                                        }

                                    });
                                }
                                mTableRow.addView(textView2, j);
                            }
                            break;
                        case "memo":
                            if(!map.get("canceltime").trim().equals("")){
                                mTableRow.addView(makeTextView(map.get("canceltime"), 15), j);
                            }else{
                                mTableRow.addView(makeTextView(data, 15), j);
                            }
                            break;
                        default:
                            mTableRow.addView(makeTextView(data, 15), j);
                            break;

                    }
                }

                if(!map.get("canceltime").trim().equals("")){

                    mTableRow.setBackgroundColor(Color.GRAY);
                }else{

                    mTableRow.setBackgroundColor(0xFF4CD7C9);
                }
                mTableLayout.addView(mTableRow, mTableLayout.getChildCount());
            }

            int n=mDataArrayMap.size();
            TextView textViewAmount=view.findViewById(R.id.FragEachDayAmount);
            textViewAmount.setText("数量："+n);

            String srcSelectTime = pDateManager.getYMD("/");
            if(!lastSelectTime.equals(srcSelectTime)){
                lastSelectTime=srcSelectTime;

                TextView textViewDate=view.findViewById(R.id.textView_FragEachDay_booking_date);
                textViewDate.setText(""+srcSelectTime);
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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //Log.e(TAG, "setUserVisibleHint: begin" );
        if (isVisibleToUser) {

            if(view!=null){

                updateView(view);
            }

        }
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