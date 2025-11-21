package org.example.notification;

public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public String getFormattedNotification(Long userId) {
        if (!repository.existsByUserId(userId)) {
            return "No notifications";
        }
        String text = repository.getLastNotificationText(userId);
        return "Last notification: " + text;
    }
}
