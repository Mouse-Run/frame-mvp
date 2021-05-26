package com.mouse.androidmvpframe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mouse.modulemvp.mvp.XActivity;

public class MainActivity extends XActivity {


    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public Object newP() {
        return null;
    }
}
