package com.minecraft.launcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.minecraft.launcher.utils.SecurityUtils;
import com.minecraft.launcher.utils.UpdateChecker;

public class MainActivity extends AppCompatActivity {

    private TextView statusText;
    private View progress1, progress2, progress3, progress4;
    private CardView notificationCard;
    private SharedPreferences prefs;
    private final String MINECRAFT_TRIAL_PKG = "com.mojang.minecrafttrialpe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);
        statusText = findViewById(R.id.status_text);
        progress1 = findViewById(R.id.progress_segment_1);
        progress2 = findViewById(R.id.progress_segment_2);
        progress3 = findViewById(R.id.progress_segment_3);
        progress4 = findViewById(R.id.progress_segment_4);
        notificationCard = findViewById(R.id.notification_card);

        Button btnCancel = findViewById(R.id.btn_cancel);
        ImageButton btnSettings = findViewById(R.id.btn_settings);

        btnCancel.setOnClickListener(v -> finish());
        btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        checkMinecraftTrial();
        showInitialWarning();
        startLoadingSequence();
        checkForUpdates();
    }

    private void checkMinecraftTrial() {
        if (!SecurityUtils.isPackageInstalled(this, MINECRAFT_TRIAL_PKG)) {
            new AlertDialog.Builder(this)
                .setTitle("Minecraft Trial Not Found")
                .setMessage(R.string.trial_not_installed)
                .setPositiveButton("Download", (d, w) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + MINECRAFT_TRIAL_PKG));
                    startActivity(intent);
                })
                .setNegativeButton("Exit", (d, w) -> finish())
                .show();
        }
    }

    private void showInitialWarning() {
        if (!prefs.getBoolean("dont_show_warning", false)) {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_checkbox, null);
            CheckBox cb = dialogView.findViewById(R.id.dialog_checkbox);
            
            new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton("OK", (d, w) -> {
                    if (cb.isChecked()) {
                        prefs.edit().putBoolean("dont_show_warning", true).apply();
                    }
                })
                .show();
        }
    }

    private void startLoadingSequence() {
        Handler handler = new Handler(Looper.getMainLooper());

        // Inicialmente mostramos el primer segmento para dar feedback
        progress1.setVisibility(View.VISIBLE);

        // Etapa 1: Checking for updates (2 segundos)
        handler.postDelayed(() -> {
            statusText.setText(R.string.starting_minecraft);
            progress2.setVisibility(View.VISIBLE);
        }, 2000);

        // Etapa 2: Starting Minecraft (3 segundos más)
        handler.postDelayed(() -> {
            statusText.setText(R.string.injecting);
            progress3.setVisibility(View.VISIBLE);
            progress4.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }, 5000);

        // Final: Abrir Minecraft
        handler.postDelayed(() -> {
            if (prefs.getBoolean("auto_start", true)) {
                launchMinecraft();
            }
        }, 7000);
    }

    private void launchMinecraft() {
        Intent intent = getPackageManager().getLaunchIntentForPackage(MINECRAFT_TRIAL_PKG);
        if (intent != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Could not launch Minecraft", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkForUpdates() {
        UpdateChecker.checkForUpdates(new UpdateChecker.UpdateCallback() {
            @Override
            public void onUpdateFound(String version, String date, String changelog) {
                showNotification(version, date, changelog);
            }

            @Override
            public void onNoUpdate() {}
        });
    }

    private void showNotification(String version, String date, String changelog) {
        notificationCard.setVisibility(View.VISIBLE);
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(500);
        notificationCard.startAnimation(fadeIn);

        notificationCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, UpdateInfoActivity.class);
            intent.putExtra("version", version);
            intent.putExtra("date", date);
            intent.putExtra("changelog", changelog);
            startActivity(intent);
        });

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setDuration(500);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation animation) {}
                @Override public void onAnimationRepeat(Animation animation) {}
                @Override public void onAnimationEnd(Animation animation) {
                    notificationCard.setVisibility(View.GONE);
                }
            });
            notificationCard.startAnimation(fadeOut);
        }, 5000);
    }
}
