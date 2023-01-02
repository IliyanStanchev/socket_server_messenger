package bg.tu_varna.sit.si;
import bg.tu_varna.sit.si.manager.EntityManagerExtender;
import bg.tu_varna.sit.si.models.User;
import bg.tu_varna.sit.si.security.PasswordEncoderWrapper;
import bg.tu_varna.sit.si.server.MessengerSocketServer;
import bg.tu_varna.sit.si.services.UserService;
import bg.tu_varna.sit.si.tasks.UserNotificationsManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        EntityManagerExtender.getEntityManager();
        UserNotificationsManager.startNotifications();

        MessengerSocketServer server = new MessengerSocketServer();
        server.start(8080);
    }
}