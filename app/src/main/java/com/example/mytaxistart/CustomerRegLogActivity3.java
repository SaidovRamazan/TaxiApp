package com.example.mytaxistart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerRegLogActivity3 extends AppCompatActivity {
    Button signInBth, signUpBth;
    EditText emailET, passwordET;
    TextView customerStatus, question;
    FirebaseAuth mAuth;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reg_log3);

        customerStatus = (TextView) findViewById(R.id.statusCustomer);
        question = (TextView)  findViewById(R.id.accountCreateCustomer);
        signInBth = (Button) findViewById(R.id.signInCustomer);
        signUpBth = (Button) findViewById(R.id.signUpCustomer);
        emailET = (EditText) findViewById(R.id.customerEmail);
        passwordET = (EditText) findViewById(R.id.customerPassword);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);


        signUpBth.setVisibility(View.INVISIBLE);
        signUpBth.setEnabled(false);

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInBth.setVisibility(View.INVISIBLE);
                question.setVisibility(View.INVISIBLE);
                signUpBth.setVisibility(View.VISIBLE);
                signUpBth.setEnabled(true);
                customerStatus.setText("Регистрация для клиентов");

            }
        });
        signUpBth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                RegisterCustomer(email, password);
            }
        });
        signInBth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                SignInCustomer(email, password);
            }
        });
    }

    private void RegisterCustomer(String email, String password) {
        loadingBar.setTitle("Регистрация водителя");
        loadingBar.setMessage("Пожалуйста дождитесь загрузки");
        loadingBar.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CustomerRegLogActivity3.this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CustomerRegLogActivity3.this, "Ошибка", Toast.LENGTH_SHORT).show();
                }
                loadingBar.dismiss();
            }

        });
    }
    private void SignInCustomer(String email, String password) {
        loadingBar.setTitle("Вход");
        loadingBar.setMessage("Пожалуйста дождитесь загрузки");
        loadingBar.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CustomerRegLogActivity3.this, "Успешний вход", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CustomerRegLogActivity3.this, "Ошибка", Toast.LENGTH_SHORT).show();
                }
                loadingBar.dismiss();
            }
        });
    }


}
