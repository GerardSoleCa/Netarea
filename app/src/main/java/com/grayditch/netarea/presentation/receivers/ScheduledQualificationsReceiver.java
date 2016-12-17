package com.grayditch.netarea.presentation.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;

import com.grayditch.netarea.R;
import com.grayditch.netarea.domain.Mark;
import com.grayditch.netarea.domain.Subject;
import com.grayditch.netarea.domain.interactor.interfaces.CheckNewQualificationsUseCase;
import com.grayditch.netarea.presentation.App;
import com.grayditch.netarea.presentation.views.mainactivity.activity.MainActivity;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by gerard on 21/04/16.
 */
public class ScheduledQualificationsReceiver extends BroadcastReceiver {

    private static final String TAG = ScheduledQualificationsReceiver.class.getClass().getName();
    private static final int NOTIFICATION_ID = 4523;

    @Inject
    CheckNewQualificationsUseCase checkNewQualificationsUseCase;

    @Override
    public void onReceive(final Context context, Intent intent) {
        App.component(context).inject(this);

        Log.v(TAG, "onReceive");

        checkNewQualificationsUseCase.execute(new CheckNewQualificationsUseCase.Callback() {
            @Override
            public void newQualifications(List<Subject> subjects) {
                Notification notification = buildNotification(context, subjects);
                showNotification(context, notification);
            }
        });
    }

    private Notification buildNotification(Context ctx, List<Subject> subjects) {
        PendingIntent pendingIntent = buildPendingIntent(ctx, MainActivity.class);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx);
        notificationBuilder
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setSmallIcon(R.drawable.ic_flag)
                .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_launcher))
                .setContentTitle(ctx.getString(R.string.notifications_title))
//                .setContentText(getNotificationContent(subjects))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getNotificationContent(subjects)))
                .setSubText(ctx.getString(R.string.notifications_subtitle))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        return notificationBuilder.build();
    }

    private <T extends AppCompatActivity> PendingIntent buildPendingIntent(Context ctx, Class<T> clazz) {
        Intent resultIntent = new Intent(ctx, clazz);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(clazz);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        return resultPendingIntent;
    }

    private void showNotification(Context ctx, Notification notification) {
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    private String getNotificationContent(List<Subject> subjects) {
        StringBuffer sb = new StringBuffer();
        for (Subject subject : subjects) {
            sb.append(subject.getName());
            sb.append("\n");
        }
        return sb.toString();
    }
}
