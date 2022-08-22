package com.example.mytaxistart;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread = new Thread()
        {
            @Override
            public  void run(){
                super.run();
                {
                    try {
                        sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        PermissionListener permissionlistener = new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                                startActivity(intent);                            }

                            @Override
                            public void onPermissionDenied(List<String> deniedPermissions) {
                                Toast.makeText(MainActivity.this, "Доступ запрещен\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                            }


                        };
                        TedPermission.create()
                                .setPermissionListener(permissionlistener)
                                .setDeniedMessage("Для использования приложения необходимо предоставить доступ к местоположению\n\nPlease turn on permissions at [Setting] > [Permission]")
                                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                                .check();


                    }
                }
            }





        };
        thread.start();
    }
    @Override
    protected void onPause(){
        super.onPause();

       finish();

    }
}