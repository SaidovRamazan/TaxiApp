package com.example.mytaxistart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DriverRegLogActivity3 extends AppCompatActivity {
    TextView driverStatus, question;
    Button signInBth, signUpBth;
    EditText emailEt, passwordET;

    FirebaseAuth mAuth;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_reg_log3);

        driverStatus = (TextView) findViewById(R.id.statusDriver);
        question = (TextView) findViewById(R.id.accountCreate);
        signInBth = (Button) findViewById(R.id.signInDriver);
        signUpBth = (Button) findViewById(R.id.signUpDriver);
        emailEt = (EditText) findViewById(R.id.driverEmail);
        passwordET = (EditText) findViewById(R.id.driverPassword);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        signUpBth.setVisibility(View.INVISIBLE);
        signUpBth.setEnabled(false);

        question.setOnClickListener((view) -> {
            signInBth.setVisibility(View.INVISIBLE);
            question.setVisibility(View.INVISIBLE);
            signUpBth.setVisibility(View.VISIBLE);
            signUpBth.setEnabled(true);
            driverStatus.setText("Регистрация для водителей");

        });
        signUpBth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailEt.getText().toString();
                String password = passwordET.getText().toString();
                RegisterDriver(email, password);
            }
        });
        signInBth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString();
                String password = passwordET.getText().toString();
                SignInDriver(email, password);
            }
        });
    }


    private void SignInDriver(String email, String password) {
        loadingBar.setTitle("Вход водителя");
        loadingBar.setMessage("Пожалуйста дождитесь загрузки");
        loadingBar.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(DriverRegLogActivity3.this, "Успешний вход", Toast.LENGTH_SHORT).show();
                    Intent driverIntent = new Intent(DriverRegLogActivity3.this,DriverMapActivity.class);
                    startActivity(driverIntent);

                } else {
                    Toast.makeText(DriverRegLogActivity3.this, "Ошибка", Toast.LENGTH_SHORT).show();
                }
                loadingBar.dismiss();
            }
        });
    }


        private void RegisterDriver (String email, String password){
            loadingBar.setTitle("Регистрация водителя");
            loadingBar.setMessage("Пожалуйста дождитесь загрузки");
            loadingBar.show();


            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(DriverRegLogActivity3.this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();


                        Intent driverIntent = new Intent(DriverRegLogActivity3.this,DriverMapActivity.class);
                        startActivity(driverIntent);

                    } else {
                        Toast.makeText(DriverRegLogActivity3.this, "Ошибка", Toast.LENGTH_SHORT).show();
                    }
                    loadingBar.dismiss();
                }
            });
        }
    }
