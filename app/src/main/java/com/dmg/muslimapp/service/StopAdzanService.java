package com.dmg.muslimapp.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import static com.dmg.muslimapp.utils.Constants.NOTIFICATION_ID;

public class StopAdzanService extends Service {
    Intent intn;
    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        this.intn = intent;
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("onStopAdzan"));
        stopSelf();

        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID);
    }
}
