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

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import net.sqlcipher.database.SQLiteDatabase;

import com.example.vault.objects.SharedPrefManager;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class register extends AppCompatActivity {
    private EditText password;
    private TextView textViewPasswordStrengthIndiactor;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        password = findViewById(R.id.input_password);
        textViewPasswordStrengthIndiactor = findViewById(R.id.strength);
        login = findViewById(R.id.button);
        Bundle extras = getIntent().getExtras();

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
                if (SharedPrefManager.getInstance(getBaseContext()).isLoggedIn()) {
                    if(SharedPrefManager.getInstance(getBaseContext()).getHash().contentEquals(password.getText().toString())) {
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        i.putExtra("password", password.getText().toString());
                    }
                } else {
                    ///create db


                    try {
                        SharedPrefManager.getInstance(getBaseContext()).accessed(hash(password.getText().toString()));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    i.putExtra("password", password.getText().toString());
                    startActivity(i);
                    finish();


                }

            }
        });

    }

    public String hash(String input) throws NoSuchAlgorithmException {
        Hasher hasher = Hashing.sha256().newHasher();
        return hasher.putString(input, StandardCharsets.UTF_8)
                .toString();
    }

}
