package com.tryandroid.hpquiz;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(tryandroid.com.hpquiz.R.layout.activity_main);

        if (savedInstanceState == null) {
            final FragmentManager fm = getSupportFragmentManager();
        }
    }
}
