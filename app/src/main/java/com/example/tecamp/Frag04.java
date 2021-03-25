package com.example.tecamp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import androidx.viewpager.widget.ViewPager;

import com.example.tecamp.sql.DataCenter;
import com.example.tecamp.ui.main.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;


/**
* 電話受付
* */
public class Frag04 extends Fragment implements
        DatePickerDialog.OnDateSetListener
        , BookingDialogFragment.OnInputSelected {


    private static final int mSrcDays = 7;
    private final DateManager[] arrayDate = new DateManager[mSrcDays];

    private final DateManager mDateManager = new DateManager();
    private static final String TAG = "Frag04";


    private TableLayout tableLayout_table;
    private TableLayout tableLayout_menu;
    private static final TextView[] textView_Src_Days = new TextView[mSrcDays];

    //Frag04_TableLayout_tableに予約記録、tagのArrayList
    private final ArrayList<String> tagArrayList=new ArrayList<>();


    ViewPager viewPager;

    public Frag04(ViewPager _viewPager){
        viewPager=_viewPager;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < mSrcDays; i++) {
            arrayDate[i] = new DateManager();
            arrayDate[i].addDay(i);
        }
    }


    private TextView createTextView(String str, String tag) {
        TextView textView = new TextView(getActivity());
        textView.setText(str);
        textView.setTag(tag);


        textView.setTextSize(20);
        textView.setWidth(230);
        textView.setTextAlignment(android.view.View.TEXT_ALIGNMENT_CENTER);
        textView.setPaddingRelative(5, 5, 5, 5);
        return textView;
    }


    @SuppressLint("SetTextI18n")
    private void Frag04DataUpdate(int _addDay) {
        try {

            mDateManager.addDay(_addDay);

            for (int i = 0; i < mSrcDays; i++) {
                arrayDate[i].addDay(_addDay);

                //textView_Src_Days[i].setText(arrayDate[i].getYear() + "/" + arrayDate[i].getMonth() + "/" + arrayDate[i].getDay());
                textView_Src_Days[i].setText(Tools.dataChange(arrayDate[i].getYMD(), "/"));

                //textView_Src_Days[i].setText(arrayDate[i].getYMD());
            }

            TextView TV_frag04_current_date = tableLayout_menu.findViewById(R.id.frag04_current_date);
            //TV_frag04_current_date.setText(arrayDate[0].getYear() + "/" + arrayDate[0].getMonth() + "/" + arrayDate[0].getDay());
            TV_frag04_current_date.setText(Tools.dataChange(arrayDate[0].getYMD(), "/"));

            for (int i = 0; i < mSrcDays; i++) {
                //Log.e(TAG, "onCreate: arrayDateManager:" + arrayDate[i].getYMD());

                String date = arrayDate[i].getYMD();
                String sql = "select siteid,username ||username2 from etcamp_SiteList a,etcamp_order b where a.orderid=b.orderid and siteid>0 and ymd=" + date;
                //Log.e(TAG, "Frag04DataUpdate: sql:" + sql);
                HashMap<String, String> map = DataCenter.pData.SqlGetStringMap(sql);
                //Log.e(TAG, "Frag04DataUpdate: map:" + map.toString());

                //clear
                for (Map.Entry<Integer, String> entry : Rooms.mapRoomNames.entrySet()) {

                    String key = entry.getKey() + "";
                    String tag = key + "_date_" + i;
                    TextView textView = (TextView) tableLayout_table.findViewWithTag(tag);
                    textView.setText("");
                }

                //更新
                for (Map.Entry<String, String> entry : map.entrySet()) {

                    String key = entry.getKey();
                    if (key.isEmpty()) continue;
                    if (map.get(key).isEmpty()) continue;


                    String tag = key + "_date_" + i;
                    if(tagArrayList.contains(tag)){

                        TextView textView = (TextView) tableLayout_table.findViewWithTag(tag);
                        textView.setText(map.get(key));
                    }

                }


            }


        } catch (Exception e) {
            Log.e(TAG, "Frag04DataUpdate: ", e);
        }
    }

    //@SuppressLint({"WrongConstant", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag04_layout, container, false);
        ButterKnife.bind(this, view);

        try {

            tableLayout_menu = (TableLayout) view.findViewById(R.id.Frag04_TableLayout_menu);
            tableLayout_table = (TableLayout) view.findViewById(R.id.Frag04_TableLayout_table);


            textView_Src_Days[0] = tableLayout_table.findViewById(R.id.Frag04_textView_date_0);
            textView_Src_Days[1] = tableLayout_table.findViewById(R.id.Frag04_textView_date_1);
            textView_Src_Days[2] = tableLayout_table.findViewById(R.id.Frag04_textView_date_2);
            textView_Src_Days[3] = tableLayout_table.findViewById(R.id.Frag04_textView_date_3);
            textView_Src_Days[4] = tableLayout_table.findViewById(R.id.Frag04_textView_date_4);
            textView_Src_Days[5] = tableLayout_table.findViewById(R.id.Frag04_textView_date_5);
            textView_Src_Days[6] = tableLayout_table.findViewById(R.id.Frag04_textView_date_6);
            /*for (int i = 0; i < mSrcDays; i++) {
                textView_Src_Days[i].setText(arrayDate[i].getMonth() + "/" + arrayDate[i].getDay());
                textView_Src_Days[i].setText(arrayDate[i].getYMD());
            }*/

            TextView TV_frag04_current_date = tableLayout_menu.findViewById(R.id.frag04_current_date);
            //TV_frag04_current_date.setText(arrayDate[0].getYear() + "/" + arrayDate[0].getMonth() + "/" + arrayDate[0].getDay());

            TableRow tableRow;
            int roomIndex = 0;
            for (Map.Entry<Integer, String> entry : Rooms.mapRoomNames.entrySet()) {
                tableRow = new TableRow(getActivity());
                int color = getResources().getColor(R.color.navy, getActivity().getTheme());
                tableRow.setBackgroundColor(color);


                TextView textViewNo = createTextView(Rooms.getRoomName(entry.getKey()), Rooms.getRoomName(entry.getKey()));
                tableRow.addView(textViewNo);

                for (int dayIndex = 0; dayIndex < mSrcDays; dayIndex++) {

                    String tag = entry.getKey() + "_date_" + dayIndex;
                    TextView textView = createTextView("", tag);
                    textView.setTextColor(Color.BLACK);

                    tagArrayList.add(tag);

                    tableRow.addView(textView, dayIndex + 1);
                }
                tableLayout_table.addView(tableRow, roomIndex + 1);
                roomIndex++;
            }


            //「検索日　選択」セット begin


            Button mOpenDialog_booking_date = tableLayout_menu.findViewById(R.id.pick_date_search_date);
            mOpenDialog_booking_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatePick dialog = new DatePick();
                    dialog.setTargetFragment(Frag04.this, 1);
                    dialog.show(getFragmentManager(), "booking date Dialog");
                }
            });
            //「検索日　選択」セット end




            Button buttonNext = (Button) tableLayout_menu.findViewById(R.id.Frag04ButtonNext);
            buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Frag04DataUpdate(1);
                    }catch (Exception e){
                        Log.e(TAG, "onClick: ",e );
                    }
                }
            });

            Button buttonPrev = (Button) tableLayout_menu.findViewById(R.id.Frag04ButtonPrev);
            buttonPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        Frag04DataUpdate(-1);
                    }catch (Exception e){
                        Log.e(TAG, "onClick: ",e );
                    }
                }
            });


            Button buttonNextWeek = (Button) tableLayout_menu.findViewById(R.id.Frag04ButtonNextWeek);
            buttonNextWeek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        Frag04DataUpdate(7);
                    }catch (Exception e){
                        Log.e(TAG, "onClick: ",e );
                    }
                }
            });

            Button buttonPrevWeek = (Button) tableLayout_menu.findViewById(R.id.Frag04ButtonPrevWeek);
            buttonPrevWeek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        Frag04DataUpdate(-7);
                    }catch (Exception e){
                        Log.e(TAG, "onClick: ",e );
                    }
                }
            });

            //Data Update
            Frag04DataUpdate(0);
        }catch (Exception e){
            Log.e(TAG, "onCreateView: ",e );
        }

        return view;
    }


    @Override
    public void sendInput(String input) {

        Log.d(TAG, "sendInput: found incoming input: " + input);
    }

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
            mDateManager.setDate(year, month + 1, dayOfMonth);

            for (int i = 0; i < mSrcDays; i++) {
                arrayDate[i].setDate(year, month + 1, dayOfMonth);
                arrayDate[i].addDay(i);
            }

            Frag04DataUpdate(0);

        } catch (Exception e) {
            Log.e(TAG, "onDateSet: ", e);
        }
    }
}
