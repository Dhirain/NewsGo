package com.dhirain.newsgo.permision;

/**
 * Created by DJ on 03-09-2017.
 */

public interface PermissionProvider {

    int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 103;

    /**
     * Returns true if the app has permission to write to external storage.
     * @return
     */
    boolean hasWriteExternalStoragePermission();

    /**
     * Request permission to write to external storage.
     */
    void requestWriteExternalStoragePermission();

}

