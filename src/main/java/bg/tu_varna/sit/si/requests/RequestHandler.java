package bg.tu_varna.sit.si.requests;
import bg.tu_varna.sit.si.models.User;
import bg.tu_varna.sit.si.models.UserNotification;
import bg.tu_varna.sit.si.services.UserNotificationService;
import bg.tu_varna.sit.si.services.UserService;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class RequestHandler {

    public static void handleRequest(SocketRequests.SocketRequestType requestType, ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException, ClassNotFoundException {
        switch (requestType) {
            case LOGIN:
                handleLogin( inputStream, outputStream );
                break;
            case REGISTER:
                handleRegistration( inputStream, outputStream );
                break;
            case GET_NEWS:
                handleGetNews( inputStream, outputStream );
                break;
            case SEND_MESSAGE:
                handleSendMessage( inputStream, outputStream );
                break;
            case GET_MESSAGES:
                break;
            case GET_USERS:
                break;
            case EDIT_PROFILE:
                handleEditProfile( inputStream, outputStream );
                break;
        }
    }

    private static void handleEditProfile(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException, ClassNotFoundException {

        String clientId = (String) inputStream.readObject();
        User user = (User) inputStream.readObject();

        ResponseCodes.ResponseCodeTypes responseCodeType = new UserService().editProfile( user );

        outputStream.writeObject(SocketRequests.SocketRequestType.EDIT_PROFILE);
        outputStream.writeObject(clientId);
        outputStream.writeObject(responseCodeType);
    }

    private static void handleRegistration(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException, ClassNotFoundException {

        String clientId = (String) inputStream.readObject();
        User user = (User) inputStream.readObject();

        UserService userService = new UserService();
        ResponseCodes.ResponseCodeTypes responseCodeType = userService.registerUser(user);

        outputStream.writeObject(SocketRequests.SocketRequestType.REGISTER);
        outputStream.writeObject(clientId);
        outputStream.writeObject(responseCodeType);
    }

    private static void handleGetNews(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException, ClassNotFoundException {

        String clientId = (String) inputStream.readObject();

        List<UserNotification> notifications = new UserNotificationService().getAllNotifications();

        outputStream.writeObject(SocketRequests.SocketRequestType.GET_NEWS);
        outputStream.writeObject(clientId);
        outputStream.writeObject(notifications);
    }

    private static void handleSendMessage(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException, ClassNotFoundException {

        String clientId = (String) inputStream.readObject();
        String message = (String) inputStream.readObject();

        outputStream.writeObject(SocketRequests.SocketRequestType.SEND_MESSAGE);
        outputStream.writeObject(clientId);
        outputStream.writeObject("Message received: " + message);
    }

    private static void handleLogin(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException, ClassNotFoundException {

        String clientId = (String) inputStream.readObject();
        User user = (User) inputStream.readObject();

        UserService userService = new UserService();
        User loggedUser = userService.login(user);

        outputStream.writeObject(SocketRequests.SocketRequestType.LOGIN);
        outputStream.writeObject(clientId);
        outputStream.writeObject(loggedUser);
    }
}

