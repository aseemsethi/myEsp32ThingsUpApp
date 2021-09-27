package com.aseemsethi.bookappoauth.ui.main;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {
    final String TAG = "BookAppOauth: PageView";

    private static MutableLiveData<String> loggedin = new MutableLiveData<>();
    private static MutableLiveData<String> status = new MutableLiveData<>();

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });
    public void setIndex(int index) {
        mIndex.setValue(index);
    }
    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getLoggedin() {
        return loggedin;
    }
    public void setLoggedin(String val) {
        Log.d(TAG, "Page View Model: Loggedin:" + val);
        loggedin.setValue(val);}

    public LiveData<String> getStatus() {
        return status;
    }
    public void setStatus(String val) {
        Log.d(TAG, "Page View Model:" + val);
        status.setValue(val);}
}