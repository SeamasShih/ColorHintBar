package com.seamas.colorhintbar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.seamas.colorhintbarlibrary.ColorHintBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ColorHintBar chb = findViewById(R.id.chb);
        chb.setItemAmount(3);
        chb.setBackgroundHintColor(Color.BLUE);
        chb.setOnClickListener(v -> chb.setSite((chb.getSite() + 1) % 3));
    }
}
