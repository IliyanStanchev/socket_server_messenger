package bg.tu_varna.sit.si.repositories;

import bg.tu_varna.sit.si.managers.EntityManagerExtender;
import bg.tu_varna.sit.si.models.Chat;
import bg.tu_varna.sit.si.models.ChatMessage;

import java.util.List;

public class ChatMessageRepository extends BaseRepository<ChatMessage> {

    public ChatMessageRepository() {
        super.setClass(ChatMessage.class);
    }

    public List<ChatMessage> getChatMessages(Chat chat) {

        return EntityManagerExtender.getEntityManager().createQuery("FROM CHAT_MESSAGES c WHERE c.chat.id=: chatId ORDER BY c.creationDate ASC")
                .setParameter("chatId", chat.getId())
                .getResultList();
    }
}
