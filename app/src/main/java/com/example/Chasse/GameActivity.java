package com.example.Chasse;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.Chasse.View.MapWithPointsView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import android.location.Location;
import android.os.Looper;

public class GameActivity extends AppCompatActivity {
    private MapWithPointsView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        mapView = findViewById(R.id.map_view);

        // Obtient la géocalisation
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                // Données récupérées à partir d'un morceau de carte sur Google Maps
                int latitudeDif = 1200;
                int longitudeDif = 2400;
                // Latitude et longitude * 1000000
                int latitudeDep = 50276900;
                int longitudeDep = 3982700;

                for (Location location : locationResult.getLocations()) {
                    // Récupération des donnéees
                    Log.d("Location", "Location: " + location.toString());
                    Log.d("Location", "Latitude: " + location.getLatitude());
                    Log.d("Location", "Longitude: " + location.getLongitude());
                    Log.d("Location", "Altitude: " + location.getAltitude());
                    // Conversion des coordonnées GPS en pixel sur une image de dimension 1064*833 pixels
                    // Valeur absolue de l'arondie de la différence entre la loc de départ et la loc récupérée
                    // multipliée en nombre de pixels divisée par la diff récupérée (produit en croix)
                    int y = (int) Math.abs(Math.round((833 * (latitudeDep - location.getLatitude() * 1000000) / latitudeDif)));
                    int x = (int) Math.abs(Math.round(1064 * (longitudeDep - location.getLongitude() * 1000000) / longitudeDif));

                    Log.d("Points", "X: " + x + ", Y: " + y);
                    //mapView.addPoint(x, y);
                    double multiplicator = 2.63;
                    mapView.addPoint((int) Math.round(x * multiplicator), (int) Math.round(y * multiplicator));

                }
                locationResult.getLocations().clear();
            }
        };


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocation();
        }
    }

    /**
     * Demande la permission à l'utilisateur d'utiliser les coordonnées GPS
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Demande la permission (si ce n'est pas déjà fait)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Si c'est accepté
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Récupère la géolocalisation
                getLocation();
            } else {
                Log.d("permission", "permission refusée");
            }
        }
    }

    /**
     * Permet de récupérer la géolocation tous les X temps
     */
    private void getLocation() {
        // Récupère la locationRequest
        LocationRequest locationRequest = LocationRequest.create();
        // Interval de temps
        locationRequest.setInterval(10000);
        // Interval de temps le plus rapide
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("Permission", "Permission refusée");
            return;
        }
        // Va dans locationCallback dans le constructeur
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    /**
     * Une fois cet activity fermée
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }


    /*
    private void scanWifi() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.startScan();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("WiFiScan", "permission manquante");
                return;
            }
            List<ScanResult> results = wifiManager.getScanResults();
            Collections.sort(results, new Comparator<ScanResult>() {
                @Override
                public int compare(ScanResult result1, ScanResult result2) {
                    double distance1 = calculateDistance(result1.level, result1.frequency);
                    double distance2 = calculateDistance(result2.level, result2.frequency);
                    return Double.compare(distance1, distance2);
                }
            });
            StringBuilder sb  = new StringBuilder();
            for (ScanResult result : results) {
                if (result.SSID.equals("eduroam") || result.SSID.equals("personnel") || result.SSID.equals("Conference")) {
                    sb.append("SSID: ");
                    sb.append(result.SSID);
                    sb.append(", BSSID: ");
                    sb.append(result.BSSID);
                    sb.append(", RSSI: ");
                    sb.append(result.level);
                    sb.append(", fréquence: ");
                    sb.append(result.frequency);
                    sb.append(", distance: ");
                    sb.append(calculateDistance(result.level, result.frequency));
                    sb.append("\n\n");
                    Log.d("WiFiScan", sb.toString());
                }
            }
            TextView textView = new TextView(GameActivity.this);
            textView.setText(sb.toString());
            textView.setTextSize(12);
            scrollView.addView(textView);
        }
    }
     */
}
