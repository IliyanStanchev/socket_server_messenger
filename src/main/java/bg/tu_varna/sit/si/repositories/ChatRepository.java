package bg.tu_varna.sit.si.repositories;

import bg.tu_varna.sit.si.models.Chat;

public class ChatRepository extends BaseRepository<Chat> {

    public ChatRepository() {
        super.setClass(Chat.class);
    }
}
