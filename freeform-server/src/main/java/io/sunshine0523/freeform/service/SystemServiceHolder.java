package io.sunshine0523.freeform.service;

import android.app.IActivityManager;
import android.app.IActivityTaskManager;
import android.os.Build;
import android.os.ServiceManager;
import android.view.IWindowManager;

import com.android.internal.statusbar.IStatusBarService;
import com.android.server.input.InputManagerService;

import io.sunshine0523.freeform.util.MLog;

public class SystemServiceHolder {

    private static final String TAG = "Mi-Freeform/SystemServiceHolder";

    static InputManagerService inputManagerService;
    //For O,P
    public static IActivityManager activityManager;
    //For Q,R,S,T
    public static IActivityTaskManager activityTaskManager;
    public static IWindowManager windowManager;
    public static IStatusBarService statusBarService;

    static void init(ServiceCallback callback) {
        new Thread(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                waitSystemService("activity_task");
                activityTaskManager = IActivityTaskManager.Stub.asInterface(ServiceManager.getService("activity_task"));
            } else {
                waitSystemService("activity");
                activityManager = IActivityManager.Stub.asInterface(ServiceManager.getService("activity"));
            }
            waitSystemService("input");
            waitSystemService("window");
            waitSystemService("statusbar");
            inputManagerService = (InputManagerService) ServiceManager.getService("input");
            windowManager = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
            statusBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
            callback.allAdded();
        }).start();
    }

    public static void waitSystemService(String name) {
        int count = 20;
        try {
            while (count-- > 0 && null == ServiceManager.getService(name)) {
                Thread.sleep(1000);
                MLog.i(TAG, name + " not start, wait 1s");
            }
        } catch (Exception ignored) { }
    }

    interface ServiceCallback {
        void allAdded();
    }
}
