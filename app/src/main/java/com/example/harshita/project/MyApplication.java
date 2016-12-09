package com.example.harshita.project;

import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;




        import android.app.Application;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
        import android.content.pm.Signature;
        import android.util.Base64;
        import android.util.Log;

        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        printHashKey();
    }

    public void printHashKey(){
        // Add code to print out the key hash
        Log.e("yoyo","yoyoyo");
        try {
            Log.e("Enter error","");
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.harshita.project",
                    PackageManager.GET_SIGNATURES);
            Log.e("package info","");
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.e("where is all this bro","where");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("error tho","error");

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
