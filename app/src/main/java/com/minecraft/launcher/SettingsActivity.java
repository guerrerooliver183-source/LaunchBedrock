package com.minecraft.launcher;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.materialswitch.MaterialSwitch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private TextView themeStatusText;

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

        setupThemeSelector();
        setupSwitches();
    }

    private void setupThemeSelector() {
        View themeRow = findViewById(R.id.theme_row);
        themeStatusText = findViewById(R.id.theme_status);
        
        updateThemeStatusText();

        themeRow.setOnClickListener(v -> showThemeDialog());
    }

    private void updateThemeStatusText() {
        int theme = prefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        String status;
        if (theme == AppCompatDelegate.MODE_NIGHT_NO) status = getString(R.string.light);
        else if (theme == AppCompatDelegate.MODE_NIGHT_YES) status = getString(R.string.dark);
        else status = getString(R.string.system_default);
        themeStatusText.setText(status);
    }

    private void showThemeDialog() {
        String[] options = {getString(R.string.system_default), getString(R.string.light), getString(R.string.dark)};
        int currentTheme = prefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        int checkedItem = 0;
        if (currentTheme == AppCompatDelegate.MODE_NIGHT_NO) checkedItem = 1;
        else if (currentTheme == AppCompatDelegate.MODE_NIGHT_YES) checkedItem = 2;

        new AlertDialog.Builder(this)
                .setTitle(R.string.select_theme)
                .setSingleChoiceItems(options, checkedItem, (dialog, which) -> {
                    int mode;
                    if (which == 1) mode = AppCompatDelegate.MODE_NIGHT_NO;
                    else if (which == 2) mode = AppCompatDelegate.MODE_NIGHT_YES;
                    else mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

                    prefs.edit().putInt("theme_mode", mode).apply();
                    AppCompatDelegate.setDefaultNightMode(mode);
                    updateThemeStatusText();
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel_dialog, null)
                .show();
    }

    private void setupSwitches() {
        MaterialSwitch switchBlack = findViewById(R.id.switch_black_background);
        MaterialSwitch switchCustom = findViewById(R.id.switch_disable_custom);
        MaterialSwitch switchHide = findViewById(R.id.switch_hide_app);
        MaterialSwitch switchAutoStart = findViewById(R.id.switch_auto_start);

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
