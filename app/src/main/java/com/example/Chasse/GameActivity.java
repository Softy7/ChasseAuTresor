package com.example.Chasse;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private ScrollView scrollView;

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        scrollView = findViewById(R.id.scrollView);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION);
        } else {
            scanWifi();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanWifi();
            } else {
                Log.d("permission", "permission refusée");
            }
        }
    }

    private double calculateDistance(int rssi, int frequency) {
        double exp = (27.55 - (20 * Math.log10(frequency)) + Math.abs(rssi)) / 20.0;
        return Math.pow(10.0, exp);
    }

    private void scanWifi() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.startScan();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("WiFiScan", "permission manquante");
                return;
            }
            List<ScanResult> results = wifiManager.getScanResults();
            List<String> stringList = new ArrayList<>();
            List<Integer> doubleList = new ArrayList<>();
            for (ScanResult result : results) {
                if (result.SSID.equals("eduroam") || result.SSID.equals("personnel") || result.SSID.equals("Conference")) {
                    StringBuilder sb  = new StringBuilder();
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
                    stringList.add(sb.toString());
                    doubleList.add(result.level);
                }
            }
            StringBuilder text = new StringBuilder();
            for (String str : stringList) {
                text.append(str);
            }
            TextView textView = new TextView(GameActivity.this);
            textView.setText(text.toString());
            textView.setTextSize(12);
            scrollView.addView(textView);
        }
    }
}
