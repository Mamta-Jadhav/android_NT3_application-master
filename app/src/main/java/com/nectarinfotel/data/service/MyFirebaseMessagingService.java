package com.nectarinfotel.data.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nectarinfotel.data.activities.DetailActivity;
import com.nectarinfotel.data.activities.TicketDetailsActivity;
import com.nectarinfotel.utils.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    Intent resultIntent;
    private NotificationUtils notificationUtils;
    public String ticket_id, category, status, urgency, id, title, message, ticket_no;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            Log.e(TAG, "Notification title: " + remoteMessage.getNotification().getTitle());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        Log.d("remoteMessage", "" + remoteMessage.getData().size());
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        Intent resultIntent = new Intent(getApplicationContext(), TicketDetailsActivity.class);
        resultIntent.putExtra("message", message);
        resultIntent.putExtra("click_action", "yes");
        showNotificationMessage(getApplicationContext(), "Nt3", message, "11", resultIntent);
    }

    private void handleDataMessage(JSONObject data) {
        Log.e(TAG, "push json: " + data.toString());

        try {
            if (data.has("title")) {
                title = data.getString("title");
            }
            if (data.has("id")) {
                id = data.getString("id");
            }
            if (data.has("urgency")) {
                urgency = data.getString("urgency");
            }
            if (data.has("status")) {
                status = data.getString("status");
            }
            if (data.has("category")) {
                category = data.getString("category");
            }
            if (data.has("ticket_id")) {
                ticket_id = data.getString("ticket_id");
                ticket_no = data.getString("ticket_id");
            }
            //{"status":"new","urgency":"1","id":"35","category":"1","title":"Newwwww"}
            //   boolean isBackground = data.getBoolean("is_background");

            Log.e(TAG, "message: " + title);
            if (title.equalsIgnoreCase("")) {
                title = "New NOtification";
            }

            if (category.equals("1")) {
                if (status.equalsIgnoreCase("new")) {
                    message = "Your new Incident ticket has been raised ";
                } else if (status.equalsIgnoreCase("assign")) {
                    message = "Incident - Ticket Assigned";
                } else if (status.equalsIgnoreCase("reassign")) {
                    message = "Incident - Ticket Re-Assigned";
                } else if (status.equalsIgnoreCase("resolved")) {
                    message = "Incident - Ticket resolved";
                } else if (status.equalsIgnoreCase("closed")) {
                    message = "Incident - Ticket close";
                }
                if (ticket_id != null) {
                    ticket_id = "Ticket No.: " + ticket_id;
                }

            } else if (category.equals("2")) {
                if (status.equalsIgnoreCase("new")) {
                    message = "Your new Problem ticket has been raised ";
                } else if (status.equalsIgnoreCase("assign")) {
                    message = "Problem - Ticket Assigned";
                } else if (status.equalsIgnoreCase("reassign")) {
                    message = "Problem - Ticket Re-Assigned";
                } else if (status.equalsIgnoreCase("resolved")) {
                    message = "Problem - Ticket resolved";
                } else if (status.equalsIgnoreCase("closed")) {
                    message = "Problem - Ticket close";
                }
                if (ticket_id != null) {
                    ticket_id = "Ticket No.: " + ticket_id;
                }
            }

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                if (status.equalsIgnoreCase("new")) {
                    resultIntent = new Intent(getApplicationContext(), DetailActivity.class);
                } else {
                    resultIntent = new Intent(getApplicationContext(), TicketDetailsActivity.class);
                }
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("status", status);
                resultIntent.putExtra("ticket_id", ticket_no);
                resultIntent.putExtra("category", category);
                resultIntent.putExtra("pushnotification", "yes");
                if (status.equalsIgnoreCase("new")) {
                    showNotificationMessage(getApplicationContext(), "Nt3", message, "11", resultIntent);
                } else {
                    showNotificationMessage(getApplicationContext(), message, ticket_id, "11", resultIntent);
                }

            } else {
                // app is in background, show the notification in notification tray
                if (status.equalsIgnoreCase("new")) {
                    resultIntent = new Intent(getApplicationContext(), DetailActivity.class);
                } else {
                    resultIntent = new Intent(getApplicationContext(), TicketDetailsActivity.class);
                }
                resultIntent.putExtra("titile", title);
                resultIntent.putExtra("status", status);
                resultIntent.putExtra("ticket_id", ticket_no);
                resultIntent.putExtra("category", category);
                resultIntent.putExtra("pushnotification", "yes");

                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                if (status.equalsIgnoreCase("new")) {
                    showNotificationMessage(getApplicationContext(), "Nt3", message, "11", resultIntent);
                } else {
                    showNotificationMessage(getApplicationContext(), message, ticket_id, "11", resultIntent);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}