package com.aseemsethi.bookappoauth;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.aseemsethi.bookappoauth.ui.main.PageViewModel;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.aseemsethi.bookappoauth.ui.main.SectionsPagerAdapter;
import com.aseemsethi.bookappoauth.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    final String TAG = "BookAppOauth Main";
    private ActivityMainBinding binding;
    BroadcastReceiver myReceiverMqtt = null;
    BroadcastReceiver myReceiverMqttStatus = null;
    private PageViewModel pageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        //startService(new Intent(this,MqttService.class));
        Context context = getApplicationContext();
        Intent serviceIntent = new Intent(context,
                myMqttService.class);
        serviceIntent.setAction(myMqttService.MQTTSUBSCRIBE_ACTION);
        serviceIntent.putExtra("topic", "aseemsethi");
        startService(serviceIntent);
    }
    void registerServices() {
        Log.d(TAG, "registerServices called filter1");
        IntentFilter filter1 = new IntentFilter("RestartMqtt");
        myReceiverMqtt = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isMyServiceRunning(myMqttService.class)) {
                    Log.d(TAG, "registerServices: svc is already running");
                    return;
                }
                Log.d(TAG, "registerServices: restart mqttService");
                Intent serviceIntent = new Intent(context, myMqttService.class);
                serviceIntent.setAction(myMqttService.MQTTSUBSCRIBE_ACTION);
                serviceIntent.putExtra("topic", "pmoa");
                startService(serviceIntent);
            }
        };
        registerReceiver(myReceiverMqtt, filter1);

        Log.d(TAG, "registerServices called filter2");
        IntentFilter filter2 = new IntentFilter("com.aseemsethi.bookappoauth.IdStatus");
        myReceiverMqttStatus = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "registerServices: IdStatus:" +
                        intent.getStringExtra("Id") + " : " +
                        intent.getStringExtra("Status"));
                pageViewModel.setStatus( intent.getStringExtra("Id") +
                        ":" + intent.getStringExtra("Status"));
            }
        };
        registerReceiver(myReceiverMqttStatus, filter2);
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume - Register BroadcastReceiver");
        //registerServices();
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "OnStart - Register BroadcastReceiver");
        registerServices();
    }
}