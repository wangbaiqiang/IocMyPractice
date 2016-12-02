package com.wbq.iocmypractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.wbq.iocmypractice.annotation.ViewInject;

public class MainActivity extends AppCompatActivity {
    @ViewInject(R.id.sure)
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
