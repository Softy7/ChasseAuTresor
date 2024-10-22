package com.example.Chasse;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;


public class ParamActivity extends AppCompatActivity {

    protected AppCompatButton modifprofil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);

        this.modifprofil = findViewById(R.id.modifprofilbtn);
        this.modifprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParamActivity.this, ChangeProfilActivity.class);
                startActivity(intent);
            }
        });
    }
}
