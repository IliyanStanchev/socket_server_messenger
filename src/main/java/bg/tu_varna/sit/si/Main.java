package bg.tu_varna.sit.si;

import bg.tu_varna.sit.si.manager.EntityManagerExtender;
import bg.tu_varna.sit.si.models.User;
import bg.tu_varna.sit.si.repository.UserRepository;
import bg.tu_varna.sit.si.server.MessengerSocketServer;
import jakarta.persistence.EntityManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        EntityManager entityManager = EntityManagerExtender.getEntityManager();

        UserRepository userRepository = new UserRepository();

        User user = new User("Iliyan", "Stanchev");
        User updatedUser = userRepository.saveOrUpdate(user);

        MessengerSocketServer server = new MessengerSocketServer();
        server.start(8080);
    }
}