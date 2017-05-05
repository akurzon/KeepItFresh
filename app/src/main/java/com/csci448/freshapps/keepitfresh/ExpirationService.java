package com.csci448.freshapps.keepitfresh;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MMMM dd", Locale.US);

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

            if (expiredItems.isEmpty()) {
                return;
            }

            Intent i = ItemListActivity.newIntent(this);
            PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

            String notificationMessage = "";
            if (expiredItems.size() == 1) {
                notificationMessage = "One item expiring soon\n" +
                        expiredItems.get(0).getName() +
                        mDateFormat.format(expiredItems.get(0).getExpirationDate());
            }
            else {
                notificationMessage += expiredItems.size() + "items expiring soon";
                    notificationMessage += "\n" + expiredItems.get(0).getName() + "and " +
                            (expiredItems.size() - 1) + " others";
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setTicker(getResources().getString(R.string.expired_items_title))
                    .setSmallIcon(R.drawable.ic_expire_notification)
                    .setContentTitle(getResources().getString(R.string.expired_items_title))
                    .setContentText(notificationMessage)
                    .setContentIntent(pi)
                    .setAutoCancel(true);

            // handle app version to check if we can set an expanded layout style to notification
            if (Build.VERSION.SDK_INT > 4.1) {
                NotificationCompat.InboxStyle inboxStyle =
                        new NotificationCompat.InboxStyle();
                inboxStyle.setBigContentTitle(getResources().getString(R.string.expired_items_title));

                for (Item expiredItem : expiredItems) {
                    inboxStyle.addLine(expiredItem.getName()+ ": "
                            + mDateFormat.format(expiredItem.getExpirationDate()));
                }
                builder.setStyle(inboxStyle);

            }
            Notification notification = builder.build();
            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(this);
            notificationManager.notify(0, notification);
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
