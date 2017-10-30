package com.blocktechwh.app.block.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import org.afinal.simplecache.ACache;

import com.blocktechwh.app.block.R;

public class SplashActivity extends AppCompatActivity {

    private ACache mCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 2000);
        mCache = ACache.get(this);
    }

    class splashhandler implements Runnable{
        public void run() {
            String value = mCache.getAsString("token");
            if(value == null){
                startActivity(new Intent(getApplication(),MainActivity.class));
//                startActivity(new Intent(getApplication(),LoginActivity.class));
            }else{
                startActivity(new Intent(getApplication(),MainActivity.class));
            }
            SplashActivity.this.finish();
        }
    }

}

