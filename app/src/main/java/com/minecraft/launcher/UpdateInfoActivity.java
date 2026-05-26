package com.minecraft.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

public class UpdateInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_update);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        String version = getIntent().getStringExtra("version");
        String date = getIntent().getStringExtra("date");
        String changelog = getIntent().getStringExtra("changelog");

        TextView versionText = findViewById(R.id.update_version);
        TextView dateText = findViewById(R.id.update_date);
        TextView changelogText = findViewById(R.id.changelog_content);
        Button installButton = findViewById(R.id.btn_install_update);

        String downloadUrl = getIntent().getStringExtra("download_url");

        if (version != null) versionText.setText("Version: " + version);
        if (date != null) dateText.setText("Published: " + date);
        if (changelog != null) changelogText.setText(changelog);

        installButton.setOnClickListener(v -> {
            if (downloadUrl != null) {
                Toast.makeText(this, "Opening download link...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(downloadUrl));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Download link not available", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
