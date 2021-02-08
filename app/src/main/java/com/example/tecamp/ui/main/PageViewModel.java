package com.example.tecamp.ui.main;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {
    private static final String TAG = "PageViewModel";

    private static final String[] TAB_TITLES_STRING = new String[]{
            "予約一覧",
            "カレンダー",
            "予約検索",
            "電話受付"};

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            Log.e(TAG, "apply: input:"+input );
            return TAB_TITLES_STRING[input-1]+"　内容";
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}