package com.example.tecamp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.tecamp.sql.DataCenter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
* 毎日予約の場合
* */
public class OrderSiteFragment extends DialogFragment {

    /*private static final int[] roomId = {
            R.id.roomText0_0,
            R.id.roomText0_1,
            R.id.roomText0_2,
            R.id.roomText0_3,
            R.id.roomText0_4,
            R.id.roomText0_5,
            R.id.roomText0_6,
            R.id.roomText0_7,

            R.id.roomText1_0,
            R.id.roomText1_1,
            R.id.roomText1_2,
            R.id.roomText1_3,
            R.id.roomText1_4,
            R.id.roomText1_5,
            R.id.roomText1_6,
            R.id.roomText1_7,

            R.id.roomText2_0,
            R.id.roomText2_1,
            R.id.roomText2_2,
            R.id.roomText2_3,
            R.id.roomText2_4,
            R.id.roomText2_5,
            R.id.roomText2_6,
            R.id.roomText2_7,

            R.id.roomText3_0,
            R.id.roomText3_1,
            R.id.roomText3_2,
            R.id.roomText3_3,
            R.id.roomText3_4,
            R.id.roomText3_5,
            R.id.roomText3_6,
            R.id.roomText3_7,

            R.id.roomText4_0,
            R.id.roomText4_1,
            R.id.roomText4_2,
            R.id.roomText4_3,
            R.id.roomText4_4,
            R.id.roomText4_5,
            R.id.roomText4_6,
            R.id.roomText4_7,

            R.id.roomText5_0,
            R.id.roomText5_1,
            R.id.roomText5_2,
            R.id.roomText5_3,
            R.id.roomText5_4,
            R.id.roomText5_5,
            R.id.roomText5_6,
            R.id.roomText5_7,

            R.id.roomText6_0,
            R.id.roomText6_1,
            R.id.roomText6_2,
            R.id.roomText6_3,
            R.id.roomText6_4,
            R.id.roomText6_5,
            R.id.roomText6_6,
            R.id.roomText6_7,

            R.id.roomText7_0,
            R.id.roomText7_1,
            R.id.roomText7_2,
            R.id.roomText7_3,
            R.id.roomText7_4,
            R.id.roomText7_5,
            R.id.roomText7_6,
            R.id.roomText7_7,

    };*/

    private EditText editText_氏名;
    static final public String[] mSrcArray = new String[]{

            "firstymd",
            "days",
            "username||username2",
            "count_child+count_adult",
            "site_count",
            "ac_count",
    };

    static final public int[] mIdArray = new int[]{
            R.id.宿泊日,
            R.id.泊数,
            R.id.氏名,
            R.id.人数,
            R.id.計,
            R.id.AC,
    };

    public static class SiteData {
        public int findViewById;
        public String detail;
    }


    public ArrayList<SiteData> arraySiteData = new ArrayList<SiteData>();

    private final ArrayList<Integer> mRoomingArray;

    private String mOrderNum;
    private String mDate;

    public OrderSiteFragment(String _orederNum, String _date) {
        mOrderNum = _orederNum;
        mDate = _date;
        mRoomingArray = DataCenter.pData.getSiteRoomsID(_orederNum);
        DataCenter.pData.updateSiteDataArrayList(_date);

        HashMap<String, String> mapOrderList = DataCenter.pData.getSiteDataMap(_orederNum);
        arraySiteData.clear();
        for (int n = 0; n < mIdArray.length; n++) {

            SiteData siteData = new SiteData();
            siteData.findViewById = mIdArray[n];
            siteData.detail = mapOrderList.get(mSrcArray[n]);

            //arraySiteDataでデータが入られる。
            arraySiteData.add(siteData);
        }


    }

