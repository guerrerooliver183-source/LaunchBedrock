package com.minecraft.launcher.utils;

import android.os.Handler;
import android.os.Looper;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateChecker {

    private static final String GITHUB_API_URL = "https://api.github.com/repos/guerrerooliver183-source/Bedrock_Launcher/releases/latest";
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public interface UpdateCallback {
        void onUpdateFound(String version, String date, String changelog, String downloadUrl);
        void onNoUpdate();
        void onError(Exception e);
    }

    public static void checkForUpdates(String currentVersion, UpdateCallback callback) {
        executor.execute(() -> {
            try {
                URL url = new URL(GITHUB_API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

                if (connection.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject json = new JSONObject(response.toString());
                    String latestVersion = json.getString("tag_name");
                    String date = json.getString("published_at").substring(0, 10);
                    String changelog = json.getString("body");
                    String downloadUrl = json.getJSONArray("assets").getJSONObject(0).getString("browser_download_url");

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (!latestVersion.equals(currentVersion)) {
                            callback.onUpdateFound(latestVersion, date, changelog, downloadUrl);
                        } else {
                            callback.onNoUpdate();
                        }
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onNoUpdate());
                }
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onError(e));
            }
        });
    }
}
