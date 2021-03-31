package com.example.tecamp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tecamp.sql.DataCenter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
* 客様資料
* */
public class OrderUserFragment extends DialogFragment {

    private static final String TAG = "OrderUserFragment";

    static final public String[] mSrcList= new String[]{
            "firstymd",//R.id.宿泊日,
            "days",//R.id.泊数,
            "username",//R.id.代表者氏名_姓,
            "username2",//R.id.代表者氏名_名,
            "username_kana",//R.id.フリガナ_セイ,
            "username_kana2",//R.id.フリガナ_メイ,
            "address",//R.id.住所,
            "tel_1",//R.id.電話1,
            "tel_2",//R.id.電話2,
            "tel_3",//R.id.電話3,
            "mail",//R.id.メール,
            "count_adult",//R.id.使用人数_大人,
            "count_child",//R.id.使用人数_小,
            "site_count",//R.id.サイト数,
            "ac_count",//R.id.AC希望数,
            //"siteid",//R.id.サイト名,
            "way",//R.id.乗物,
            "fee",//R.id.料金,
            "memo",//R.id.備考,
            "admemo",//R.id.管理者備考,
            "ordernum",//R.id.予約ID,

    };

    static final public int[] mIdList = new int[]{
            R.id.宿泊日,
            R.id.泊数,
            R.id.代表者氏名_姓,
            R.id.代表者氏名_名,
            R.id.フリガナ_セイ,
            R.id.フリガナ_メイ,
            R.id.住所,
            R.id.電話1,
            R.id.電話2,
            R.id.電話3,
            R.id.メール,
            R.id.使用人数_大人,
            R.id.使用人数_小,
            R.id.サイト数,
            R.id.AC希望数,
            //R.id.サイト名,
            R.id.乗物,
            R.id.料金,
            R.id.備考,
            R.id.管理者備考,
            R.id.予約ID,
    };

    public OrderUserFragment(String _orederNum, String _date) {
        mRoomingArray = DataCenter.pData.getSiteRoomNames(_orederNum);

        HashMap<String, String> mapOrderList = DataCenter.pData.getDialogDataArray(_orederNum,_date);
        arrayDetailData.clear();
        for (int n = 0; n < mIdList.length; n++) {
            detailData mDetailData = new detailData();
            mDetailData.findViewById = OrderUserFragment.mIdList[n];
            mDetailData.detail = mapOrderList.get(mSrcList[n]);
            arrayDetailData.add(mDetailData);
        }
    }

    public static class detailData {
        public int findViewById;
        public String detail;
    }
    public ArrayList<detailData> arrayDetailData = new ArrayList<detailData>();


    public interface OnInputSelected {
        void sendInput(String input);
    }
    public OnInputSelected mOnInputSelected;
    private TextView mActionDelete;


    private ArrayList mRoomingArray;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.frag_order_user_layout);

        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.CENTER;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;

        window.setAttributes(attributes);



        return dialog;
    }



    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_order_user_layout, container, false);

        for (int i = 0; i < arrayDetailData.size(); i++) {
            int id =  arrayDetailData.get(i).findViewById;
            String str = arrayDetailData.get(i).detail;
            TextView textView = (TextView)view.findViewById(id);
            textView.setText(str);
        }

        editText_代表者氏名_姓 =(EditText) view.findViewById(R.id.代表者氏名_姓);
        //サイト名  mRoomingArray
        TextView textView = (TextView) view.findViewById(R.id.サイト名);
        String s=mRoomingArray.toString();

        textView.setText(s.substring(1,s.length()-1));

        TextView mActionCancel = view.findViewById(R.id.action_cancel);
        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog");
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });


        TextView mActionUpdate = view.findViewById(R.id.action_select);
        mActionUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: capturing input.");


                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        return view;
    }


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    EditText editText_代表者氏名_姓;


    @Override
    public void onResume()
    {
        Log.e(TAG, "onResume: begin" );
        super.onResume();
        editText_代表者氏名_姓.post(new Runnable()
        {
            @Override
            public void run()
            {
                Log.e(TAG, "run: being" );
                InputMethodManager imm =
                        (InputMethodManager) editText_代表者氏名_姓.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive())
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }

        });
    }
}