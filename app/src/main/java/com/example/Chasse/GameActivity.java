package com.example.Chasse;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.Chasse.Model.Point;
import com.example.Chasse.View.MapWithPointsView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import android.location.Location;
import android.os.Looper;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private MapWithPointsView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private final ArrayList<Point> points = new ArrayList<>();
    private Point pointWhereToGo;
    private Point player1Position;
    private Point player2Position;
    private final Intent enigmaActivity = new Intent(GameActivity.this, EnigmaActivity.class);
    private final Intent couleursActivity = new Intent(GameActivity.this, CouleursActivity.class);
    private int counterPart = 0;
    private int counterGameWins = 0;
    private static final int NUMBER_OF_MINI_GAMES = 3;
    private final Intent[] miniGamesList = new Intent[]{enigmaActivity, couleursActivity};
    private final Intent[] miniGamesOrder = new Intent[NUMBER_OF_MINI_GAMES];



    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        mapView = findViewById(R.id.map_view);


        // AJOUT DES POINTS
        points.add(new Point(449, 238, 0, "Table de ping-pong"));
        points.add(new Point(134, 165, 0, "Garage à vélos"));
        points.add(new Point(502, 403, 0, "Terrain de pétanque"));
        points.add(new Point(680, 325, 0, "Machine à café, à côté de l'entrée principale"));
        points.add(new Point(829, 133, 0, "Au bureau des étudiants, ou à proximité si fermé"));
        points.add(new Point(307, 174, 1, "Au bout du couloir, à côté de la salle S1.10"));
        points.add(new Point(595, 166, 1, "A la salle de pause au 1er étage"));
        points.add(new Point(690, 81, 1, "A la salle S1.05, ou à proximité si fermé ou occupé"));
        points.add(new Point(918, 78, 1, "Au fond du couloir, à proximité de la salle S1.09"));
        points.add(new Point(702, 359, 1,"Au dessus de la porte d'entrée principale, à côté de la salle S1.15"));
        points.add(new Point(576, 281, 2, "A côté de l'amphithéatre"));

        // Ordre des mini-jeux
        for (int i = 0; i < NUMBER_OF_MINI_GAMES; i++) {
            Random rand = new Random();
            miniGamesOrder[i] = miniGamesList[rand.nextInt(NUMBER_OF_MINI_GAMES)];
        }

        // Intent
        Intent intent = getIntent();
        counterPart = intent.getIntExtra("counterPart", 0);
        counterGameWins = intent.getIntExtra("counterGameWins", 0);

        Log.d("counterPart", String.valueOf(counterPart));


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
                Location location = locationResult.getLastLocation();

                //for (Location location : locationResult.getLocations()) {
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

                    player1Position = new Point(x, y);

                    Log.d("Points", "X: " + x + ", Y: " + y);
                    //mapView.addPoint(x, y);
                    mapView.modifyPointPosition((int) Math.round(x), (int) Math.round(y), 2);

                //}
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

            addNewPointToGo();
            showIfThePlayerIsNear();

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
        locationRequest.setInterval(1000);
        // Interval de temps le plus rapide
        locationRequest.setFastestInterval(500);
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
     * Permet de vérifier si un joueur est présent ou pas à proximité du point où aller
     * @param pointWhereToGo: Le point où aller
     * @param pointToPlayer: Le point du joueur actuel
     * @return returne vrai si la distance est inférieure ou égal à 5m, sinon retourne faux
     */
    public boolean isPlayerNearToPoint(Point pointWhereToGo, Point pointToPlayer){
        // 5m = 1024 (trouvé via l'image)
        // 10m = 4096
        // 15m = sqrt(9216) * 2

        Log.d("distance", String.valueOf(Math.sqrt(Math.pow(pointWhereToGo.getX() - pointToPlayer.getX(), 2) +
                Math.pow(pointWhereToGo.getY() - pointToPlayer.getY(), 2))));
        return Math.sqrt(Math.pow(pointWhereToGo.getX() - pointToPlayer.getX(), 2) +
                Math.pow(pointWhereToGo.getY() - pointToPlayer.getY(), 2)) <= 150;
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

    public void addNewPointToGo(){
        Random random = new Random();
        int numeroPoint = random.nextInt(points.size());
        pointWhereToGo = points.get(numeroPoint);
        mapView.modifyPointPosition(pointWhereToGo.getX(), pointWhereToGo.getY(), 1);
    }

    public void showIfThePlayerIsNear(){
        Thread thread = new Thread(() -> {
            while (true){
                if (pointWhereToGo != null && player1Position != null) {

                    Log.d("position", String.valueOf(this.player1Position.getX()));
                    if (isPlayerNearToPoint(this.pointWhereToGo, this.player1Position)) {
                        runOnUiThread(() ->{
                            Toast.makeText(GameActivity.this, "Vous êtes proche du point", Toast.LENGTH_LONG).show();
                            startActivity(miniGamesOrder[counterPart]);
                        });
                        break;
                    }
                    try{
                        synchronized (this){
                            wait(5000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
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
