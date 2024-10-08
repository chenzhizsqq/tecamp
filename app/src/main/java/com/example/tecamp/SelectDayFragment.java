package com.example.tecamp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tecamp.decorators.EventDecorator;
import com.example.tecamp.decorators.HighlightWeekendsDecorator;
import com.example.tecamp.sql.DataCenter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
* 検索日 選択
* */
public class SelectDayFragment extends DialogFragment
        implements OnDateSelectedListener, OnMonthChangedListener {

    private static final String TAG = "SelectDayFragment";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE曜日, yyyy年 MMM d日 ");
    private TextView mTV_date_src;
    private final TextView mTV_Frag01_date;
    private DateManager mSelectDate;

    SelectDayFragment(TextView _TextView, DateManager _selectDate) {
        mTV_Frag01_date = _TextView;
        mSelectDate = _selectDate;
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.calendarView_tab)
    public MaterialCalendarView widget;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.frag_select_day_layout);

        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.CENTER;//下方
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;//满屏
        /*attributes.height = WindowManager.LayoutParams.MATCH_PARENT;//满屏*/

        window.setAttributes(attributes);

        return dialog;
    }


    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_select_day_layout, container, false);

        ButterKnife.bind(this, view);
        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);

        //表示設定
        widget.setHeaderTextAppearance(R.style.TextAppearance_AppCompat_Large);
        widget.setDateTextAppearance(R.style.TextAppearance_AppCompat_Large);
        widget.setWeekDayTextAppearance(R.style.TextAppearance_AppCompat_Large);
        //widget.setTileSize(LinearLayout.LayoutParams.MATCH_PARENT);


        //日本年月表示方法
        widget.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                StringBuffer buffer = new StringBuffer();
                int yearOne = day.getYear();
                int monthOne = day.getMonth() ;
                buffer.append(yearOne).append("年").append(monthOne).append("月");
                return buffer;
            }
        });

        MonthUpdate(widget, mSelectDate.getYear(), mSelectDate.getMonth());
        CalendarDay day = CalendarDay.from(mSelectDate.getYear(),
                mSelectDate.getMonth(),
                mSelectDate.getDay());
        widget.setCurrentDate(day);

        mTV_date_src = view.findViewById(R.id.dialog_frag01_date_src);

        Button mActionCancel = view.findViewById(R.id.action_cancel);
        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog");
                //Toast.makeText(getContext(), "戻る", Toast.LENGTH_SHORT).show();
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });


        Button mActionSelect = view.findViewById(R.id.action_select);
        mActionSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectDate!=null){

                    mTV_Frag01_date.setText(mSelectDate.getYMD("/"));

                    Objects.requireNonNull(getDialog()).dismiss();
                }else{
                    Toast.makeText(getContext(), "選択ください", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * Called when a user clicks on a day.
     * There is no logic to prevent multiple calls for the same date and state.
     *
     * @param widget   the view associated with this listener
     * @param date     the date that was selected or unselected
     * @param selected true if the day is now selected, false otherwise
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSelected(
            @NonNull MaterialCalendarView widget,
            @NonNull CalendarDay date,
            boolean selected) {

        //Log.e(TAG, "onDateSelected: " + (selected ? FORMATTER.format(date.getDate()) : "選択ください"));
        mSelectDate.setDate(date.getYear(),date.getMonth(),date.getDay());

        /*mDateManager.setDate(date);

        Frag01DataUpdate(0);*/
        //Log.e(TAG, "onDateSelected: " + FORMATTER.format(date.getDate()));
    }


    private void MonthUpdate(MaterialCalendarView widget, int year, int month) {
        try {

            //Log.e(TAG, "MonthUpdate: year:"+year+",month:"+month );
            widget.removeDecorators();
            //土曜日　日曜日
            widget.addDecorator(new HighlightWeekendsDecorator());


            //DAY選択出来ない　設定
            widget.addDecorator(new PrimeDayDisableDecorator2());

            //毎月スクリーン　更新
            HashMap<String, String> getMonthData = DataCenter.pData.getRoomsOneMonth(year, month);
            //Log.e(TAG, "onCreateView: getRoomsOneMonth:" + getMonthData.toString());

            ArrayList<CalendarDay> datesArray = new ArrayList<>();
            for (HashMap.Entry<String, String> entry : getMonthData.entrySet()) {
                datesArray.clear();
                //Log.e(TAG, "onCreateView: key value :" + entry.getKey() + " : " + entry.getValue());

                int y = Tools.dataGetYear(entry.getKey());
                int m = Tools.dataGetMonth(entry.getKey());
                int d = Tools.dataGetDay(entry.getKey());

                LocalDate localDate = LocalDate.of(y, m, d);
                CalendarDay singleDay = CalendarDay.from(localDate);
                datesArray.add(singleDay);
                widget.addDecorator(new EventDecorator(datesArray, "" + entry.getValue()));
            }
        } catch (Exception e) {
            Log.e(TAG, "MonthUpdate: ", e);
        }
    }


    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        try {
            MonthUpdate(widget, date.getYear(), date.getMonth());
        } catch (Exception e) {
            Log.e(TAG, "onMonthChanged: ", e);
        }
    }


    //選択できないDAY
    private static class PrimeDayDisableDecorator2 implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(final CalendarDay day) {
            return PRIME_TABLE[day.getDay()];
        }

        @Override
        public void decorate(final DayViewFacade view) {

            view.setDaysDisabled(true);
        }

        //trueは選択できる。falseは表示選択できません。
        private static boolean[] PRIME_TABLE = {
                true,  // 0?
                true,
                true, // 2
                true, // 3
                true,
                true, // 5
                true,
                true, // 7
                true,
                true,
                true,
                true, // 11
                true,
                true, // 13
                true,
                true,
                true,
                true, // 17
                true,
                true, // 19
                true,
                true,
                true,
                true, // 23
                true,
                true,
                true,
                true,
                true,
                true, // 29
                true,
                true, // 31
                true,
                true,
                true, //PADDING
        };
    }
}