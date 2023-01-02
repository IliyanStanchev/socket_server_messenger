package bg.tu_varna.sit.si.tasks;

import bg.tu_varna.sit.si.services.UserNotificationService;

import java.util.Timer;
import java.util.TimerTask;

public class UserNotificationsManager extends TimerTask {

    UserNotificationService notificationService = new UserNotificationService();

    @Override
    public void run() {

        notificationService.addNewNotification();
    }

    public static void startNotifications() {
        Timer timer = new Timer();
        timer.schedule(new UserNotificationsManager(), 0, 10000);
    }
}
