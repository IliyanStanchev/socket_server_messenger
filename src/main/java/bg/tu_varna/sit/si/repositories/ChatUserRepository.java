package bg.tu_varna.sit.si.repositories;

import bg.tu_varna.sit.si.models.ChatUser;

public class ChatUserRepository extends BaseRepository<ChatUser> {

    public ChatUserRepository() {
        super.setClass(ChatUser.class);
    }

}
