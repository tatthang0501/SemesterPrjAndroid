package com.example.semesterprojectnguyentatthangb17dccn563.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.semesterprojectnguyentatthangb17dccn563.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private EditText rgEmail, rgPassword, rgCfPassword;
    private Button btnRegister, btnBack;
    protected FirebaseAuth authentication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        init();
        authentication = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!rgEmail.getText().toString().contains("@")){
                    rgEmail.setError("Email's not true");
                    return;
                }
                if(rgEmail.getText().toString().length()==0){
                    rgEmail.setError("Email is blank");
                    return;
                }
                if(rgPassword.getText().toString().length() < 8){
                    rgPassword.setError("Password must be more than 8 characters");
                    return;
                }
                if(rgCfPassword.getText().toString() != rgPassword.getText().toString()){
                    rgCfPassword.setError("Confirm password not match");
                    return;
                }
                String e = rgEmail.getText().toString();
                String p = rgPassword.getText().toString();
                authentication.createUserWithEmailAndPassword(e,p).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(RegisterActivity.this, "Register successful, you can login now", Toast.LENGTH_SHORT).show();
                        authentication.signOut();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void init(){
        rgEmail = findViewById(R.id.edtRegisterEmail);
        rgPassword = findViewById(R.id.edtRegisterPassword);
        rgCfPassword = findViewById(R.id.edtRegisterCfPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBackToLogin);
    }
}