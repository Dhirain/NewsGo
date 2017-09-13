package com.dhirain.newsgo.fileUtils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.dhirain.newsgo.NewsGoApp;
import com.dhirain.newsgo.utills.GenericCallback;
import com.koushikdutta.ion.Ion;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by DJ on 03-06-2017.
 */

public class ReadWrite {

    public static void saveFile(String url, String fileName) throws IOException{
        if(FileUtil.isExternalStorageWritable() && FileUtil.isExternalStorageReadable()){
            final Context context  = NewsGoApp.singleton().getContext();
            Ion.with(context)
                    .load(url)
                    .write(new File(ReadWrite.getAppDirectory(),fileName+FileUtil.HTML))
                    .setCallback((e, file) -> {
                        if(file == null){
                            Toast.makeText(context, "URL not exist", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(context, "download completed", Toast.LENGTH_SHORT).show();
                    });
        }
        else {
            Toast.makeText(NewsGoApp.singleton().getContext(), "SD card not found", Toast.LENGTH_SHORT).show();
        }
    }

    public static String readFile(String fileName) throws IOException{
        String myData = "";
        if(FileUtil.isExternalStorageWritable() && FileUtil.isExternalStorageReadable()){
                File file = new File(ReadWrite.getAppDirectory(),fileName+FileUtil.HTML);
                FileInputStream fis = new FileInputStream(file);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    myData = myData + strLine;
                }
                in.close();
        }
        else {
            Toast.makeText(NewsGoApp.singleton().getContext(), "SD card not found", Toast.LENGTH_SHORT).show();
            throw  new IOException("SD card not found");
        }
        return myData;
    }


    private static File getAppDirectory(){

        String root = Environment.getExternalStorageDirectory().toString();

        File myDir = new File(root + FileUtil.FOLDER_NAME);

        myDir.mkdirs();

        return myDir;

    }

    public static void readFile(final String fileName, GenericCallback callback)    {
        Single.fromCallable(() -> readFile(fileName))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (data) -> callback.onRequestSuccess(data),
                        (err) -> callback.onRequestFailure(err, err.getMessage())
                );
    }

}
