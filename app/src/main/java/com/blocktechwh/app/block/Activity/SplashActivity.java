package com.blocktechwh.app.block.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.R;

import org.afinal.simplecache.ACache;

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
            if("".equals(App.token)){
                Intent intent = new Intent(getApplication(),LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getApplication(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
                //startActivity(new Intent(getApplication(),MainActivity.class));
            }
            SplashActivity.this.finish();
        }
    }

}

