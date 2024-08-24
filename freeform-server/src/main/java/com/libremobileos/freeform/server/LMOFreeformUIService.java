package com.libremobileos.freeform.server;

import static android.content.Context.CONTEXT_IGNORE_SECURITY;
import static android.content.Context.CONTEXT_INCLUDE_CODE;
import static android.os.Process.SYSTEM_UID;

import android.app.PendingIntent;
import android.content.Context;
import android.hardware.display.DisplayManagerInternal;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.ArrayMap;
import android.util.Slog;
import android.view.Surface;

import java.util.Map;

import com.libremobileos.freeform.ILMOFreeformDisplayCallback;
import com.libremobileos.freeform.ILMOFreeformUIService;
import com.libremobileos.freeform.server.ui.FreeformWindowManager;

public class LMOFreeformUIService extends ILMOFreeformUIService.Stub {

    private static final String TAG = "LMOFreeform/LMOFreeformUIService";

    private Context systemContext = null;
    private DisplayManagerInternal displayManager = null;
    private LMOFreeformService lmoFreeformService = null;
    // private Handler uiHandler = null;
    private Handler handler = new Handler();

    public LMOFreeformUIService(Context context, DisplayManagerInternal displayManager, LMOFreeformService lmoFreeformService) {
        if (null == context || null == displayManager || null == lmoFreeformService) return;

        this.systemContext = context;
        this.displayManager = displayManager;
        this.lmoFreeformService = lmoFreeformService;
        // this.uiHandler = displayManager.getUiHandler();
        // this.handler = displayManager.getHandler();

        SystemServiceHolder.init(() -> {
            try {
                ServiceManager.addService("lmo_freeform", this);
                Map<String, IBinder> cache = new ArrayMap<>();
                cache.put("lmo_freeform", this);
                ServiceManager.initServiceCache(cache);
                Slog.i(TAG, "add lmo_freeform SystemService: " + ServiceManager.getService("lmo_freeform"));
            } catch (Exception e) {
                Slog.e(TAG, "add lmo_freeform service failed, " + e);
            }
            if (ServiceManager.getService("lmo_freeform") == null) return;
        });
    }

    @Override
    public void startAppInFreeform(
            String packageName, String activityName, int userId, PendingIntent pendingIntent,
            int width, int height, int densityDpi, float refreshRate,
            boolean secure, boolean ownContentOnly, boolean shouldShowSystemDecorations,
            String resPkg, String layoutName) {
        if (Binder.getCallingUid() != SYSTEM_UID) {
            throw new SecurityException("Caller must be system");
        }
        Slog.i(TAG, "startAppInFreeform");
        FreeformWindowManager.addWindow(
                handler, systemContext,
                packageName, activityName, userId, pendingIntent,
                width, height, densityDpi, refreshRate,
                secure, ownContentOnly, shouldShowSystemDecorations,
                resPkg, layoutName);
    }

    @Override
    public void removeFreeform(String freeformId) {
        if (Binder.getCallingUid() != SYSTEM_UID) {
            throw new SecurityException("Caller must be system");
        }
        FreeformWindowManager.removeWindow(freeformId);
    }

    @Override
    public void createFreeformInUser(
            String name, int width, int height, int densityDpi, float refreshRate,
            boolean secure, boolean ownContentOnly, boolean shouldShowSystemDecorations,
            Surface surface, ILMOFreeformDisplayCallback callback
    ) {
        if (Binder.getCallingUid() != SYSTEM_UID) {
            throw new SecurityException("Caller must be system");
        }
        displayManager.createFreeformLocked(
                name, callback,
                width, height, densityDpi,
                secure, ownContentOnly, shouldShowSystemDecorations,
                surface, refreshRate, 1666666L
        );
    }

    @Override
    public void resizeFreeform(IBinder appToken, int width, int height, int densityDpi) {
        if (Binder.getCallingUid() != SYSTEM_UID) {
            throw new SecurityException("Caller must be system");
        }
        displayManager.resizeFreeform(appToken, width, height, densityDpi);
    }

    @Override
    public void releaseFreeform(IBinder appToken) {
        if (Binder.getCallingUid() != SYSTEM_UID) {
            throw new SecurityException("Caller must be system");
        }
        displayManager.releaseFreeform(appToken);
    }

    @Override
    public boolean ping() {
        if (Binder.getCallingUid() != SYSTEM_UID) {
            throw new SecurityException("Caller must be system");
        }
        // need inputManager is not null
        return lmoFreeformService.isRunning();
    }
}
