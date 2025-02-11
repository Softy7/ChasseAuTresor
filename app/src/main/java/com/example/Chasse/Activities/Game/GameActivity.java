package com.example.Chasse.Activities.Game;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.Chasse.Activities.Game.Chat.ChatActivity;
import com.example.Chasse.Model.Point;
import com.example.Chasse.R;
import com.example.Chasse.View.MapWithPointsView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import android.location.Location;
import android.os.Looper;
import io.socket.emitter.Emitter;

import java.util.*;

public class GameActivity extends Games {

    private ImageButton openChat;
    private MapWithPointsView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private final ArrayList<Point> points = new ArrayList<>();
    private Point pointWhereToGo;
    private Point player1Position = new Point(-999999, -999999);
    private Point player2Position = new Point(-999999, -999999);
    private int counterPart;
    private int counterGameWins = 0;
    private TextView textState;
    private final ArrayList<Intent> miniGamesList = new ArrayList<>();
    private final Intent[] miniGamesOrder = new Intent[NUMBER_OF_MINI_GAMES];
    private static final String COUNTER_MINI_GAMES_PLAYED = "miniGamesPlayed";
    private static final String NUMBER_MINI_GAMES_WON = "numberOfMiniGamesWon";
    private static final String IS_THE_LAST_PART = "isTheLastPart";
    private volatile int numberOfPlayersNeedNearToPoint;
    private volatile boolean isGameCanStart = false;

    // Mode développeur
    private ImageButton buttonModeDev;
    private EditText editTextPosX;
    private EditText editTextPosY;
    private boolean isDevModeEnabled = false;
    private final Map<Integer, List<?>> mapGameList = new HashMap<>(); // Activity, nombre de joueurs proche du point


    private boolean isTheMainUser;
    private boolean isTheMiniGameWillStart = false;
    private volatile boolean loop = true;

