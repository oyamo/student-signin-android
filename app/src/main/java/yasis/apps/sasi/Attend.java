package yasis.apps.sasi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import yasis.apps.sasi.OyamNet.p2pNet;

import java.util.Calendar;
import java.util.Date;

public class Attend extends AppCompatActivity {
    LocationManager locationManager;
    DatabaseReference mReference;
    ProgressBar progressBar,progressLocation;
    LinearLayout errorPane;
    TextView venue, unitName;
    Button signInBtn ;
    String Unit;
    p2pNet oyamnet;
    double longitude, Latitude,alt,long0,long1,alt0,alt1,lat0,lat1;
    FirebaseAuth mAuth;
    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel channel;
    WifiP2pManager manager;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);
        //Register the Intents




        //Register the UI IDs
        progressBar = findViewById(R.id.progressAttend);
        errorPane = findViewById(R.id.errorPane);
        venue = findViewById(R.id.venue);
        progressLocation = findViewById(R.id.progressLocation);
        signInBtn = findViewById(R.id.signInbtn);
        signInBtn.setEnabled(false);
        venue.setText("----");
        unitName = findViewById(R.id.unitName);
        unitName.setText("----");
        progressBar.setVisibility(View.GONE);
        //Initialise the location manager and DatabaseReference
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mReference = FirebaseDatabase.getInstance().getReference("timetable").child("MMU");
        mAuth = FirebaseAuth.getInstance();
        //Start Listening to change in location
        getLocation();
        //Fetch Database Details from the  remote server
        getLesson();
        oyamnet = new p2pNet(this,intentFilter);


    }
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            alt = location.getAltitude();
            Latitude = location.getLatitude();
            longitude = location.getLongitude();
            progressLocation.setVisibility(View.GONE);
            if(isInClass()) signInBtn.setEnabled(true);
            //((TextView)findViewById(R.id.coord)).setText("lat:"+x+", long:"+y+", alt:"+alt);
        }
        //-1.3799712248146534 lat
        //1758 alt
        //36.76577850244939 long
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    public void getPerm(){
        String[] permisions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(this,permisions,200);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(hasPermision()){
//            startActivity(new Intent(MainActivity.this,Login.class));
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Location permission needed");
            builder.setMessage("Please allow this app to access your location");
            builder.show();
            builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            getPerm();
        }
    }

    public boolean hasPermision(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }

    }
    public void getLesson(){
       progressBar.setVisibility(View.VISIBLE);
        Date currentDate = Calendar.getInstance().getTime();
        String[] days = new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        int dayOfWeekInt = currentDate.getDay();
        final String dayOfWeek = days[dayOfWeekInt-1];
//        DatabaseReference unit = mReference.child(dayOfWeek).child("Unit");
//        DatabaseReference venue = mReference.child(dayOfWeek).child("Venue");
//        DatabaseReference lat = mReference.child(dayOfWeek).child("Coords").child("Lat");
//        DatabaseReference longitude = mReference.child(dayOfWeek).child("Coords").child("Long");
//        DatabaseReference alt = mReference.child(dayOfWeek).child("Coords").child("Alt");
        mReference.child(dayOfWeek).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                errorPane.setVisibility(View.GONE);

                String unit = dataSnapshot.child("Unit").getValue(String.class);
                Unit = unit;
                String v = dataSnapshot.child("Venue").getValue(String.class);
                lat0 = dataSnapshot.child("Coords").child("Lat0").getValue(Double.class);
                lat1 = dataSnapshot.child("Coords").child("Lat1").getValue(Double.class);
//                String lat = lat_.toString();
                long0 = dataSnapshot.child("Coords").child("Long0").getValue(Double.class);
                long1 = dataSnapshot.child("Coords").child("Long1").getValue(Double.class);
//                String Long = long_.toString();
                 alt0 = dataSnapshot.child("Coords").child("Alt0").getValue(Double.class);
                 alt1 = dataSnapshot.child("Coords").child("Alt1").getValue(Double.class);
//                String alt = alt_.toString();
                if(isInClass()) signInBtn.setEnabled(true);
                unitName.setText(unit);
                venue.setText(v);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                errorPane.setVisibility(View.VISIBLE);
                //Toast.makeText(Attend.this, "Error while getting data"+databaseError., Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
        moveTaskToBack(true);

    }


    public void Retry(View view) {
        getLesson();
    }
    public boolean isInClass(){
        boolean inClass = false;
        if(Latitude< lat0 && Latitude> lat1 &&
            longitude>long0 && longitude<long1
             && alt>alt0 && alt<alt1)
            inClass = true;
        return inClass;
    }

    public void SignIn(View view) {
        progressBar.setVisibility(View.VISIBLE);
        Date currentDate = Calendar.getInstance().getTime();
        String[] days = new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        int dayOfWeekInt = currentDate.getDay();
        final String dayOfWeek = days[dayOfWeekInt-1];
        String date = currentDate.getDate()+"-"+currentDate.getMonth()+"-"+currentDate.getYear();
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail().replaceAll("[\\W.]","_");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SignUps");
        databaseReference
                    .child(date)
                    .child(Unit)
                    .child(email).setValue("attended", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                progressBar.setVisibility(View.GONE);
                if(databaseError!=null){
                    Toast.makeText(Attend.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
             });

    }
    @SuppressLint("MissingPermission")
    public  void getLocation(){
        if(!hasPermision()){
            getPerm();
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20, 1, locationListener);
        }
    }

}
