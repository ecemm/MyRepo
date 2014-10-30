package com.planetgeekz;

import android.app.Notification;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class SMNotificationListener extends NotificationListenerService{

	private static final String TAG="Planetgeekz-SMNotificationListener";
	
	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		
		Notification mNotification=sbn.getNotification();
        if (mNotification!=null){
        	
            String packageName = sbn.getPackageName();
            Log.i(TAG,"ID :" + sbn.getId() + "\t" + packageName+" posted");

            Intent intent = new Intent(Constants.PLANETGEEKZ_NOTIFICATION);
            intent.putExtra(Constants.INTENT_EXTRAS_PACKAGE_NAME, packageName);
            intent.putExtra(Constants.INTENT_EXTRAS_NOTIFICATION_REMOVED, false);
            sendBroadcast(intent);

        }
		
	}
	
	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		Notification mNotification=sbn.getNotification();
        if (mNotification!=null){
        	
            String packageName = sbn.getPackageName();
            Log.i(TAG,"ID :" + sbn.getId() + "\t" + packageName+" removed");

            Intent intent = new Intent(Constants.PLANETGEEKZ_NOTIFICATION);
            intent.putExtra(Constants.INTENT_EXTRAS_NOTIFICATION_REMOVED, true);
            sendBroadcast(intent);

        }
	}
}
