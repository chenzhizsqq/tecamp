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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tecamp.config.Config;
import com.example.tecamp.sql.DataCenter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static com.example.tecamp.config.Config.SharedPreferences_Frag01;

//予約一覧
public class Frag01 extends Fragment implements
        DatePickerDialog.OnDateSetListener
        , Frag01DialogFragment.OnInputSelected
        , TextWatcher {

    //簡単記録で使うデータ名前  String group_by = "ordernum";
    public static final String[] pSimpleDataSqlNames = {
            "date",
            "days",
            "username||username2",
            "(count_adult + count_child) as count",
            "count(siteid) as countsiteid",
            "way",
            "memo",
            //Frag01の mShowColumns

            "canceltime",
            "ordernum",
            "count_child",
    };

    private static final String TAG = "Frag01";
    public TextView mTextViewBookingDate;
    private static String mStringBookingDate = "";
    public static  DateManager mDateManager = new DateManager();
    /*private static CalendarDay selectDate=CalendarDay.from(
            mDateManager.getYear()
            , mDateManager.getMonth()
            , mDateManager.getDay()
            );*/


    private static int mCurrentPage = 1;
    private final int[] mTableRowIdArray = {
            //1
            R.id.table_row_1, R.id.table_row_2, R.id.table_row_3, R.id.table_row_4, R.id.table_row_5
            , R.id.table_row_6, R.id.table_row_7, R.id.table_row_8, R.id.table_row_9, R.id.table_row_10,

            //2
            R.id.table_row_11, R.id.table_row_12, R.id.table_row_13, R.id.table_row_14, R.id.table_row_15
            , R.id.table_row_16, R.id.table_row_17, R.id.table_row_18, R.id.table_row_19, R.id.table_row_20,

            //3
            R.id.table_row_21, R.id.table_row_22, R.id.table_row_23, R.id.table_row_24, R.id.table_row_25
            , R.id.table_row_26, R.id.table_row_27, R.id.table_row_28, R.id.table_row_29, R.id.table_row_30,

            //4
            R.id.table_row_31, R.id.table_row_32, R.id.table_row_33, R.id.table_row_34, R.id.table_row_35
            , R.id.table_row_36, R.id.table_row_37, R.id.table_row_38, R.id.table_row_39, R.id.table_row_40,

            //5
            R.id.table_row_41, R.id.table_row_42, R.id.table_row_43, R.id.table_row_44, R.id.table_row_45
            , R.id.table_row_46, R.id.table_row_47, R.id.table_row_48, R.id.table_row_49, R.id.table_row_50,

            //6
            R.id.table_row_51, R.id.table_row_52, R.id.table_row_53, R.id.table_row_54, R.id.table_row_55
            , R.id.table_row_56, R.id.table_row_57, R.id.table_row_58, R.id.table_row_59, R.id.table_row_60,

            //7
            R.id.table_row_61, R.id.table_row_62, R.id.table_row_63, R.id.table_row_64, R.id.table_row_65
            , R.id.table_row_66, R.id.table_row_67, R.id.table_row_68, R.id.table_row_69, R.id.table_row_70,

            //8
            R.id.table_row_71, R.id.table_row_72, R.id.table_row_73, R.id.table_row_74, R.id.table_row_75
            , R.id.table_row_76, R.id.table_row_77, R.id.table_row_78, R.id.table_row_79, R.id.table_row_80,

            //9
            R.id.table_row_81, R.id.table_row_82, R.id.table_row_83, R.id.table_row_84, R.id.table_row_85
            , R.id.table_row_86, R.id.table_row_87, R.id.table_row_88, R.id.table_row_89, R.id.table_row_90,

            //10
            R.id.table_row_91, R.id.table_row_92, R.id.table_row_93, R.id.table_row_94, R.id.table_row_95
            , R.id.table_row_96, R.id.table_row_97, R.id.table_row_98, R.id.table_row_99, R.id.table_row_100
    };


    TableLayout mTableLayoutBookingList;
    TableLayout mTableLayoutFrag01_TableLayout_All;
    TableLayout mTableLayoutPageSelect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timeCreate();
    }


    public String getSelectDate() {
        String str;
        String year = "" + mDateManager.getYear();
        String month = mDateManager.getMonth() < 10 ? "0" + mDateManager.getMonth() : "" + mDateManager.getMonth();
        String day = mDateManager.getDay() < 10 ? "0" + mDateManager.getDay() : "" + mDateManager.getDay();
        str = year + month + day;
        Log.d(TAG, "getSelectDate: " + str);
        return str;
    }

    @SuppressLint("SetTextI18n")
    public void Frag01DataUpdate(int _addDay) throws JSONException {
        try {
            //DataCenter.UpdateData();    //buttonNext.setOnClickListener onClick
            //データのday更新
            mDateManager.addDay(_addDay);

            //view update
            String str = String.format(
                    Locale.US,
                    "%d/%d/%d",
                    mDateManager.getYear(),
                    mDateManager.getMonth(),
                    mDateManager.getDay()
            );
            mStringBookingDate = str;
            mTextViewBookingDate.setText(str);

            //Sqlからdata update
            DataCenter.pData.updateSimpleDataArray(getSelectDate());
            //DataCenter.pData.updateDetailDataArray(getSelectDate());

            DataCenter.pData.updateSiteRoomDataArray();

            //テスト


            //表示画面更新
            TableLayoutUpdate();
            FromUpdate();

        } catch (Exception e) {
            Log.e(TAG, "currentPageUpdate: ", e);

        }
    }

    private String LogTokenGet(String tokenVal) {

        //SharedPreferences対象
        SharedPreferences sp = Objects.requireNonNull(getActivity()).getSharedPreferences(SharedPreferences_Frag01, MODE_PRIVATE);

        //データLOAD
        return sp.getString("token", "none");
        //if NOT_EXIST return "none"
    }


    //Show rows 宿泊初日	泊数	代表者	人数	サイト	乗物	備考
    private final int mShowColumns = 7;

    //資料内容更新
    @SuppressLint("SetTextI18n")
    private void TableLayoutUpdate() throws JSONException {
        //table_row資料内容更新

        //内容clear
        for (int i = 0; i < 100; i++) {    //100
            for (int j = 0; j < mShowColumns; j++) {   //7
                int idTag = i * mTableRowIdArray.length + j;    //i*100 + j
                TextView idTextView = (TextView) mTableLayoutBookingList.findViewWithTag(idTag);
                String temp = "";
                //temp = "test i:" + i + " j:" + j; //test
                idTextView.setText(temp);
                idTextView.setMaxHeight(0);
                idTextView.setMinHeight(0);
                idTextView.setEnabled(false);
            }

            //row hidden
            TableRow tableRow = (TableRow) mTableLayoutBookingList.findViewById(mTableRowIdArray[i]);
            tableRow.setVisibility(View.INVISIBLE);
            tableRow.setMinimumHeight(0);

        }

        //内容update
        for (int i = 0; i < DataCenter.pData.GetOrderListArrayList().size(); i++) {
            //Log.d(TAG, "DataUpdate: " + mJsonControl.GetOrderListArrayList().get(i));

            //普通表示
            for (int j = 0; j < mShowColumns; j++) {   //
                int idTag = i * mTableRowIdArray.length + j;    //i*100 + j

                TextView idTextView;
                idTextView = (TextView) mTableLayoutBookingList.findViewWithTag(idTag);

                //詳細内容を見る
                switch (j) {
                    case 2:
                    case 4:
                        idTextView.setTextColor(Color.RED);
                        break;
                    default:
                        idTextView.setTextColor(Color.BLACK);
                        break;
                }

                String temp = "";
                temp = Tools.SrcContent(
                        DataCenter.pData.getSimpleData(
                                i + (mCurrentPage - 1) * mTableRowIdArray.length
                                , pSimpleDataSqlNames[j]
                        )
                        , 15);
                if (!temp.isEmpty()) {
                    idTextView.setText(temp);
                    idTextView.setEnabled(true);
                }
                idTextView.setMinHeight(80);
                idTextView.setMaxHeight(80);

            }


            //特別表示
            for (int j = 0; j < pSimpleDataSqlNames.length; j++) {   //
                int idTag = i * mTableRowIdArray.length + j;    //i*100 + j

                String orderNum = DataCenter.pData.getSimpleData(i, "ordernum");
                TextView idTextView;
                switch (pSimpleDataSqlNames[j]) {
                    case "date":
                        String firstDay = DataCenter.pData.getSiteFirstDay(orderNum);
                        //Log.e(TAG, "TableLayoutUpdate: fistDay:" + firstDay);

                        idTextView = (TextView) mTableLayoutBookingList.findViewWithTag(idTag);

                        String srcFirstDay = Tools.dataChange(firstDay, "/");
                        idTextView.setText(srcFirstDay);
                        break;
                    case "days":
                        ArrayList<String> dateArray = DataCenter.pData.getSiteDays(orderNum);
                        //Log.e(TAG, "TableLayoutUpdate: dateArray" + dateArray.toString());
                        idTextView = (TextView) mTableLayoutBookingList.findViewWithTag(idTag);
                        String dateSrc = "";
                        if (dateArray.size() > 1) {
                            //Log.e(TAG, "TableLayoutUpdate: DataCegetItem: mDateManager.getYMD():" + mDateManager.getYMD());
                            int indexOf = dateArray.indexOf(mDateManager.getYMD()) + 1;
                            dateSrc = dateArray.size() + "(" + indexOf + ")";
                        } else {
                            dateSrc = dateArray.size() + "";
                        }
                        idTextView.setText(dateSrc);
                        break;
                    case "(count_adult + count_child) as count":
                        String sOrdernum = DataCenter.pData.getSimpleData(i, "ordernum");
                        String scount_child = DataCenter.pData.getSimpleData(i, "count_child");
                        String sAllcount = DataCenter.pData.getSimpleData(i, "(count_adult + count_child) as count");

                        //Log.e(TAG, "TableLayoutUpdate: ordernum : "+sOrdernum );
                        //Log.e(TAG, "TableLayoutUpdate: count_child : "+scount_child );

                        idTextView = (TextView) mTableLayoutBookingList.findViewWithTag(i * mTableRowIdArray.length + 3);
                        if (!scount_child.isEmpty()) {
                            if (Integer.parseInt(scount_child) > 0) {

                                idTextView.setText(sAllcount + "(" + scount_child + ")");
                            }
                        }
                        break;
                    case "count(siteid) as countsiteid":
                        String tOrdernum = DataCenter.pData.getSimpleData(i, "ordernum");
                        ArrayList<Integer> nListGetSiteRooms = DataCenter.pData.getSiteRooms(tOrdernum);
                        //int nOrderNum=Integer.parseInt(tOrdernum);

                        StringBuilder allRoomName = new StringBuilder();
                        boolean bFind = false;
                        idTextView = (TextView) mTableLayoutBookingList.findViewWithTag(i * mTableRowIdArray.length + 4);
                        for (int id : nListGetSiteRooms) {
                            for (Map.Entry<Integer, String> entry : Rooms.mapRoomNames.entrySet()) {

                                int nGetKey = entry.getKey();
                                if (id == nGetKey) {
                                    bFind = true;

                                    //Log.e(TAG, "TableLayoutUpdate: id == nGetKey " + id);
                                    //Log.e(TAG, "TableLayoutUpdate: id == nGetKey get" + Rooms.mapRoomNames.get(id));
                                    allRoomName.append(Rooms.mapRoomNames.get(id)).append(" ");
                                    String LastSrc = Tools.SrcContent(allRoomName.toString(), 15);
                                    idTextView.setText(LastSrc);

                                }
                            }
                        }
                        if (!bFind) {
                            idTextView.setText("");
                            idTextView.setEnabled(false);
                        }

                        //Log.e(TAG, "TableLayoutUpdate: ordernum " + tOrdernum);
                        //Log.e(TAG, "TableLayoutUpdate: Rooms.getRoomName(nOrderNum) " + Rooms.getRoomName(nOrderNum));
                        //Log.e(TAG, "TableLayoutUpdate: tempGetSiteRooms " + nListGetSiteRooms);
                        break;
                    case "canceltime":
                        /*Log.e(TAG, "TableLayoutUpdate: canceltime "+
                                DataCenter.pData.getSimpleData(i ,"canceltime")
                        );*/
                        String canceltime = DataCenter.pData.getSimpleData(i, "canceltime");
                        if (!canceltime.isEmpty()) {

                            idTextView = (TextView) mTableLayoutBookingList.findViewWithTag(i * mTableRowIdArray.length + 6);
                            idTextView.setText(canceltime);

                            idTextView = (TextView) mTableLayoutBookingList.findViewWithTag(i * mTableRowIdArray.length + 4);
                            idTextView.setText("キャンセル");


                            //キャンセル 黒い表示
                            for (int k = 0; k < mShowColumns; k++) {   //
                                int kTag = i * mTableRowIdArray.length + k;    //i*100 + j

                                TextView kTextView;
                                kTextView = (TextView) mTableLayoutBookingList.findViewWithTag(kTag);
                                kTextView.setEnabled(false);

                                kTextView.setTextColor(Color.BLACK);
                            }
                        }
                        break;
                    case "ordernum":
                        /*Log.e(TAG, "TableLayoutUpdate: ordernum " +
                                DataCenter.pData.getSimpleData(i, "ordernum")
                        );*/
                        break;
                }
            }

            //row show
            TableRow tableRow = (TableRow) mTableLayoutBookingList.findViewById(mTableRowIdArray[i]);
            tableRow.setVisibility(View.VISIBLE);
            tableRow.setMinimumHeight(80);
        }
        mTableLayoutBookingList.setMinimumHeight(0);
        mTableLayoutFrag01_TableLayout_All.setMinimumHeight(0);


    }

    //Layout画面更新
    @SuppressLint("SetTextI18n")
    private void FromUpdate() {
        //table_row_id_no更新
        /*for (int i = 0; i < table_row_id.length; i++) {

            int indexTable_row_id_no = i + (currentPage - 1) * table_row_id.length + 1;
            TextView TextViewRowIdNo = (TextView) tableLayoutBookingList.findViewById(table_row_id_no[i]);
            TextViewRowIdNo.setText("" + indexTable_row_id_no);
        }*/


        //「数量」表示
        TextView textViewFrag01Amount = (TextView) mTableLayoutPageSelect.findViewById(R.id.Frag01Amount);
        textViewFrag01Amount.setText("数量：" + DataCenter.pData.getSimpleDataArrayListSize());

        //「翌日」ボタン
        Button buttonNext = (Button) mTableLayoutPageSelect.findViewById(R.id.Frag01ButtonNext);
        /*if (currentPage >= getOrderListLength / 10 + 1) {
            buttonNext.setVisibility(View.INVISIBLE);
        } else {*/
        buttonNext.setVisibility(View.VISIBLE);
        //}


        //「前日」ボタン
        Button buttonPre = (Button) mTableLayoutPageSelect.findViewById(R.id.Frag01ButtonPrev);
        /*if (currentPage <= 1) {
            buttonPre.setVisibility(View.INVISIBLE);
        } else {*/
        buttonPre.setVisibility(View.VISIBLE);
        //}
    }


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

                Frag01DialogFragment frag01DialogFragment = new Frag01DialogFragment(ordernum, getSelectDate());
                frag01DialogFragment.setTargetFragment(Frag01.this, 1);
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
                Frag01SiteFragment frag01SiteFragment = new Frag01SiteFragment(ordernum, getSelectDate());
                frag01SiteFragment.setTargetFragment(Frag01.this, 1);
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
        View view = inflater.inflate(R.layout.frag01_layout, container, false);
        ButterKnife.bind(this, view);


        mTableLayoutBookingList = (TableLayout) view.findViewById(R.id.予約一覧);
        mTableLayoutFrag01_TableLayout_All = (TableLayout) view.findViewById(R.id.Frag01_TableLayout_All);
        mTableLayoutPageSelect = (TableLayout) view.findViewById(R.id.Frag01_page_select);


        //簡単記録
        ArrayList<TableRow> arrayListTableRow = new ArrayList<TableRow>();
        for (int value : mTableRowIdArray) {
            arrayListTableRow.add((TableRow) mTableLayoutBookingList.findViewById(value));
        }

        for (int i = 0; i < mTableRowIdArray.length; i++) {
            for (int j = 0; j < mShowColumns; j++) {
                TextView textView = CreateStyleTextView("");
                int mTag = i * mTableRowIdArray.length + j;
                textView.setTag(mTag);

                //テスト使用 begin
                textView.setText("" + mTag);
                //テスト使用 end

                //詳細内容を見る
                if (j == 2) {
                    textView.setTextColor(Color.RED);
                    setTextView2DialogFragment(textView, i);
                }

                //サイト内容を見る
                if (j == 4) {
                    textView.setTextColor(Color.RED);
                    setTextView2SiteFragment(textView, i);
                }
                /*switch (mShowColumns){
                    default:
                        textView.setMinWidth(100);
                        break;
                }*/

                arrayListTableRow.get(i).addView(textView, j);
            }
        }


        //「ページ」表示
        /*TextView textViewCurrentPage = (TextView) tableLayoutPageSelect.findViewById(R.id.Frag01CurrentPage);
        textViewCurrentPage.setText("ページ：" + currentPage);*/

        //「数量」表示
        TextView textViewFrag01Amount = (TextView) mTableLayoutPageSelect.findViewById(R.id.Frag01Amount);
        textViewFrag01Amount.setText("数量：" + DataCenter.pData.getSimpleDataArrayListSize());

        //「次へ」ボタン
        Button buttonNext = (Button) mTableLayoutPageSelect.findViewById(R.id.Frag01ButtonNext);
        /*if (currentPage >= getOrderListLength / table_row_id_no.length + 1) {
            buttonNext.setVisibility(View.INVISIBLE);
        } else {
            buttonNext.setVisibility(View.VISIBLE);
        }*/

        //「更新」ボタン
        Button buttonUpdate = (Button) mTableLayoutPageSelect.findViewById(R.id.Frag01Update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DataCenter.UpdateData();
                        }
                    }).start();
                    Frag01DataUpdate(0);
                    //「数量」表示
                    TextView textViewFrag01Amount = (TextView) mTableLayoutPageSelect.findViewById(R.id.Frag01Amount);
                    textViewFrag01Amount.setText("数量：" + DataCenter.pData.getSimpleDataArrayListSize());
                } catch (JSONException e) {
                    Log.e(TAG, "onClick: ", e);
                    e.printStackTrace();
                }
            }
        });


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Frag01DataUpdate(1);
                } catch (JSONException e) {
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });

        //「前日」ボタン
        Button buttonPre = (Button) mTableLayoutPageSelect.findViewById(R.id.Frag01ButtonPrev);

        buttonPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Frag01DataUpdate(-1);
                } catch (JSONException e) {
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });


        //「検索日　選択」セット begin
        mTextViewBookingDate = view.findViewById(R.id.textView_frag01_booking_date);
        if (mStringBookingDate.isEmpty()) {
            String str = String.format(
                    Locale.US,
                    "%d/%d/%d",
                    mDateManager.getYear(),
                    mDateManager.getMonth(),
                    mDateManager.getDay()
            );
            mStringBookingDate = str;
            mTextViewBookingDate.setText(str);

        }
        mTextViewBookingDate.setText(mStringBookingDate);
        mTextViewBookingDate.addTextChangedListener(this);

        Button mOpenDialog_booking_date = view.findViewById(R.id.pick_frag01_date_search_date);
        mOpenDialog_booking_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*selectDate = CalendarDay.from(
                        mDateManager.getYear()
                        , mDateManager.getMonth()
                        , mDateManager.getDay()
                );*/
                //Log.e(TAG, "onClick: selectDate:"+selectDate.toString() );
                Frag01SelectFragment frag01SelectFragment = new Frag01SelectFragment(mTextViewBookingDate);
                frag01SelectFragment.setTargetFragment(Frag01.this, 1);
                assert getFragmentManager() != null;
                frag01SelectFragment.show(getFragmentManager(), "frag01SelectFragment");

            }
        });


        Button mOpenDialog_booking_date_old = view.findViewById(R.id.pick_frag01_date_search_date_old);
        mOpenDialog_booking_date_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePick dialog = new DatePick();

                dialog.setTargetFragment(Frag01.this, 1);
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "booking date Dialog");
            }
        });
        //「検索日　選択」セット end

        //記録更新
        try {
            //表示画面更新
            Frag01DataUpdate(0);
        } catch (JSONException e) {
            Log.e(TAG, "onCreateView: ", e);
        }
        return view;
    }

    TextView CreateStyleTextView(String strText) {
        TextView textView = new TextView(getActivity());
        textView.setText(strText);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }


    private Timer timer;
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
        timer.schedule(timerTask, second, Config.minuteFrag01DataUpdate);
    }

    /**
     * @param view       the picker associated with the dialog
     * @param year       the selected year
     * @param month      the selected month (0-11 for compatibility with
     * @param dayOfMonth the selected day of the month (1-31, depending on
     */

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.e(TAG, "onDateSet int year"+year+", int month"+month+", int dayOfMonth"+dayOfMonth);

        try {
            mDateManager.setDate(year, month + 1, dayOfMonth);

            Frag01DataUpdate(0);

            //テスト
            //DataCenter.pData.testSql(getSelectDate());
            //DataCenter.pJsonControl.getSiteRooms("20b4g013");
        } catch (Exception e) {
            Log.e(TAG, "onDateSet: ", e);
        }
        Log.d(TAG, "onDateSet end");
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
        if(bAfterTextChanged){
            Log.e(TAG, "beforeTextChanged: mDateManager.getYMD():"+mDateManager.getYMD() );
            bAfterTextChanged=false;
            try {
                Frag01DataUpdate(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        /*Log.e(TAG, "afterTextChanged: s.toString():" + s.toString());
        //Log.e(TAG, "afterTextChanged: selectDate.toString():" + selectDate.toString());
        String t=s.toString();
        if(bAfterTextChanged){
            bAfterTextChanged=false;
            try {
                Frag01DataUpdate(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
    }

    public static boolean bAfterTextChanged=false;

    //時間task
    class CountUpTimerTask extends TimerTask {
        @Override
        public void run() {
            // handlerを使って処理をキューイングする
            handler.post(new Runnable() {
                public void run() {
                    try {
                        Log.d(TAG, "CountUpTimerTask class  毎60秒更新一回");

                        //表示画面更新
                        Frag01DataUpdate(0);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DataCenter.UpdateData();
                            }
                        }).start();
                    } catch (JSONException e) {
                        Log.e(TAG, "run: ", e);
                    }
                }
            });
        }
    }

    @Override
    public void sendInput(String input) {
        Log.d(TAG, "Frag01 from Frag01DialogFragment get input: " + input);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

//特定日期 按钮 end