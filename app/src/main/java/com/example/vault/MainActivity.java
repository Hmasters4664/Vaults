package com.example.vault;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.vault.Adapters.CategoryAdapter;
import com.example.vault.Adapters.CustomSpinnerAdapter;
import com.example.vault.objects.Categories;
import com.example.vault.objects.GridSpacingItemDecoration;
import com.example.vault.objects.SharedPrefManager;
import com.example.vault.touchlisteners.RecyclerTouchListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends base {
    private String passwords;
    private SQLiteDatabase database;
    private SQLiteDatabase category;
    private static final String TAG="ERROR";
    private List<Categories> cinfo;
    private RecyclerView view;
    private CategoryAdapter CatA;
    private View popupInputDialogView;
    private GridLayoutManager gridLayoutManager;
    String[] mNamesArray;
    String[] picsArray;
    private ColorGenerator generator = ColorGenerator.MATERIAL;
    CustomSpinnerAdapter spin;
    private EditText catagoryName;
    private Spinner spinner;
    private Button saveUserDataButton, cancelUserDataButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cinfo= new ArrayList<>();
        cinfo.clear();
        int spanCount = 3; // 3 columns
        int spacing = 50; // 50px
        boolean includeEdge = false;

        SQLiteDatabase.loadLibs(this);
        view = (RecyclerView) findViewById(R.id.rec);
        CatA = new CategoryAdapter(cinfo);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(),spanCount);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        view.setLayoutManager(gridLayoutManager);
        view.addItemDecoration(new GridSpacingItemDecoration(getApplicationContext(),R.dimen.default_gap));
        mNamesArray = getResources().getStringArray(R.array.names);
        picsArray = getResources().getStringArray(R.array.colours);
        view.setAdapter(CatA);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.new_cat:
                        Toast.makeText(MainActivity.this, "New category", Toast.LENGTH_SHORT).show();
                        dialogbuilder();
                        break;
                    case R.id.settings:
                        Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getBaseContext(), Settings.class);
                        startActivity(intent);
                        break;
                    case R.id.exit_nav:
                        Toast.makeText(MainActivity.this, "Log Out", Toast.LENGTH_SHORT).show();
                        out();
                        Intent i = new Intent(getBaseContext(), register.class);
                        startActivity(i);
                        finish();
                        break;
                }
                return true;
            }
        });







        view.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), view, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String category=cinfo.get(position).getName();
                Intent i = new Intent(getBaseContext(), ListViewr.class);
                i.putExtra("category",category);
                i.putExtra("password", passwords);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            passwords = extras.getString("password");
            initializeCats();
        }else {
            Intent intent = new Intent(MainActivity.this, register.class);
            startActivity(intent);
            finish();

        }



        if(category!=null)
        showCategories();



    }

    private void InitializeSQLCipher() {
        File databaseFile = getDatabasePath("passwordfile.db");
        databaseFile.mkdirs();
        databaseFile.delete();
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile,passwords, null);
        database.execSQL("create table passwords(title, username, website, password, notes, category)");
    }
    private void InitializeCategories()
    {
        File databaseFile = getDatabasePath("categories.db");
        databaseFile.mkdirs();
        databaseFile.delete();
        category = SQLiteDatabase.openOrCreateDatabase(databaseFile,"#!@#$%^&()0987654321", null);
        category.execSQL("create table categories(Name, Colour)");

    }

    private void initializeCats()
    {
        File databaseFile = getDatabasePath("categories.db");
        category = SQLiteDatabase.openOrCreateDatabase(databaseFile,"#!@#$%^&()0987654321", null);

    }

    private void initializePass()
    {
        File databaseFile = getDatabasePath("passwordfile.db");
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile,passwords, null);
    }

    private void showCategories()
    {
        String query = "SELECT * FROM categories";
        Cursor cursor = category.rawQuery(query, null);
        cinfo.clear();

        try {
            if (cursor.moveToFirst()) {
                do {
                    Categories ct = new Categories();
                    ct.setName(cursor.getString(cursor.getColumnIndex("Name")));
                    ct.setColour(cursor.getInt(cursor.getColumnIndex("Colour")));

                    cinfo.add(ct);
                    CatA.notifyDataSetChanged();
                    // MessA.notifyItemInserted(MessA.getItemCount()+1);
                    view.smoothScrollToPosition(CatA.getItemCount()-1);

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

    private void dialogbuilder()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        // Set title, icon, can not cancel properties.
        alertDialogBuilder.setTitle("Category Creation Dialog");
        alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
        alertDialogBuilder.setCancelable(false);
        initPopupViewControls();
        alertDialogBuilder.setView(popupInputDialogView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        saveUserDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get user data from popup dialog editeext.
                String name = catagoryName.getText().toString();
                int colour = generator.getColor(name);
                category.execSQL("insert into categories(Name, Colour) values(?, ?)", new Object[]{name,colour});
                // Create data for the listview.

                showCategories();
                alertDialog.cancel();
            }
        });

        cancelUserDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

    }

    private void initPopupViewControls()
    {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        popupInputDialogView = layoutInflater.inflate(R.layout.dialog, null);
        catagoryName = (EditText) popupInputDialogView.findViewById(R.id.userName);
        saveUserDataButton = popupInputDialogView.findViewById(R.id.button_save_user_data);
        cancelUserDataButton = popupInputDialogView.findViewById(R.id.button_cancel_user_data);
    }


    public void out()
    {
        SharedPrefManager.getInstance(this).LogOut();
    }

}
