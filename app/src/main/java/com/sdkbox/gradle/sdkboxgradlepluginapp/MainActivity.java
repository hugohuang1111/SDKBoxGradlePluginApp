package com.sdkbox.gradle.sdkboxgradlepluginapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiBanner;
import com.inmobi.ads.InMobiInterstitial;
import com.inmobi.sdk.InMobiSdk;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SDKBox";
    private InMobiInterstitial _interstitial;
    private InMobiBanner _banner;

    private class InMobiListener implements InMobiInterstitial.InterstitialAdListener2, InMobiBanner.BannerAdListener {

        @Override
        public void onAdLoadFailed(InMobiInterstitial inMobiInterstitial, InMobiAdRequestStatus inMobiAdRequestStatus) {
            Log.d(TAG, "onAdLoadFailed:" + inMobiAdRequestStatus.getMessage());
        }

        @Override
        public void onAdReceived(InMobiInterstitial inMobiInterstitial) {

        }

        @Override
        public void onAdLoadSucceeded(InMobiInterstitial inMobiInterstitial) {
            Log.d(TAG, "onAdLoadSucceeded");
        }

        @Override
        public void onAdRewardActionCompleted(InMobiInterstitial inMobiInterstitial, Map<Object, Object> map) {

        }

        @Override
        public void onAdDisplayFailed(InMobiInterstitial inMobiInterstitial) {

        }

        @Override
        public void onAdWillDisplay(InMobiInterstitial inMobiInterstitial) {

        }

        @Override
        public void onAdDisplayed(InMobiInterstitial inMobiInterstitial) {

        }

        @Override
        public void onAdInteraction(InMobiInterstitial inMobiInterstitial, Map<Object, Object> map) {

        }

        @Override
        public void onAdDismissed(InMobiInterstitial inMobiInterstitial) {

        }

        @Override
        public void onUserLeftApplication(InMobiInterstitial inMobiInterstitial) {

        }




        @Override
        public void onAdLoadSucceeded(InMobiBanner inMobiBanner) {
            Log.d(TAG, "onAdLoadSucceeded banner");
        }

        @Override
        public void onAdLoadFailed(InMobiBanner inMobiBanner, InMobiAdRequestStatus inMobiAdRequestStatus) {
            Log.d(TAG, "onAdLoadFailed banner:" + inMobiAdRequestStatus.getMessage());
        }

        @Override
        public void onAdDisplayed(InMobiBanner inMobiBanner) {

        }

        @Override
        public void onAdDismissed(InMobiBanner inMobiBanner) {

        }

        @Override
        public void onAdInteraction(InMobiBanner inMobiBanner, Map<Object, Object> map) {

        }

        @Override
        public void onUserLeftApplication(InMobiBanner inMobiBanner) {

        }

        @Override
        public void onAdRewardActionCompleted(InMobiBanner inMobiBanner, Map<Object, Object> map) {

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("HHH", "InMobiSdk init");

        InMobiSdk.init(MainActivity.this, "922cc696d9fa475097651b5cad78567d");
        _interstitial = new InMobiInterstitial(MainActivity.this, 1449919424310L, new InMobiListener());

        _banner = (InMobiBanner)findViewById(R.id.banner);
        _banner.load();

        Button btn = (Button)findViewById(R.id.btnLoadAD);
        if (null != btn) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _interstitial.load();
                }
            });
        }

        btn = (Button)findViewById(R.id.btnShowAD);
        if (null != btn) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _interstitial.show();
                }
            });
        }

        btn = (Button)findViewById(R.id.btnLoadBanner);
        if (null != btn) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _banner.load();
                }
            });
        }


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    URL url = new URL("https://www.so.com");
//                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//                    conn.connect();
//                } catch (Exception e) {
//                    Log.d("HHH", e.toString());
//                }
//            }
//        }).run();

    }
}
