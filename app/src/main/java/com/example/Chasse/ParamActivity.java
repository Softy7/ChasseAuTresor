package com.example.Chasse;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;


public class ParamActivity extends AppCompatActivity {

    Button modifprofil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);
        modifprofil = (Button) findViewById(R.id.modifprofilbtn);

        this.modifprofil.setOnClickListener(v -> {
            Intent intent = new Intent(ParamActivity.this, ChangeProfilActivity.class);
            startActivity(intent);
        });



    }



}
