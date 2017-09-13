package com.dhirain.newsgo.permision;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Dhirain on 3/2/17.
 */

public class PermissionProviderImpl implements PermissionProvider {

    private static final String[] WRITE_EXTERNAL_STORAGE_PERMISSION = new String[]{

       Manifest.permission.WRITE_EXTERNAL_STORAGE

    };

    private Context context;

    public PermissionProviderImpl(Context context) {
        this.context = context;
    }


    @Override
    public boolean hasWriteExternalStoragePermission() {

        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void requestWriteExternalStoragePermission() {

        ActivityCompat.requestPermissions((Activity) context, WRITE_EXTERNAL_STORAGE_PERMISSION, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);

    }
}
