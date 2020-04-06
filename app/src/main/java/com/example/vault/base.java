package com.example.vault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.vault.objects.SharedPrefManager;

import java.util.Date;

public abstract class base extends AppCompatActivity {
private long lastActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        String themeName = pref.getString("theme", "AppTheme");
        if (themeName.equals("Night")) {
            setTheme(R.style.Night);
        } else if (themeName.equals("Orange")) {
            setTheme(R.style.Orange);
        }else if (themeName.equals("Pink")) {
            setTheme(R.style.Pink);
        }else if (themeName.equals("AppTheme")) {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        lastActivity = new Date().getTime();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        lastActivity = new Date().getTime();
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onResume() {
        long now = new Date().getTime();
        if (now - lastActivity > 300000) {
            SharedPrefManager.getInstance(this).LogOut();
            Intent i = new Intent(getBaseContext(), register.class);
            startActivity(i);
        }
        super.onResume();
    }

    public void out()
    {
        SharedPrefManager.getInstance(this).LogOut();
    }
}
