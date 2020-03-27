package com.example.vault;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.util.Random;

public class PasswordCreation extends AppCompatActivity {

    private SQLiteDatabase database;
    private String passwords;
    private String category;
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

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pass = GetPassword(16);
                password.setText(Pass);
            }
        });




    }

    public String GetPassword(int length){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
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
        database.execSQL("insert into categories(title, username, website, password, notes, " +
                "category) values(?, ?, ?, ?, ?, ?)",
                new Object[]{t,u,w,p,n,category});
    }

}