    //予約済みRoom ArrayList
    ArrayList<String> mSiteidArray;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.frag_order_site_layout);

        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.CENTER;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;//满屏

        window.setAttributes(attributes);
        return dialog;
    }


    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int FP = ViewGroup.LayoutParams.MATCH_PARENT;
    private static final String TAG = "OrderSiteFragment";




    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_order_site_layout, container, false);

        editText_氏名 =(EditText) view.findViewById(R.id.氏名);
        TableLayout mRoomListTableList = (TableLayout) view.findViewById(R.id.roomListTable);

        //全部ROOM表示
        {
            TableRow mTableRow = new TableRow(getActivity());
            int nRoom = 0;
            for (Map.Entry<Integer, String> entry : Rooms.mapRoomNames.entrySet()) {
                if (nRoom % 8 == 0) {
                    mTableRow = new TableRow(getActivity());
                }
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                llp.setMargins(0, 0, 0, 0); // llp.setMargins(left, top, right, bottom);
                mTableRow.setLayoutParams(llp);
                mTableRow.setGravity(Gravity.CENTER);

                mTableRow.setTextAlignment(TableRow.TEXT_ALIGNMENT_CENTER);
                mTableRow.setBackgroundColor(Color.WHITE);


                int nGetKey = entry.getKey();
                Button mTextView = new Button(getActivity());
                makeTextViewByTag(nGetKey, mTextView);
                mTextView.setTextColor(Color.BLACK);
                mTextView.setBackgroundColor(0xFFADD8E6);

                mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(v.getTooltipText()=="選択"){
                            v.setBackgroundColor(0xFFADD8E6);
                            v.setTooltipText("");
                        }else{
                            v.setBackgroundColor(0xFFFA8072);
                            v.setTooltipText("選択");
                        }
                    }
                });

                TextViewDefault(mTextView);

                mTableRow.addView(mTextView);
                if (nRoom % 8 == 7) {

                    mRoomListTableList.addView(mTableRow, new TableLayout.LayoutParams(FP, WC));
                }
                nRoom++;
            }
            mRoomListTableList.addView(mTableRow, new TableLayout.LayoutParams(FP, WC));

        }

        //予約済みROOM
        mSiteidArray = DataCenter.pData.SqlGetStringArray("select siteid from etcamp_SiteList as a,etcamp_order as b" +
                " where a.orderid = b.orderid and b.canceltime=='' and ymd =  " + mDate);
        mSiteidArray.forEach(s -> {
            //Log.e(TAG, "onCreateView: forEach "+s );

            if (!s.isEmpty()) {

                int id = Integer.parseInt(s);
                for (Map.Entry<Integer, String> entry : Rooms.mapRoomNames.entrySet()) {

                    int nGetKey = entry.getKey();
                    if (id == nGetKey) {
                        Button mTextView = (Button) mRoomListTableList.findViewWithTag(id);
                        mTextView.setText(Rooms.getRoomName(id) + "\n予約済み");

                        TextViewDefault(mTextView);
                        mTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        mTextView.setTextColor(Color.BLACK);
                        mTextView.setBackgroundColor(Color.GRAY);
                        break;
                    }
                }
            }
        });


        //予約中ROOM特別表示
        for (int id : mRoomingArray) {
            //Log.e(TAG, "onCreateView: id:" + id);

            for (Map.Entry<Integer, String> entry : Rooms.mapRoomNames.entrySet()) {

                int nGetKey = entry.getKey();
                if (id == nGetKey) {
                    Button mTextView = (Button) mRoomListTableList.findViewWithTag(id);
                    mTextView.setText(Rooms.getRoomName(id) + "\n予約中");
                    TextViewDefault(mTextView);
                    mTextView.setBackgroundColor(0xFFFA8072);
                    mTextView.setTooltipText("選択");
                    mTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(v.getTooltipText()=="選択"){
                                v.setBackgroundColor(0xFFADD8E6);
                                v.setTooltipText("");
                            }else{
                                v.setBackgroundColor(0xFFFA8072);
                                v.setTooltipText("選択");
                            }
                        }
                    });
                    break;
                }
            }
        }


        for (int i = 0; i < arraySiteData.size(); i++) {
            TextView mTextView;
            int id = arraySiteData.get(i).findViewById;
            String str = arraySiteData.get(i).detail;
            mTextView = (TextView) view.findViewById(id);
            mTextView.setText(str);
        }

        Button mActionCancel = view.findViewById(R.id.action_cancel);
        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog");
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });


        Button mActionUpdate = view.findViewById(R.id.action_select);
        mActionUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: capturing input.");


                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        return view;
    }


    @SuppressLint("SetTextI18n")
    private void makeTextViewByTag(int i, TextView textView) {
        String _strText = Rooms.getRoomName(i);

        textView.setTag(i);
        textView.setText(_strText);

        //test
        //textView.setText("tag:"+textView.getRoomDetailTag());
        //test

    }

    private void TextViewDefault(TextView textView) {
        textView.setTextSize(14);
        textView.setWidth(240);
        textView.setHeight(110);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setGravity(Gravity.CENTER_VERTICAL);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        editText_氏名.post(new Runnable()
        {
            @Override
            public void run()
            {
                //アプリを開くと、キーボードが自動的に非表示になります。
                InputMethodManager imm =
                        (InputMethodManager) editText_氏名.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive())
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }

        });
    }
}