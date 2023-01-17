package bg.tu_varna.sit.si.repositories;

import bg.tu_varna.sit.si.managers.EntityManagerExtender;
import bg.tu_varna.sit.si.models.Chat;
import bg.tu_varna.sit.si.models.ChatUser;
import bg.tu_varna.sit.si.requestModels.SearchChatData;

import java.util.ArrayList;
import java.util.List;

public class ChatRepository extends BaseRepository<Chat> {

    public ChatRepository() {
        super.setClass(Chat.class);
    }

    public static List<Chat> getChats(int userId) {

        List<ChatUser> chatUsers = EntityManagerExtender.getEntityManager().createQuery("FROM CHAT_USERS c WHERE c.chat.id IN ( SELECT d.chat.id from CHAT_USERS d WHERE d.user.id  =: userId ) ORDER by c.chat.lastActivity DESC")
                .setParameter("userId", userId)
                .getResultList();

        List<Chat> chats = new ArrayList<>();
        for (ChatUser chatUser : chatUsers) {

            if( chatUser.getUser().getId() == userId)
                continue;

            if( chats.contains(chatUser.getChat()) ) {
                continue;
            }

            chatUser.getChat().setName(chatUser.getUser().getFirstName() + " " + chatUser.getUser().getLastName());
            chats.add(chatUser.getChat());
        }

        return chats;
    }

    public static Chat searchChat(SearchChatData searchChatData) {

        int chatId;

        try {
            chatId = (int) EntityManagerExtender.getEntityManager().createQuery("SELECT c.chat.id FROM CHAT_USERS c WHERE c.user.id =: userId1 AND c.chat.chatType =: chatType AND c.chat.id IN (SELECT cu.chat.id FROM CHAT_USERS cu WHERE cu.user.id =: userId2)")
                    .setParameter("userId1", searchChatData.getCurrentUser().getId())
                    .setParameter("userId2", searchChatData.getOtherUser().getId())
                    .setParameter("chatType", searchChatData.getChatType())
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }

        if(chatId == 0) {
            return null;
        }

        Chat chat = EntityManagerExtender.getEntityManager().find(Chat.class, chatId);

        return chat;
    }
}
