package com.example.contesto.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.contesto.R;
import com.example.contesto.Utils.Constants;
import com.example.contesto.Utils.Methods;
import com.google.android.material.card.MaterialCardView;
import com.schibsted.spain.parallaxlayerlayout.ParallaxLayerLayout;
import com.schibsted.spain.parallaxlayerlayout.SensorTranslationUpdater;

public class MainActivity2 extends ParentActivity {

    ParallaxLayerLayout mParallaxLayout,mParallaxLayout1;
    SensorTranslationUpdater sensorTranslationUpdater,sensorTranslationUpdater1;
    MaterialCardView running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_main2, content);




        mParallaxLayout = findViewById(R.id.ActivityTwoParallax);
        mParallaxLayout1 = findViewById(R.id.ActivityTwoParallax1);
        sensorTranslationUpdater = new SensorTranslationUpdater(this);
        sensorTranslationUpdater1 = new SensorTranslationUpdater(this);
        mParallaxLayout.setTranslationUpdater(sensorTranslationUpdater);
        mParallaxLayout1.setTranslationUpdater(sensorTranslationUpdater1);


    }

    //  Function to remember current activity.


    @Override
    protected void onResume() {
        super.onResume();
        sensorTranslationUpdater.registerSensorManager();
        sensorTranslationUpdater1.registerSensorManager();

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorTranslationUpdater.unregisterSensorManager();
        sensorTranslationUpdater1.unregisterSensorManager();

    }

}