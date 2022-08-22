package com.example.mytaxistart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity {
    Button driverBth, customerBth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        driverBth = (Button)findViewById(R.id.driverBth);
        customerBth = (Button)findViewById(R.id.customerBth);

        driverBth.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent driverIntent = new Intent(MainActivity2.this, DriverRegLogActivity3.class);
                startActivity(driverIntent);
            }
        });

        customerBth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent customerIntent = new Intent(MainActivity2.this, CustomerRegLogActivity3.class);
                startActivity(customerIntent);
            }
        });


    }
}