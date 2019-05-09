package vn.edu.hcmute.mp.g4mediaplayer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.ScanSongService;

public class WelcomeActivity extends AppCompatActivity {

    private SharedPreferences ref;
    private static final String FIRST_RUN = "FIRST_RUN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ref = getSharedPreferences("vn.edu.hcmute.mp.g4mediaplayer", MODE_PRIVATE);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadResource();
        } else {
            ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    private void loadResource() {
        if (ref.getBoolean(FIRST_RUN, true)) {
            try {
                ScanSongService loadService = new ScanSongService(this);
                loadService.scanAndSave();
                ref.edit().putBoolean(FIRST_RUN, false).apply();
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            } catch (Exception e) {
                Log.e("ERROR_READ", e.getMessage());
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadResource();
        } else {
            Toast.makeText(WelcomeActivity.this, "You doesn't allow permission, so you can't see your song.",
                    Toast.LENGTH_LONG).show();
        }
    }
}