    private Thread thread;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("finished", String.valueOf(game.isFinished()));
        if (game.isFinished()){
            finish();
        }

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.game_activity);
        mapView = findViewById(R.id.map_view);
        textState = findViewById(R.id.statut_game);

        // Initialisation point
        mapView.modifyPointPosition(player1Position.getX(), player1Position.getY(), 2);

        // Intent
        Intent intent = getIntent();
        counterPart = intent.getIntExtra("miniGamesPlayed", 0);
        counterGameWins = intent.getIntExtra("counterMiniGamesWon", 0);
        isTheMainUser = intent.getBooleanExtra("isTheMainUser", false);

        // AJOUT DES MINI JEUX
        Intent enigmaActivity = new Intent(GameActivity.this, EnigmaActivity.class);
        Intent couleursActivity = new Intent(GameActivity.this, CouleursActivity.class);
        Intent puzzleActivity = new Intent(GameActivity.this, PuzzleActivity.class);

        mapGameList.put(0, Arrays.asList(enigmaActivity, 1));
        mapGameList.put(1, Arrays.asList(couleursActivity, 1));
        mapGameList.put(2, Arrays.asList(puzzleActivity, 2));


        try {
            if (mapGameList.containsKey(counterPart)) {
                List<?> values = mapGameList.get(counterPart);

                if (values.size() > 1 && values.get(1) instanceof Integer) {
                    numberOfPlayersNeedNearToPoint = (int) values.get(1);
                } else {
                    numberOfPlayersNeedNearToPoint = 1;
                }
            } else {
                numberOfPlayersNeedNearToPoint = 1;
            }
        } catch (Exception e) {
            numberOfPlayersNeedNearToPoint = 1;
        }

        openChat = findViewById(R.id.openChat);
        openChat.setOnClickListener(v -> {
            Intent intentChat = new Intent(GameActivity.this, ChatActivity.class);
            startActivity(intentChat);
        });


        // AJOUT DES POINTS
        points.add(new Point(449, 238, 0, "Table de ping-pong"));
        points.add(new Point(134, 165, 0, "Garage à vélos"));
        points.add(new Point(502, 403, 0, "Terrain de pétanque"));
        points.add(new Point(680, 325, 0, "Machine à café, à côté de l'entrée principale"));
        points.add(new Point(829, 133, 0, "Au bureau des étudiants, ou à proximité si fermé"));
        points.add(new Point(307, 174, 1, "Au bout du couloir, à côté de la salle S1.10"));
        points.add(new Point(595, 166, 1, "A la salle de pause au 1er étage"));
        //points.add(new Point(690, 81, 1, "A la salle S1.05, ou à proximité si fermé ou occupé"));
        points.add(new Point(918, 78, 1, "Au fond du couloir, à proximité de la salle S1.09"));
        points.add(new Point(702, 359, 1,"Au dessus de la porte d'entrée principale, à côté de la salle S1.15"));
        points.add(new Point(576, 281, 2, "A côté de l'amphithéatre"));


        if (numberOfPlayersNeedNearToPoint == 1){
            textState.setText("Il faut qu'il n'y ait qu'un seul joueur proche du point");
            Thread t1 = new Thread(() -> {
               try{
                   Thread.sleep(1500);
               } catch (InterruptedException ignored) {

               }
               if (isVocalActivate)
                   textToSpeech("Il faut qu'il n'y ait qu'un seul joueur proche du point");
            });
            t1.start();
        } else {
            textState.setText("Il faut qu'il y ait 2 joueurs proches du point");
            Thread t1 = new Thread(() -> {
                try{
                    Thread.sleep(1500);
                } catch (InterruptedException ignored) {

                }
                if (isVocalActivate)
                    textToSpeech("Il faut qu'il y ait 2 joueurs proches du point");
            });
            t1.start();
        }

        Log.d("tableau", Arrays.toString(miniGamesOrder));




        Log.d("counterPart", String.valueOf(counterPart));

        // Mode dev

        buttonModeDev = findViewById(R.id.button_dev_mode);
        editTextPosX = findViewById(R.id.text_input_x);
        editTextPosY = findViewById(R.id.text_input_y);

        // Pour cacher les view
        editTextPosX.setVisibility(View.GONE);
        editTextPosY.setVisibility(View.GONE);

        buttonModeDev.setOnClickListener(v -> {
            if (isDevModeEnabled) {
                editTextPosX.setVisibility(View.GONE);
                editTextPosY.setVisibility(View.GONE);
                isDevModeEnabled = false;
            } else {
                editTextPosX.setVisibility(View.VISIBLE);
                editTextPosY.setVisibility(View.VISIBLE);
                isDevModeEnabled = true;
            }
        });

        showIfThePlayerIsNear();

        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editTextPosX.getText().length() > 0 && editTextPosY.getText().length() > 0) {
                    try {
                        player1Position = new Point(
                                Integer.parseInt(editTextPosX.getText().toString()),
                                Integer.parseInt(editTextPosY.getText().toString()));
                        mapView.modifyPointPosition(player1Position.getX(), player1Position.getY(), 2);
                    } catch (NumberFormatException e) {
                        //
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        editTextPosX.addTextChangedListener(textWatcher);
        editTextPosY.addTextChangedListener(textWatcher);

        // socket
        socket.on("player state main game", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                int positionXPlayer2 = (int) objects[0];
                int positionYPlayer2 = (int) objects[1];
                player2Position = new Point(positionXPlayer2, positionYPlayer2);
                runOnUiThread(() -> mapView.modifyPointPosition(player2Position.getX(), player2Position.getY(), 3));
            }
        });


        if (isTheMainUser){
            addNewPointToGo();
            socket.emit("point position to go", pointWhereToGo.getX(), pointWhereToGo.getY());

            socket.on("send point", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    socket.emit("point position to go", pointWhereToGo.getX(), pointWhereToGo.getY());
                }
            });
            speakLocation();
        }
        socket.on("point position to go", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                int posX = (int) objects[0];
                int posY = (int) objects[1];
                Log.d("Positions", posX + " " + posY);
                pointWhereToGo = new Point(posX, posY);
                runOnUiThread(() -> mapView.modifyPointPosition(pointWhereToGo.getX(), pointWhereToGo.getY(), 1));
                //speakLocation();
            }
        });




        // Obtient la géocalisation
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (!isDevModeEnabled) {
                    // Données récupérées à partir d'un morceau de carte sur Google Maps
                    int latitudeDif = 1200;
                    int longitudeDif = 2400;
                    // Latitude et longitude * 1000000
                    int latitudeDep = 50276900;
                    int longitudeDep = 3982700;
                    Location location = locationResult.getLastLocation();


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


        socket.on("choose random game", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                if (isTheMainUser && !isTheMiniGameWillStart){
                    isTheMiniGameWillStart = true;
                    //Random random = new Random();
                    //int miniGameId = random.nextInt(miniGamesList.size());
                    socket.emit("new mini game starts");
                }
            }
        });

        socket.on("mini game starting", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                int miniGame = (int) objects[1];
                Log.d("mini game id", String.valueOf(miniGame));
                runOnUiThread(() -> {
                    Intent miniGameIntent;
                    try {
                        miniGameIntent = (Intent) mapGameList.get(counterPart).get(0);
                    } catch (Exception e) {
                        miniGameIntent = new Intent(GameActivity.this, EnigmaActivity.class);
                    }
                    final Intent finalIntent = miniGameIntent;

                    finalIntent.putExtra(IS_THE_MAIN_USER, Long.parseLong(objects[0].toString()) == game.getUserId() );
                    finalIntent.putExtra(COUNTER_MINI_GAMES_PLAYED, counterPart);
                    finalIntent.putExtra(NUMBER_MINI_GAMES_WON, counterGameWins);
                    boolean isTheLastPart = counterPart >= NUMBER_OF_MINI_GAMES - 1;
                    finalIntent.putExtra(IS_THE_LAST_PART, isTheLastPart);
                    isTheGameFinished = false;
                    if (isVocalActivate){
                        textToSpeech("Le prochain mini-jeu va bientôt commencer");
                    } else {
                        Toast.makeText(GameActivity.this, "Le prochain mini-jeu va bientôt commencer", Toast.LENGTH_SHORT).show();
                    }
                    Thread thread1 = new Thread(() -> {
                        // Attend 1 s avant que le jeu démarre
                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException ignored) {
                            //
                        }
                    });
                    thread1.start();
                    try {
                        thread1.join();
                    } catch (InterruptedException ignored) {
                        //
                    } finally {
                        startActivity(finalIntent);
                        finish();
                    }
                });
            }
        });
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
                Math.pow(pointWhereToGo.getY() - pointToPlayer.getY(), 2)) <= 108;
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
        thread.interrupt();
    }

    public void addNewPointToGo(){
        Random random = new Random();
        int numeroPoint = random.nextInt(points.size());
        pointWhereToGo = points.get(numeroPoint);
        mapView.modifyPointPosition(pointWhereToGo.getX(), pointWhereToGo.getY(), 1);
    }

    public void showIfThePlayerIsNear(){
        thread = new Thread(() -> {
            while (loop){
                if (Thread.currentThread().isInterrupted()){
                    break;
                }
                if (pointWhereToGo != null && player1Position != null) {
                    Log.d("position", String.valueOf(this.player1Position.getX()));
                    // émet côté serveur l'état du joueur
                    socket.emit("player state main game", player1Position.getX(), player1Position.getY(), isPlayerNearToPoint(this.pointWhereToGo, this.player1Position), numberOfPlayersNeedNearToPoint);
                } else if (!isTheMainUser && pointWhereToGo == null){
                    socket.emit("point not received");
                }
                try{
                    Thread.sleep(650);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        thread.start();

    }

    private void stopThread() {
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loop = false;
        }

    }


    @Override
    protected void onPreDestroy() {
        socket.off("mini game starting");
        socket.off("choose random game");
        socket.off("player state main game");
        socket.off("send point");
        socket.off("point position to go");
        stopThread();
    }

    private void speakLocation(){
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {}
            Log.d("vocal", "llllllllllllllllllllllllll");
            if (isVocalActivate)
                textToSpeech("Vous devez aller à : " + pointWhereToGo.getDescription());
        });
        t2.start();
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
