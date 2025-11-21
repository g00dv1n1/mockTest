package org.example.notification;

import java.util.HashMap;
import java.util.Map;

public class NotificationRepository {

    private final Map<Long, String> notifications = new HashMap<>();

    public boolean existsByUserId(Long userId) {
        return notifications.containsKey(userId);
    }

    public String getLastNotificationText(Long userId) {
        return notifications.get(userId);
    }

    public void saveNotification(Long userId, String text) {
        notifications.put(userId, text);
    }
}
