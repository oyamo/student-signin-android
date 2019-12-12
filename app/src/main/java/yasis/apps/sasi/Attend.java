package yasis.apps.sasi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.database.*;

import java.util.Calendar;
import java.util.Date;

public class Attend extends AppCompatActivity {
    LocationManager locationManager;
    DatabaseReference mReference;
    ProgressBar progressBar;
    LinearLayout errorPane;
    TextView venue, unitName;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);
        progressBar = findViewById(R.id.progressAttend);
        errorPane = findViewById(R.id.errorPane);
        venue = findViewById(R.id.venue);
        venue.setText("----");
        unitName = findViewById(R.id.unitName);
        unitName.setText("----");
        progressBar.setVisibility(View.GONE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mReference = FirebaseDatabase.getInstance().getReference("timetable").child("MMU");
        getLesson();
        if(!hasPermision()){
            getPerm();
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20, 1, locationListener);
        }


    }
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double alt = location.getAltitude();
            double x = location.getLatitude();
            double y = location.getLongitude();
            //((TextView)findViewById(R.id.coord)).setText("lat:"+x+", long:"+y+", alt:"+alt);
        }

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
        String dayOfWeek = days[dayOfWeekInt-1];
        DatabaseReference unit = mReference.child(dayOfWeek).child("Unit");
        unit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                errorPane.setVisibility(View.GONE);
                String unit = dataSnapshot.getValue(String.class);
                unitName.setText(unit);
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
}
