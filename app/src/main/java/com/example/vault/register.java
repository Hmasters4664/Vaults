package com.example.vault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import net.sqlcipher.database.SQLiteDatabase;

import com.example.vault.objects.SharedPrefManager;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class register extends base {
    private EditText password;
    private TextView textViewPasswordStrengthIndiactor;
    private Button login;
    private String passwords;
    private SQLiteDatabase database;
    private SQLiteDatabase category;
    private String sh, sh2;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        password = findViewById(R.id.input_password);
        textViewPasswordStrengthIndiactor = findViewById(R.id.strength);
        login = findViewById(R.id.button);
        counter = 0;
        Bundle extras = getIntent().getExtras();
        if(!SharedPrefManager.getInstance(getApplicationContext()).isInitialized())
        {
            textViewPasswordStrengthIndiactor.setText(R.string.Enter_password_register);
        } else {
            textViewPasswordStrengthIndiactor.setText(R.string.Enter_password);
        }

        password.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // When No Password Entered
                //textViewPasswordStrengthIndiactor.setText("");
            }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            public void afterTextChanged(Editable s)
            {
                if(s.length()>=16) {
                    textViewPasswordStrengthIndiactor.setText("STRONG");
                    textViewPasswordStrengthIndiactor.setTextColor(getResources().getColor(R.color.green));
                }
                else if(s.length()>10 && s.length()<16 ) {
                    textViewPasswordStrengthIndiactor.setText("MEDIUM");
                    textViewPasswordStrengthIndiactor.setTextColor(getResources().getColor(R.color.yellow));
                }
                else if(s.length()<=10) {
                    textViewPasswordStrengthIndiactor.setText("WEAK");
                    textViewPasswordStrengthIndiactor.setTextColor(getResources().getColor(R.color.red));
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a=0;
                if(!SharedPrefManager.getInstance(getApplicationContext()).isInitialized())
                {
                    InitializeCategories();
                    InitializeSQLCipher();
                    SharedPrefManager.getInstance(getApplicationContext()).Initialize();
                    try {
                        sh= hash(password.getText().toString().trim());
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    SharedPrefManager.getInstance(getApplicationContext()).accessed(sh);
                }

                login();

            }
        });

    }

    public String hash(String input) throws NoSuchAlgorithmException {
        Hasher hasher = Hashing.sha256().newHasher();
        return hasher.putString(input, StandardCharsets.UTF_8).hash()
                .toString();
    }

    private void InitializeSQLCipher() {
        File databaseFile = getDatabasePath("passwordfile.db");
        databaseFile.mkdirs();
        databaseFile.delete();
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile,password.getText().toString().trim(), null);
        database.execSQL("create table passwords(title, username, website, password, notes, category)");
    }
    private void InitializeCategories() {
        File databaseFile = getDatabasePath("categories.db");
        databaseFile.mkdirs();
        databaseFile.delete();
        category = SQLiteDatabase.openOrCreateDatabase(databaseFile, "#!@#$%^&()0987654321", null);
        category.execSQL("create table categories(Name, Colour)");
    }
    private void login()
    {
        Hasher hasher = Hashing.sha256().newHasher();
        try {
            sh2= hash(password.getText().toString().trim());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        sh=SharedPrefManager.getInstance(getApplicationContext()).getHash();

        if(sh.equals(sh2))
        {
            SharedPrefManager.getInstance(getApplicationContext()).LogIn();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.putExtra("password", password.getText().toString().trim());
            startActivity(intent);
            finish();
        } else {
            if(counter<3) {
                Toast.makeText(register.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                counter++;
            }else{
                Toast.makeText(register.this, "Deleting database and closing", Toast.LENGTH_LONG).show();
                InitializeSQLCipher();
                InitializeCategories();
                SharedPrefManager.getInstance(getApplicationContext()).deInitialize();
                finish();
            }
        }

    }


}
