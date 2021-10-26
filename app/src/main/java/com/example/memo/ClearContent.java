package com.example.memo;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

public class ClearContent extends Application {
    private List<Activity> activityList = new LinkedList<Activity>();
    private static ClearContent instance;
    public static Context context;
    private Activity nowActivity = null;
    public static BluetoothDevice device;

    public static ClearContent getInstance() {
        if (null == instance) {
            instance = new ClearContent();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }



    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
    }
}

