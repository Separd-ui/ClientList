package com.example.clientlist;

import android.app.Activity;
import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LogoActivity extends Activity {
    private Animation anim_button,anim_img;
    private ImageView imageView;
    private Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
        imageView=findViewById(R.id.img);
        button=findViewById(R.id.but);
        anim_button=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_img);
        anim_img=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_button);
        button.startAnimation(anim_button);
        imageView.startAnimation(anim_img);
        //auto_start();

    }

    public void onClickStart(View view) {
        Intent i=new Intent(LogoActivity.this,MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
    private void auto_start()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent i=new Intent(LogoActivity.this,MainActivity.class);
                startActivity(i);
            }
        }).start();

    }
}
