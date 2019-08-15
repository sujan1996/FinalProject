package com.example.projectfinal;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationProviderClient;
    SupportMapFragment mapFragment;
    private LocationCallback locationCallback;
    private Marker currentUser;
    double lattd;
    double longtd;

    private Button mLogout, mSettings;
    private Button apreq, anreq, bpreq, bnreq, abpreq, abnreq, opreq, onreq, onclear;

    String userDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        buildLocationRequest();
                        buildLocationCallback();
                        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(UserMapActivity.this);

                        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        mapFragment.getMapAsync(UserMapActivity.this);


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(UserMapActivity.this, "Enable Location Permission", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mLogout = (Button) findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserMapActivity.this, FirstActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        Intent intent = getIntent();
        final String userClass = intent.getStringExtra("bloodType");

        mSettings = (Button) findViewById(R.id.settings);
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMapActivity.this, UserSettingActivity.class);
                intent.putExtra("userClass", userClass);
                startActivity(intent);
                return;

            }
        });
    }


    private void buildLocationCallback() {


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (mMap != null) {
                    if (currentUser != null) currentUser.remove();
                    currentUser = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(locationResult.getLastLocation().getLatitude(),
                                    locationResult.getLastLocation().getLongitude()))
                            .title("YOU"));


                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUser.getPosition(), 12.0f));

                    //save real time location in database
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Intent intent = getIntent();
                    String bloodType = intent.getStringExtra("bloodType");

                    lattd = locationResult.getLastLocation().getLatitude();
                    longtd = locationResult.getLastLocation().getLongitude();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("UserLocation").child(bloodType);

                    GeoFire geoFire = new GeoFire(ref);

                    geoFire.setLocation(userId, new GeoLocation(lattd, longtd), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            if (error != null) {
                                System.err.println("There was an error saving the location to GeoFire: " + error);
                            } else {
                                System.out.println("Location saved on server successfully!");
                            }
                        }
                    });

                }


                apreq = (Button) findViewById(R.id.apreq);
                apreq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!getUserAroundStarted)
                            getUsersAround("A+");
                    }

                });
                anreq = (Button) findViewById(R.id.anreq);

                anreq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!getUserAroundStarted)
                            getUsersAround("A-");
                    }
                });
                bpreq = (Button) findViewById(R.id.bpreq);

                bpreq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!getUserAroundStarted)
                            getUsersAround("B+");
                    }
                });

                bnreq = (Button) findViewById(R.id.bnreq);

                bnreq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!getUserAroundStarted)
                            getUsersAround("B-");
                    }
                });
                abpreq = (Button) findViewById(R.id.abpreq);

                abpreq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!getUserAroundStarted)
                            getUsersAround("AB+");
                    }
                });
                abnreq = (Button) findViewById(R.id.abnreq);

                abnreq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!getUserAroundStarted)
                            getUsersAround("AB-");
                    }
                });
                opreq = (Button) findViewById(R.id.opreq);

                opreq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!getUserAroundStarted)
                            getUsersAround("O+");
                    }
                });
                onreq = (Button) findViewById(R.id.onreq);

                onreq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!getUserAroundStarted)
                            getUsersAround("O-");
                    }
                });

            }


        };
    }

    private void buildLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setSmallestDisplacement(10f);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mFusedLocationProviderClient != null)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    protected void onStop() {
        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    //checking whether to search user or not
    boolean getUserAroundStarted = false;

    //for which blood group user to search


    List<Marker> markerList = new ArrayList<Marker>();

    private void getUsersAround(final String s) {
        getUserAroundStarted = true;
        DatabaseReference usersLocation = FirebaseDatabase.getInstance().getReference().child("UserLocation").child(s);
        GeoFire geoFire = new GeoFire(usersLocation);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(lattd, longtd), 10000);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, final GeoLocation location) {
                for (Marker markerIt : markerList) {
                    if (markerIt.getTag().equals(key))
                        return;
                }

                DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Users").child(s).child(key);
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userDetails = String.valueOf(dataSnapshot.getValue());
                        LatLng userLocation = new LatLng(location.latitude, location.longitude);


                        Marker mUserMarker = mMap.addMarker(new MarkerOptions().position(userLocation).
                                title(userDetails));
                        mUserMarker.setTag(key);
                        markerList.add(mUserMarker);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                for (Marker markerIt : markerList) {
                    if (markerIt.getTag().equals(key)) {
                        markerIt.setPosition(new LatLng(lattd, longtd));
                    }
                }

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }


}
