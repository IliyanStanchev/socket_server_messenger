package bg.tu_varna.sit.si.services;

import bg.tu_varna.sit.si.models.Chat;
import bg.tu_varna.sit.si.models.ChatMessage;
import bg.tu_varna.sit.si.models.ChatUser;
import bg.tu_varna.sit.si.repositories.ChatMessageRepository;
import bg.tu_varna.sit.si.repositories.ChatRepository;
import bg.tu_varna.sit.si.repositories.ChatUserRepository;
import bg.tu_varna.sit.si.requestModels.ChatViewData;
import bg.tu_varna.sit.si.requestModels.SearchChatData;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChatService {

    public static Chat searchChat(SearchChatData searchChatData) {

        return ChatRepository.searchChat(searchChatData);
    }

    public static Chat createChat(SearchChatData searchChatData) {

        Chat chat = new Chat();
        chat.setChatType(searchChatData.getChatType());
        chat.setName( searchChatData.getOtherUser().getFirstName() + " " + searchChatData.getOtherUser().getLastName() );

        ChatRepository chatRepository = new ChatRepository();
        Chat savedChat = chatRepository.saveOrUpdate(chat);
        if( savedChat == null ) {
            return null;
        }

        ChatUser chatUser = new ChatUser();
        chatUser.setChat(savedChat);
        chatUser.setUser(searchChatData.getCurrentUser());

        ChatUserRepository chatUserRepository = new ChatUserRepository();
        ChatUser savedChatUser = chatUserRepository.saveOrUpdate(chatUser);
        if( savedChatUser == null ) {
            return null;
        }

        chatUser = new ChatUser();
        chatUser.setChat(savedChat);
        chatUser.setUser(searchChatData.getOtherUser());

        savedChatUser = chatUserRepository.saveOrUpdate(chatUser);
        if( savedChatUser == null ) {
            return null;
        }

        return savedChat;
    }

    public static void updateChat(Chat chat) {

        ChatRepository chatRepository = new ChatRepository();

        Chat foundChat = chatRepository.findById(chat.getId());
        if( foundChat == null ) {
            return;
        }

        foundChat.setName(chat.getName());
        foundChat.setChatType(chat.getChatType());
        foundChat.setLastActivity(chat.getLastActivity());

        Chat savedChat = chatRepository.saveOrUpdate(foundChat);
        if( savedChat == null ) {
            return;
        }
    }

    public List<ChatViewData> getChats(int userId) {

        List<Chat> chats =  ChatRepository.getChats(userId);
        List<ChatViewData> chatViewData = new ArrayList<>();

        ChatMessageRepository chatMessageRepository = new ChatMessageRepository();

        for(Chat chat : chats) {

            ChatViewData chatView = new ChatViewData();
            chatView.setChat(chat);

            ChatMessage lastMessage = chatMessageRepository.getLastMessage(chat.getId());

            if( lastMessage == null ) {
                chatView.setLastMessageData("");
                chatViewData.add(chatView);
                continue;

            }

            if( lastMessage.getSender().getId() == userId ) {
                chatView.setLastMessageData("You: " + lastMessage.getContent() + " " + lastMessage.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            } else {
                chatView.setLastMessageData(lastMessage.getSender().getFirstName() + ": " + lastMessage.getContent() + " " + lastMessage.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            }

            chatViewData.add(chatView);
        }

        return chatViewData;
    }
}
