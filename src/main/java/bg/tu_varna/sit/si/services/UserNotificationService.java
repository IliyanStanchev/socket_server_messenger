package bg.tu_varna.sit.si.services;
import bg.tu_varna.sit.si.models.UserNotification;
import bg.tu_varna.sit.si.repositories.UserNotificationRepository;
import de.svenjacobs.loremipsum.LoremIpsum;

import java.time.LocalDateTime;
import java.util.List;

public class UserNotificationService {

    public List<UserNotification> getAllNotifications() {

        UserNotificationRepository notificationRepository = new UserNotificationRepository();
        return notificationRepository.getNotificationsOrderedByDate();

    }

    public void addNewNotification() {

        UserNotificationRepository notificationRepository = new UserNotificationRepository();

        UserNotification notification = new UserNotification();

        final long lastNotificationId = notificationRepository.getLastNotificationId();
        notification.setTitle("Notification " + (lastNotificationId + 1));
        notification.setCreationDate(LocalDateTime.now());

        LoremIpsum loremIpsum = new LoremIpsum();
        notification.setContent(loremIpsum.getWords((int) ( ( lastNotificationId % 10 ) * 10 + 10 )));

        notificationRepository.saveOrUpdate(notification);
    }
}
