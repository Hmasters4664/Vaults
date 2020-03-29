package com.example.vault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.util.Random;

public class PasswordCreation extends AppCompatActivity {

    private SQLiteDatabase database;
    private String passwords;
    private String category;
    private Long id;
    private CheckBox lower,upper,special,num;
    private EditText title,username,website,length,password;
    private Button save,generate;
    private int len;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_creation);
        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            passwords = extras.getString("password");
            category = extras.getString("category");
            id = extras.getLong("id");
        }
        lower =findViewById(R.id.lowercase);
        upper = findViewById(R.id.uppercase);
        special = findViewById(R.id.special);
        title = findViewById(R.id.tile);
        username = findViewById(R.id.username);
        website = findViewById(R.id.website);
        length = findViewById(R.id.length);
        password = findViewById(R.id.input_password);
        save = findViewById(R.id.save);
        generate = findViewById(R.id.gen);
        initializePass();
        if (id!=null)
        {

            select();
        }

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len;
                try {
                    len = Integer.parseInt(length.getText().toString());
                } catch(NumberFormatException nfe) {
                    len=16;
                }

                String Pass = GetPassword(len);
                password.setText(Pass);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (check())
               {
                   if(id!=null)
                   {
                       edit();
                   }else {
                       save();
                   }
                   Intent intent = new Intent(getBaseContext(), ListViewr.class);
                   intent.putExtra("category",category);
                   intent.putExtra("password", passwords);
                   startActivity(intent);
                   finish();

               }else
               {
                   Toast.makeText(PasswordCreation.this, "Fill in title", Toast.LENGTH_LONG).show();
               }

            }
        });




    }

    public String GetPassword(int length){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$&!".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();

        Random rand = new Random();

        for(int i = 0; i < length; i++){
            char c = chars[rand.nextInt(chars.length)];
            stringBuilder.append(c);
        }

        return stringBuilder.toString();
    }

    private void initializePass()
    {
        File databaseFile = getDatabasePath("passwordfile.db");
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile,passwords, null);
    }

    private void save()
    {
        String t=title.getText().toString();
        String u=username.getText().toString();
        String w=website.getText().toString();
        String p=password.getText().toString();
        String n="";
        database.execSQL("insert into passwords(title, username, website, password, notes, " +
                "category) values(?, ?, ?, ?, ?, ?)",
                new Object[]{t,u,w,p,n,category});
    }

    private boolean check()
    {
        if(title.getText().toString().isEmpty()) {
            return false;
        } else{
            return true;
        }
    }

    private void edit()
    {
        String t=title.getText().toString();
        String u=username.getText().toString();
        String w=website.getText().toString();
        String p=password.getText().toString();
        String n="";
        ContentValues data = new ContentValues();
        data.put("title",t);
        data.put("username",u);
        data.put("website",w);
        data.put("password",p);
        data.put("notes",n);
        database.update("passwords",data,"ROWID="+Long.toString(id),null);


    }
    private void select(){
    String query = "SELECT title, username, website, password, notes, category, ROWID FROM passwords WHERE ROWID=?";
    Cursor cursor = database.rawQuery(query, new Long[] {id});
        cursor.moveToFirst();
        title.setText(cursor.getString(cursor.getColumnIndex("title")));
        username.setText(cursor.getString(cursor.getColumnIndex("username")));
        website.setText(cursor.getString(cursor.getColumnIndex("website")));
        password.setText(cursor.getString(cursor.getColumnIndex("password")));
    }

}
