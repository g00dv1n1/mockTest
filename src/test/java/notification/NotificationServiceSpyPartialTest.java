package notification;

import org.example.notification.NotificationRepository;
import org.example.notification.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceSpyPartialTest {

    @Spy
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void spyPartialMock_overridesOnlyText() {
        Long userId = 5L;
        notificationRepository.saveNotification(userId, "Real text (ignored)");
        doReturn("Stubbed text").when(notificationRepository).getLastNotificationText(userId);
        String result = notificationService.getFormattedNotification(userId);
        assertEquals("Last notification: Stubbed text", result);
    }
}
