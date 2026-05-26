package com.minecraft.launcher;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        setupSwitches();
    }

    private void setupSwitches() {
        MaterialSwitch switchTheme = findViewById(R.id.switch_theme);
        MaterialSwitch switchBlack = findViewById(R.id.switch_black_background);
        MaterialSwitch switchCustom = findViewById(R.id.switch_disable_custom);
        MaterialSwitch switchHide = findViewById(R.id.switch_hide_app);
        MaterialSwitch switchAutoStart = findViewById(R.id.switch_auto_start);

        bindSwitch(switchTheme, "theme_enabled", false);
        bindSwitch(switchBlack, "black_mode", false);
        bindSwitch(switchCustom, "disable_custom", false);
        bindSwitch(switchHide, "hide_original", false);
        bindSwitch(switchAutoStart, "auto_start", true);
    }

    private void bindSwitch(MaterialSwitch sw, String key, boolean defaultValue) {
        sw.setChecked(prefs.getBoolean(key, defaultValue));
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(key, isChecked).apply();
        });
    }
}
