package com.km.mbottlecapcollector.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.km.mbottlecapcollector.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileHelper {
    private static final String TAG = FileHelper.class.getSimpleName();
    public static String CAP_PREFIX = "cap";
    public static String SELFIE_PREFIX = "face";
    public static String SCREENSHOT_PREFIX = "sc";

    public static void save(byte[] bytes, File file, Context context) throws IOException {
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            output.write(bytes);
        } finally {
            if (output != null) {
                output.close();
            }
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(file)));
        }
    }


    public static void deleteFilesInPictureFolder(Context context) {
        File pictureFolder = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] fileList = pictureFolder.listFiles();
        int fileCount = fileList.length;
        if (fileCount != 0) {
            Log.i(TAG, "Removing " + fileCount + " files from picture folder");
            for (File picture : fileList) {
                picture.delete();
            }
        }
    }
}
