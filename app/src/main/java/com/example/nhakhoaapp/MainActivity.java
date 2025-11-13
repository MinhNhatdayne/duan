package com.example.nhakhoaapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private Button btnSignIn;
    private TextView txtForgot, txtCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        edtEmail = findViewById(R.id.edtEmail);
//        edtPassword = findViewById(R.id.edtPassword);
//        btnSignIn = findViewById(R.id.btnSignIn);
//        txtForgot = findViewById(R.id.txtForgot);
//        txtCreate = findViewById(R.id.txtCreate);
    }
}