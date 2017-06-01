package com.example.yuta.rcsample;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.thrivecom.ringcaptcha.RingcaptchaAPIController;
import com.thrivecom.ringcaptcha.RingcaptchaApplication;
import com.thrivecom.ringcaptcha.RingcaptchaApplicationHandler;
import com.thrivecom.ringcaptcha.RingcaptchaService;
import com.thrivecom.ringcaptcha.RingcaptchaVerification;
import com.thrivecom.ringcaptcha.lib.handlers.RingcaptchaHandler;
import com.thrivecom.ringcaptcha.lib.handlers.RingcaptchaSMSHandler;
import com.thrivecom.ringcaptcha.lib.models.RingcaptchaResponse;

public class MainActivity extends AppCompatActivity {

    private int MY_PERMISSIONS_REQUEST_READ_SMS = 0x01;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(requestCode != MY_PERMISSIONS_REQUEST_READ_SMS) {
            return;
        }
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted, yay! Do the
            // contacts-related task you need to do.
        } else {

            // permission denied, boo! Disable the
            // functionality that depends on this permission.
        }
        this.sendCode();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, MY_PERMISSIONS_REQUEST_READ_SMS);
        } else {
            this.sendCode();
        }
    }

    private void sendCode() {
        String app_key = "YOURAPPKEY";
        String api_key = "YOURAPIKEY";

        RingcaptchaAPIController controller = new RingcaptchaAPIController(app_key);
        Context context = getApplicationContext();
        String phone = "PHONENUMBER";
        controller.sendCaptchaCodeToNumber(context, phone, RingcaptchaService.SMS, new RingcaptchaHandler() {
            @Override
            public void onSuccess(RingcaptchaResponse o) {
                RingcaptchaAPIController.setSMSHandler(new RingcaptchaSMSHandler() {
                    @Override
                    public boolean handleMessage(String s, String s1) {
                        return false;
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                // Display an error to the user
                Log.i("test", e.getMessage());
            }
        }, api_key);
    }
}
