package com.aseemsethi.bookappoauth.ui.main;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aseemsethi.bookappoauth.MainActivity;
import com.aseemsethi.bookappoauth.R;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class securityFragment extends Fragment {

    private PageViewModel pageViewModel;
    final String TAG = "BookAppOauth: Sec";
    View root;

    public static securityFragment newInstance(int index) {
        return new securityFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.security_fragment, container, false);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        pageViewModel.getLoggedin().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.d(TAG, "onChanged Logged in: " + s);
                Toast.makeText(getActivity().getApplicationContext(),s,
                        Toast.LENGTH_LONG).show();
            }
        });
        pageViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.d(TAG, "onChanged Status: " + s);
                String[] arr = s.split(":", 2);
                    TextView v1 = (TextView) root.findViewById(R.id.sensorValue1);
                    v1.setText("Door:" + arr[0] + ", Status:" + arr[1]);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        Log.d(TAG, "onActivity Created");
        // TODO: Use the ViewModel
    }

}