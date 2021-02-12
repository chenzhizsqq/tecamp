package com.example.tecamp;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import butterknife.ButterKnife;

//予約検索
public class Frag03 extends Fragment  implements DatePickerDialog.OnDateSetListener {

    private TextView textView_stay_date;
    private TextView textView_booking_date;

    private String string_stay_date;
    private String string_booking_date;

    private int nSelectView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        string_stay_date = "";
        string_booking_date = "";
        nSelectView = 0;
    }


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag03_layout, container, false);
        ButterKnife.bind(this, view);


        //stay_date
        textView_stay_date = view.findViewById(R.id.textView_stay_date);
        if(string_stay_date.isEmpty()){

        }else{
            textView_stay_date.setText(string_stay_date);
        }
        Button mOpenDialog_stay_date = view.findViewById(R.id.pick_date_stay_date);
        mOpenDialog_stay_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                nSelectView = 1;
                DatePick dialog = new DatePick();
                dialog.setTargetFragment(Frag03.this, 1);
                dialog.show(getFragmentManager(), "stay date Dialog");
            }
        });
        //stay_date

        //booking_date
        textView_booking_date = view.findViewById(R.id.textView_booking_date);
        if(string_booking_date.isEmpty()){

        }else{
            textView_booking_date.setText(string_booking_date);
        }
        Button mOpenDialog_booking_date = view.findViewById(R.id.pick_date_booking_date);
        mOpenDialog_booking_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                nSelectView = 2;
                DatePick dialog = new DatePick();
                dialog.setTargetFragment(Frag03.this, 1);
                dialog.show(getFragmentManager(), "booking date Dialog");
            }
        });


        return view;
    }


    /**
     * @param view       the picker associated with the dialog
     * @param year       the selected year
     * @param month      the selected month (0-11 for compatibility with
     * @param dayOfMonth the selected day of the month (1-31, depending on
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String str = String.format(Locale.US, "%d/%d/%d",year, month+1, dayOfMonth);
        switch (nSelectView)
        {
            case 1:
                textView_stay_date.setText( str );
                string_stay_date=str;
                break;
            case 2:
                textView_booking_date.setText(str);
                string_booking_date=str;
                break;
        }
    }
}
