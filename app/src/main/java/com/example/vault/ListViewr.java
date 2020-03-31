package com.example.vault;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.vault.Adapters.PasswordAdapter;
import com.example.vault.objects.Categories;
import com.example.vault.objects.Password;
import com.example.vault.objects.SharedPrefManager;
import com.example.vault.touchlisteners.RecyclerTouchListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListViewr extends AppCompatActivity {

    private SQLiteDatabase database;
    private String passwords;
    private String category;
    private PasswordAdapter PassA;
    private List<Password> PList;
    private RecyclerView mrecyler;
    private LinearLayoutManager linear;
    private static final String TAG="ERROR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_viewr);
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(ListViewr.this, register.class);
            startActivity(intent);
            finish();

        }

        Bundle extras = getIntent().getExtras();

        if(extras !=null)
        {
            passwords = extras.getString("password");
            category = extras.getString("category");
        }
        initializePass();
        mrecyler= findViewById(R.id.Pass_List);
        PList = new ArrayList<>();
        PList.clear();
        PassA = new PasswordAdapter(PList);
        linear = new LinearLayoutManager(getBaseContext());
        mrecyler.setLayoutManager(linear);
        mrecyler.setAdapter(PassA);
        showpassword();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation2);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.new_cat:
                        Intent intent = new Intent(ListViewr.this, PasswordCreation.class);
                        intent.putExtra("category",category);
                        intent.putExtra("password", passwords);
                        intent.putExtra("edit",false );
                        startActivity(intent);
                        finish();

                        break;
                    case R.id.settings:
                        Toast.makeText(ListViewr.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.exit_nav:
                        Toast.makeText(ListViewr.this, "Log Out", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        mrecyler.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mrecyler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(ListViewr.this, PasswordCreation.class);
                intent.putExtra("category",category);
                intent.putExtra("password", passwords);
                intent.putExtra("id", PList.get(position).getRowid());
                intent.putExtra("edit",true );
                startActivity(intent);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(ListViewr.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                String pass  = PList.get(position).getPassword();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("password", pass);
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);

            }
        }));


    }
    private void initializePass()
    {
        File databaseFile = getDatabasePath("passwordfile.db");
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile,passwords, null);
    }
    private void showpassword()
    {
        String query = "SELECT title, username, website, password, notes, category, ROWID FROM passwords WHERE category=?";
        Cursor cursor = database.rawQuery(query, new String[] {category});
        PList.clear();

        try {
            if (cursor.moveToFirst()) {
                do {
                    Password ct = new Password();
                    ct.setRowid(cursor.getLong(cursor.getColumnIndex("rowid")));
                    ct.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                    ct.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                    ct.setWebsite(cursor.getString(cursor.getColumnIndex("website")));
                    ct.setPassword(cursor.getString(cursor.getColumnIndex("password")));

                    PList.add(ct);
                    PassA.notifyDataSetChanged();
                    // MessA.notifyItemInserted(MessA.getItemCount()+1);
                    mrecyler.smoothScrollToPosition(PassA.getItemCount()-1);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

    }
    @Override
    public void onResume(){
        super.onResume();
        Bundle extras = getIntent().getExtras();

        if(extras !=null)
        {
            passwords = extras.getString("password");
            category = extras.getString("category");
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Bundle extras = getIntent().getExtras();

        if(extras !=null)
        {
            passwords = extras.getString("password");
            category = extras.getString("category");
        }

    }
}
