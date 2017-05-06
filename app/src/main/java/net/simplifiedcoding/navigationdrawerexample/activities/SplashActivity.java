package net.simplifiedcoding.navigationdrawerexample.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import net.simplifiedcoding.navigationdrawerexample.R;

public class SplashActivity extends Activity {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 12345;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                if (!sharedpreferences.getString("isValidUser", "").equals("1")) {
                    Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                    //intent = new Intent(SplashActivity.this, AppDescriptionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("name", sharedpreferences.getString("name", ""));
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }
}