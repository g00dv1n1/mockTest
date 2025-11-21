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
public class NotificationSequentialSpyTest {

    @Spy
    private NotificationRepository repository;

    @InjectMocks
    private NotificationService service;

    @Test
    void sequentialSpyTest() {
        Long userId = 1L;

        doReturn(false)
                .doReturn(true)
                .when(repository)
                .existsByUserId(userId);

        String first = service.getFormattedNotification(userId);
        assertEquals("No notifications", first);

        doReturn("Spy message").when(repository).getLastNotificationText(userId);

        String second = service.getFormattedNotification(userId);
        assertEquals("Last notification: Spy message", second);
    }
}
