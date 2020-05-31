package edu.monash.MovieMemoir;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.monash.MovieMemoir.database.WatchlistDatabase;
import edu.monash.MovieMemoir.entity.Watchlist;

public class WatchlistAlarmService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    protected WatchlistDatabase watchlistDatabase = null;
    private boolean isCreatedChannel = false;
    private String SERVICE_CHANNEL = "WATCHLIST_ALARM_SERVICE_CHANNEL";
    public WatchlistAlarmService() {
        super("WatchlistAlarmService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent_no_use) {
        watchlistDatabase = WatchlistDatabase.getInstance(this);
        if(!isCreatedChannel){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Watchlist Alarm Service";
                String description = "Alarm service for watchlist notifications";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(SERVICE_CHANNEL, name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(channel);
                isCreatedChannel = true;
            }
        }
        List<Watchlist> movies = watchlistDatabase.watchlistDao().getAllWatchlist();
        for(Watchlist movie : movies)
        {
            String mname = movie.getTitle();
            String userAddDate = movie.getAdd_date();
            try {
                @SuppressLint("SimpleDateFormat") Date user_add_date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(userAddDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(user_add_date);
                calendar.add(Calendar.DATE,7);
                if(calendar.getTime().compareTo(user_add_date) >= 0){
                    long UNIQUE_ID = System.currentTimeMillis();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setAction("OpenWatchList");
                    intent.putExtra("Title", mname);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) UNIQUE_ID, intent, PendingIntent.FLAG_IMMUTABLE);
                    Notification notification = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        notification = new Notification.Builder(this, SERVICE_CHANNEL)
                                .setSmallIcon(R.mipmap.ic_launcher_round)
                                .setContentTitle("My Movie Memoir")
                                .setContentText(mname)
                                .setContentIntent(pendingIntent)
                                .setOngoing(true)
                                .setAutoCancel(true)
                                .build();
                    }
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    NotificationManager notificationManager = (NotificationManager)this.getSystemService(this.NOTIFICATION_SERVICE);
                    notificationManager.notify((int) UNIQUE_ID, notification);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
}
