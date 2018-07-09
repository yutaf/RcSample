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
    private String APP_KEY = "tyda7yly5u4icypi5u3u";
    private String SECRET_KEY = "9u7i4o6ehivenihocugo";
    // Retry in 180 seconds
//    private String APP_KEY = "8o1e7i6avubo5y8e6i9u";
//    private String SECRET_KEY = "reqy4uqytujy2iqyna4a";
    // Retry in 15 seconds
//    private String APP_KEY = "e8efe1o4i6eso5i8osy4";
//    private String SECRET_KEY = "a4iruhyvibuvo6ehu8az";

    private String API_KEY = "d32bec2373cebdd3894e92c316948f3ded8b8aef";
    private String PHONE = "+817031905898";

    // Callback after user taps the prompt asking permission
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
//        this.sendCode();
//        this.verifyCode();
        this.launchVerificationWidget();
//        this.launchOnboardingWidget();
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
//            this.sendCode();
//            this.verifyCode();
            this.launchVerificationWidget();
//            this.launchOnboardingWidget();
        }
    }

    private void launchVerificationWidget() {
        RingcaptchaApplication.verifyPhoneNumber(getApplicationContext(),APP_KEY, SECRET_KEY, new RingcaptchaApplicationHandler() {
            @Override
            public void onSuccess(RingcaptchaVerification ringcaptchaVerification) {
                //Verification successful
            }
            @Override
            public void onCancel() {
                //Decide what do do if user cancelled operation
            }
        });
    }

    private void launchOnboardingWidget() {
        RingcaptchaApplication.onboard(getApplicationContext(),"1e1y4u5idu9e6e6utyhi", "dylo6u8uvero9efi7i8o", new RingcaptchaApplicationHandler() {
            @Override
            public void onSuccess(RingcaptchaVerification ringcaptchaVerification) {
                //Verification successful
                Log.i("test", "Success");
            }
            @Override
            public void onCancel() {
                //Decide what do do if user cancelled operation
                Log.i("test", "Canceled");
            }
        });
    }

    private void verifyCode() {
        RingcaptchaAPIController controller = new RingcaptchaAPIController(APP_KEY);
        String pin = "2427";
        controller.verifyCaptchaWithCode(getApplicationContext(), pin, new RingcaptchaHandler() {

            //Called when the response is successful
            @Override
            public void onSuccess(RingcaptchaResponse ringcaptchaResponse) {
                //Clear SMS handler to avoid multiple verification attempts
                RingcaptchaAPIController.setSMSHandler(null);
            }

            //Called when the response is unsuccessful
            @Override
            public void onError(Exception e) {
                //Display an error to the user
                Log.i("test", e.getMessage());
            }
        },API_KEY);
    }

    private void sendCode() {
        RingcaptchaAPIController controller = new RingcaptchaAPIController(APP_KEY);
        controller.sendCaptchaCodeToNumber(getApplicationContext(), PHONE, RingcaptchaService.SMS, new RingcaptchaHandler() {
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
        }, API_KEY);
    }
}
