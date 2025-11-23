package notification;

import org.example.notification.NotificationRepository;
import org.example.notification.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceSpyRealTest {

    @Spy
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void spyRealBehavior_noMocking() {
        Long userId = 10L;
        notificationRepository.saveNotification(userId, "Real notification!");
        String result = notificationService.getFormattedNotification(userId);
        assertEquals("Last notification: Real notification!", result);
        verify(notificationRepository).existsByUserId(userId);
        verify(notificationRepository).getLastNotificationText(userId);
    }
}
