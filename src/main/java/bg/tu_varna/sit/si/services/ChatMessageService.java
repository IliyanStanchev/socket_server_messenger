package bg.tu_varna.sit.si.services;

import bg.tu_varna.sit.si.models.Chat;
import bg.tu_varna.sit.si.models.ChatMessage;
import bg.tu_varna.sit.si.repositories.ChatMessageRepository;

import java.util.List;

public class ChatMessageService {

    public ChatMessage addNewMessage(ChatMessage message) {

        return new ChatMessageRepository().saveOrUpdate(message);
    }

    public List<ChatMessage> getChatMessages(Chat chat) {

        return new ChatMessageRepository().getChatMessages(chat);
    }
}
