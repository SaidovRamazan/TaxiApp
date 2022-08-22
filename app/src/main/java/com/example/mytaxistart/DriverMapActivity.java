package com.example.mytaxistart;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.LocationRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Geo;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.location.FilteringMode;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import java.util.Objects;

public class DriverMapActivity extends AppCompatActivity implements UserLocationObjectListener{

   // private final String MAPKIT_API_KEY = "6c76e465-98ed-4d86-97a5-0d750c46d7fb";
    private MapView mapview;
    private UserLocationLayer userLocationLayer;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private FirebaseAuth mAuth;
    private Boolean currentLogOutDriverStatus;
    Button LogoutDriverBth;
    Button SettingsDriverBth;

    FirebaseUser currentUser;






    @Override
    protected void onCreate(Bundle savedInstanceState){
            MapKitFactory.setApiKey("6c76e465-98ed-4d86-97a5-0d750c46d7fb");
            MapKitFactory.initialize(this);

            setContentView(R.layout.activity_driver_map);


            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();
            String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            LogoutDriverBth = (Button)findViewById(R.id.Exit);
            SettingsDriverBth = (Button)findViewById(R.id.driverSettings);
            System.out.println(LogoutDriverBth +"BATOM!!!!!!!!!!");
            System.out.println(SettingsDriverBth);




            // Укажите имя Activity вместо map.

            super.onCreate(savedInstanceState);
            mapview = (MapView)findViewById(R.id.mapview);
            mapview.getMap().move(new CameraPosition(new Point(0, 0), 14, 0.0f, 0.0f));

            MapKit mapKit = MapKitFactory.getInstance();
            userLocationLayer = mapKit.createUserLocationLayer(mapview.getMapWindow());
            userLocationLayer.setVisible(true);
            userLocationLayer.setHeadingEnabled(true);



            userLocationLayer.setObjectListener((UserLocationObjectListener) this);
            requestLocationPermission();
            mapKit.createLocationManager().subscribeForLocationUpdates(0,0,0,true, FilteringMode.ON,new LocationListener() {
            @Override
            public void onLocationUpdated(@NonNull com.yandex.mapkit.location.Location location) {
                Log.d("TagCheck","LocationUpdated"+location.getPosition().getLongitude());
                Log.d("TagCheck","LocationUpdated"+location.getPosition().getLatitude());

                mapview.getMap().move(new CameraPosition(new Point(0, 0), 14, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH,1),
                null);


                DatabaseReference DriverFreeRef = FirebaseDatabase.getInstance().getReference().child("Driver Free");
                GeoFire geoFire = new GeoFire(DriverFreeRef);
                geoFire.setLocation(userID,new GeoLocation(location.getPosition().getLatitude(),location.getPosition().getLongitude()));

            }
            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {
            }
        });


        LogoutDriverBth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLogOutDriverStatus = true;
                mAuth.signOut();

                LogoutDriver();
                DisconnectDriver();


            }
        });

    }
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                "android.permission.ACCESS_FINE_LOCATION")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{"android.permission.ACCESS_FINE_LOCATION"},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }





    @Override
    protected void onStop() {
        super.onStop();
        MapKitFactory.getInstance().onStop();
        mapview.onStop();
        if (!currentLogOutDriverStatus) {
            DisconnectDriver();
        }
    }
    private void DisconnectDriver(){
            DatabaseReference DriverFreeRef = FirebaseDatabase.getInstance().getReference().child("Driver Free");
            GeoFire geoFire = new GeoFire(DriverFreeRef);
            geoFire.removeLocation(currentUser.getUid());

    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapview.onStart();
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        userLocationLayer.setAnchor(
                new PointF((float)(mapview.getWidth() * 0.5), (float)(mapview.getHeight() * 0.5)),
                new PointF((float)(mapview.getWidth() * 0.5), (float)(mapview.getHeight() * 0.83)));

        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                this, R.drawable.user_arrow));

        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();

        pinIcon.setIcon(
                "icon",
                ImageProvider.fromResource(this, R.drawable.icon),
                new IconStyle().setAnchor(new PointF(0f, 0f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(0f)
                        .setScale(1f)
        );

        pinIcon.setIcon(
                "pin",
                ImageProvider.fromResource(this, R.drawable.search_result),
                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(1f)
                        .setScale(0.5f)
        );

        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);
    }

    @Override
    public void onObjectRemoved(UserLocationView view) {
    }

    @Override
    public void onObjectUpdated(UserLocationView view, ObjectEvent event) {
    }





    private void LogoutDriver(){
        Intent exitIntent = new Intent(DriverMapActivity.this,MainActivity2.class);
        startActivity(exitIntent);
        finish();
    }


}
