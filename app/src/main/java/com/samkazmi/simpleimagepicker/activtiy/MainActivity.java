package com.samkazmi.simpleimagepicker.activtiy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.samkazmi.simpleimagepicker.R;
import com.samkazmi.simpleimagepicker.helper.Helper;
import com.samkazmi.simpleimageselect.Config;
import com.samkazmi.simpleimageselect.SimpleImageSelect;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleImageSelect.Config(this, "Choose Profile Picture from", "FolderName");
    }

    private void chooseSingleImage() {
        SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_BOTH, this);
        // SimpleImageSelect.chooseSingleImage(Config.TYPE_CAMERA, this);
        //SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_GALLERY_ONLY, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            String path = SimpleImageSelect.onActivityResult(this, requestCode, resultCode, data);
            if (path != null) {
                Log.v("MainActiviy", "app: " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chooseImageFrom(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Helper.checkForPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                            , android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA}
                    , 23231, R.string.storage_permission_for_profile_picture, findViewById(R.id.button))) {
                chooseSingleImage();
            }
        } else {
            chooseSingleImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 23231) {
            boolean grantedAll = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    grantedAll = false;
                    break;
                }
            }
            if (grantedAll) {
                chooseImageFrom(null);
            } else {
                Toast.makeText(this, "all permissions are not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SimpleImageSelect.ClearConfig(this);
    }
}
