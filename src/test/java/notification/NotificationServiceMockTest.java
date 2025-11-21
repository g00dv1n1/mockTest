package notification;

import org.example.notification.NotificationRepository;
import org.example.notification.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceMockTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void noNotification_returnsNoNotifications() {
        Long userId = 1L;
        when(notificationRepository.existsByUserId(userId)).thenReturn(false);
        String result = notificationService.getFormattedNotification(userId);
        assertEquals("No notifications", result);
    }

    @Test
    void notificationExists_returnsCustomMessage() {
        Long userId = 2L;
        when(notificationRepository.existsByUserId(userId)).thenReturn(true);
        when(notificationRepository.getLastNotificationText(userId)).thenReturn("Mocked text");
        String result = notificationService.getFormattedNotification(userId);
        assertEquals("Last notification: Mocked text", result);
    }
}
