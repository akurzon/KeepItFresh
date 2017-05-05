package com.csci448.freshapps.keepitfresh;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Date;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ExpirationService extends IntentService {

    private static final long DAY_IN_MS = 1000 * 60 * 60 * 24;
    private static final long POLL_INTERVAL = AlarmManager.INTERVAL_DAY;

    public ExpirationService() {
        super("ExpirationService");
    }

    /**
     * Starts this service to handle monitoring expiration dates
     * @param context is the context within which to start the service
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startService(Context context) {
        Intent intent = new Intent(context, ExpirationService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<Item> items = (ArrayList<Item>)
                    StoredItems.getInstance(getApplicationContext()).getItemList();
            ArrayList<Item> expiredItems = fetchExpiredItems(items);



            for (Item expiredItem : expiredItems) {
                // TODO: 5/4/17 make a notification for your expired items

            }
        }
    }

    private ArrayList<Item> fetchExpiredItems(ArrayList<Item> items) {
        ArrayList<Item> expiredItems = new ArrayList<>();
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int threshold = Integer.valueOf
                (preferences.getString("pref_choose_notification_threshold","1"));

        for (Item item : items) {
            if (item.getExpirationDate().getTime() <=
                    new Date().getTime() + threshold * DAY_IN_MS) {
                expiredItems.add(item);
            }
        }

        return expiredItems;
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent intent = new Intent(context, ExpirationService.class);

        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }

    }

}
