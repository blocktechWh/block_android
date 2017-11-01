package com.blocktechwh.app.block.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.blocktechwh.app.block.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }
    public void setTitle(CharSequence title) {
        ((TextView)findViewById(R.id.titlebar_title_tv)).setText(title);
    }


}

