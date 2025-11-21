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
public class NotificationSequentialMockTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationService service;

    @Test
    void sequentialMockingTest() {
        Long userId = 1L;

        when(repository.existsByUserId(userId))
                .thenReturn(false)
                .thenReturn(true);

        String first = service.getFormattedNotification(userId);
        assertEquals("No notifications", first);

        when(repository.getLastNotificationText(userId)).thenReturn("Test message");
        String second = service.getFormattedNotification(userId);
        assertEquals("Last notification: Test message", second);
    }
}
