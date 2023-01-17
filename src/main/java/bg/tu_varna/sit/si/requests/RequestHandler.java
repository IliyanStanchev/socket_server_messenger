package bg.tu_varna.sit.si.requests;
import bg.tu_varna.sit.si.enumerables.ChatType;
import bg.tu_varna.sit.si.managers.EntityManagerExtender;
import bg.tu_varna.sit.si.managers.SystemMessagesManager;
import bg.tu_varna.sit.si.models.Chat;
import bg.tu_varna.sit.si.models.ChatMessage;
import bg.tu_varna.sit.si.models.User;
import bg.tu_varna.sit.si.models.UserNotification;
import bg.tu_varna.sit.si.requestModels.ChatViewData;
import bg.tu_varna.sit.si.requestModels.SearchChatData;
import bg.tu_varna.sit.si.services.ChatMessageService;
import bg.tu_varna.sit.si.services.ChatService;
import bg.tu_varna.sit.si.services.UserNotificationService;
import bg.tu_varna.sit.si.services.UserService;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
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
                handleGetMessages( inputStream, outputStream );
                break;
            case GET_USERS:
                handleGetUsers( inputStream, outputStream );
                break;
            case EDIT_PROFILE:
                handleEditProfile( inputStream, outputStream );
                break;
            case GET_CHATS:
                handleGetChats( inputStream, outputStream );
                break;
            case SEARCH_CHAT:
                handleSearchChat( inputStream, outputStream );
                break;
        }
    }

    private static void handleGetUsers(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException, ClassNotFoundException {

        String clientId = (String) inputStream.readObject();
        int currentUserId = (int) inputStream.readObject();

        List<User> users = UserService.getUsers(currentUserId);

        outputStream.writeObject(SocketRequests.SocketRequestType.GET_USERS);
        outputStream.writeObject(clientId);
        outputStream.writeObject( users );
    }

    private static void handleSearchChat(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException, ClassNotFoundException {

        String clientId = (String) inputStream.readObject();
        SearchChatData searchChatData = (SearchChatData) inputStream.readObject();

        if( searchChatData.getOtherUser().getId() == 1 ){
            searchChatData.setChatType(ChatType.SYSTEM);
        }

        EntityManagerExtender.beginTransaction();

        Chat chat = ChatService.searchChat(searchChatData);
        if( chat == null )
        {
            chat = ChatService.createChat(searchChatData);
        }

        if( chat == null )
        {
            EntityManagerExtender.rollbackTransaction();
            return;
        }

        EntityManagerExtender.commitTransaction();

        outputStream.writeObject(SocketRequests.SocketRequestType.SEARCH_CHAT);
        outputStream.writeObject(clientId);
        outputStream.writeObject( chat );
    }

    private static void handleGetChats(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException, ClassNotFoundException {

        String clientId = (String) inputStream.readObject();
        int userId = (int) inputStream.readObject();

        List<ChatViewData> chats = new ChatService().getChats(userId);

        outputStream.writeObject(SocketRequests.SocketRequestType.GET_CHATS);
        outputStream.writeObject(clientId);
        outputStream.writeObject( chats );
    }

    private static void handleGetMessages(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException, ClassNotFoundException {

        String clientId = (String) inputStream.readObject();
        Chat chat = (Chat) inputStream.readObject();

        List<ChatMessage> messages = new ChatMessageService().getChatMessages( chat );

        outputStream.writeObject(SocketRequests.SocketRequestType.GET_MESSAGES);
        outputStream.writeObject(clientId);
        outputStream.writeObject( messages );
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
        ChatMessage message = (ChatMessage) inputStream.readObject();

        ChatMessageService chatMessageService = new ChatMessageService();
        chatMessageService.addNewMessage(message);

        message.getChat().setLastActivity(LocalDateTime.now());
        ChatService.updateChat(message.getChat());

        outputStream.writeObject(SocketRequests.SocketRequestType.SEND_MESSAGE);
        outputStream.writeObject(clientId);

        if( message.getChat().getChatType() == ChatType.SYSTEM) {
            ChatMessage answerMessage = SystemMessagesManager.getSystemMessage(message);
            chatMessageService.addNewMessage(answerMessage);
            outputStream.writeObject(answerMessage);
        }
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

