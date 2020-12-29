package com.qty.ptsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.qty.pt.QTPerfomanceTesting;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        QTPerfomanceTesting.getInstance().start("str");
        for (int i = 0; i < 1000; i++) {
            String test = "Test String " + i;
            System.out.println(test);
            if ((i + 1) % 100 == 0) {
                Log.d(MainActivity.class.getSimpleName(), "onResume=>" + (i + 1) + " use time: " + QTPerfomanceTesting.getInstance().countDown("str", false));
            }
        }
        Log.d(MainActivity.class.getSimpleName(), "onResume=>end use time: " + QTPerfomanceTesting.getInstance().end("str", true));
    }
}