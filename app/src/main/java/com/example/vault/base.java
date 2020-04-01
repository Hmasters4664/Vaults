package com.example.vault;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vault.objects.SharedPrefManager;

import java.util.Date;

public abstract class base extends AppCompatActivity {
private long lastActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
}
