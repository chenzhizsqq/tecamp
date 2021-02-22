package com.example.tecamp;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.tecamp.sql.DataCenter;
import com.example.tecamp.ui.main.SectionsPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.function.Consumer;

import butterknife.ButterKnife;

//予約検索
public class Frag03 extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "Frag03";
    private TextView textView_stay_date;
    private TextView textView_booking_date;
    private EditText editTextName;

    private String string_stay_date;
    private String string_booking_date;

    private int nSelectView;

    String[] dataArray = {
            "date"
            , "address"
            , "tel_1||'-'||tel_2||'-'||tel_3"
            , "username||' '||username2||'('||username_kana||' '||username_kana2||')'"
            , "days"
            , "siteid"
            , "site_count"
            , "createtime"
    };


    ViewPager viewPager;

    public Frag03(ViewPager _viewPager){
        viewPager=_viewPager;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        string_stay_date = "";
        string_booking_date = "";
        nSelectView = 0;
    }


    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag03_layout, container, false);
        ButterKnife.bind(this, view);


        string_stay_date="";

        //stay_date
        textView_stay_date = view.findViewById(R.id.frag03_textView_stay_date);
        if (!string_stay_date.isEmpty()) {
            textView_stay_date.setText("");
        }

        Button mOpenDialog_stay_date = view.findViewById(R.id.frag03_pick_date_stay_date);
        mOpenDialog_stay_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nSelectView = 1;
                DatePick dialog = new DatePick();
                dialog.setTargetFragment(Frag03.this, 1);
                dialog.show(getFragmentManager(), "stay date Dialog");
            }
        });
        //stay_date

        //booking_date
        textView_booking_date = view.findViewById(R.id.frag03_textView_booking_date);
        if (string_booking_date.isEmpty()) {

        } else {
            textView_booking_date.setText(string_booking_date);
        }
        Button mOpenDialog_booking_date = view.findViewById(R.id.frag03_pick_date_booking_date);
        mOpenDialog_booking_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nSelectView = 2;
                DatePick dialog = new DatePick();
                dialog.setTargetFragment(Frag03.this, 1);
                dialog.show(getFragmentManager(), "booking date Dialog");
            }
        });

        editTextName = view.findViewById(R.id.frag03EditTextName);

        Button mButtonSearch = view.findViewById(R.id.frag03_button_search);
        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                if (editTextName.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "名前を入力してください", Toast.LENGTH_SHORT).show();

                } else {
*/

                try {
                    String selectUsername = editTextName.getText().toString();

                    //データ処理
                    String selectData = Arrays.toString(dataArray);
                    selectData = selectData.substring(1, selectData.length() - 1);
                    //Log.e(TAG, "onClick: selectData:" + selectData);

                    String sql = "select " + selectData + " from etcamp_order where (username like \"%" + selectUsername
                            + "%\" or username2 like \"%" + selectUsername
                            + "%\" or username_kana like \"%" + selectUsername
                            + "%\" or username_kana2 like \"%" + selectUsername
                            + "%\")";
                    if (!string_stay_date.isEmpty()) {

                        sql += " and date like '" + sqlDate1 + "%'";
                    }
                    if (!string_booking_date.isEmpty()) {

                        sql += " and createtime like '" + sqlDate2 + "%'";
                    }
                    sql += "  order by date";
                    //Log.e(TAG, "onClick: sql:" + sql);


                    mSqlGetArrayMap = DataCenter.pData.SqlGetArrayMap(sql);


                    //TableLayout処理
                    TableLayout mTableLayout = view.findViewById(R.id.frag03_予約一覧);
                    mTableLayout.removeAllViews();

                    //状態表示
                    TableRow mTableRowState = new TableRow(getActivity());
                    mTableRowState.addView(makeTextView("検索条件：　"));
                    mTableRowState.addView(makeTextView("名前：" + editTextName.getText().toString()));

                    mTableRowState.addView(makeTextView("宿泊日:" + textView_stay_date.getText().toString()));

                    mTableRowState.addView(makeTextView("予約日:" + textView_booking_date.getText().toString()));
                    mTableLayout.addView(mTableRowState, mTableLayout.getChildCount());

                    //TableLayout title処理
                    TableRow mTableRowTitle = new TableRow(getActivity());
                    if (mSqlGetArrayMap.size() > 0) {
                        for (int j = 0; j < dataArray.length; j++) {
                            mTableRowTitle.addView(makeTextView(dataArray[j], 20), j);
                        }
                        mTableLayout.addView(mTableRowTitle, mTableLayout.getChildCount());
                    }


                    //TableLayout データ各行処理
                    for (int i = 0; i < mSqlGetArrayMap.size(); i++) {
                        //Log.e(TAG, "onClick: mSqlGetArrayMap:"+i+":"+mSqlGetArrayMap.get(i) );

                        TableRow mTableRow = new TableRow(getActivity());

                        HashMap<String, String> map = mSqlGetArrayMap.get(i);
                        //Log.e(TAG, "onClick: map:" + map.toString());

                        for (int j = 0; j < dataArray.length; j++) {
                            String data = map.get(dataArray[j]);
                            //Log.e(TAG, "onClick: "+dataArray[j]+":" + data);
                            assert data != null;
                            mTableRow.addView(makeTextView(data, 20), j);
                        }

                        mTableLayout.addView(mTableRow, mTableLayout.getChildCount());
                    }

                } catch (Exception e) {
                    Log.e(TAG, "mButtonSearch onClick: ", e);
                } finally {
                    TextView textView_stay_date2 = view.findViewById(R.id.frag03_textView_stay_date);
                    textView_stay_date2.setText("");
                    textView_stay_date2.setHint("宿泊日選択");

                    TextView textView_booking_date2 = view.findViewById(R.id.frag03_textView_booking_date);
                    textView_booking_date2.setText("");
                    textView_booking_date2.setHint("予約日選択");
                    editTextName.setText("");
                    sqlDate1 = "";
                    sqlDate2 = "";
                }
            }
            //}
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    @NotNull
    private TextView makeTextView(String text) {
        TextView textView = new TextView(getActivity());
        textView.setText(text);
        textView.setPadding(5, 1, 5, 1);
        //Log.e(TAG, "makeTextView: text:"+text );
        return textView;
    }

    @SuppressLint("SetTextI18n")
    @NotNull
    private TextView makeTextView(String text, int maxLength) {
        String mText = text.substring(0, Math.min(text.length(), maxLength));
        //Log.e(TAG, "makeTextView: mText:"+mText );
        return makeTextView(mText);
    }

    ArrayList<HashMap<String, String>> mSqlGetArrayMap;
    String sqlDate1;
    String sqlDate2;

    /**
     * @param view       the picker associated with the dialog
     * @param year       the selected year
     * @param month      the selected month (0-11 for compatibility with
     * @param dayOfMonth the selected day of the month (1-31, depending on
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        try {

            String str = String.format(Locale.US, "%d/%d/%d", year, month + 1, dayOfMonth);

            String y = String.format(Locale.US, "%d", year);
            String m = String.format(Locale.US, "%d", month + 1);
            String d = String.format(Locale.US, "%d", dayOfMonth);
            String sqlM = m.length() > 1 ? m : "0" + m;
            String sqlD = d.length() > 1 ? d : "0" + d;
            //Log.e(TAG, "onDateSet: sqlDate:" + sqlDate1);

            switch (nSelectView) {
                case 1:
                    textView_stay_date.setText(str);
                    string_stay_date = str;
                    sqlDate1 = y + sqlM + sqlD;
                    break;
                case 2:
                    textView_booking_date.setText(str);
                    string_booking_date = str;
                    sqlDate2 = y + sqlM + sqlD;
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "onDateSet: ", e);
        }
    }
}
