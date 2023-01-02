package bg.tu_varna.sit.si.repositories;
import bg.tu_varna.sit.si.managers.EntityManagerExtender;
import bg.tu_varna.sit.si.models.UserNotification;

import java.util.ArrayList;
import java.util.List;

public class UserNotificationRepository extends BaseRepository<UserNotification> {

    public UserNotificationRepository() {
        super.setClass(UserNotification.class);
    }

    public List<UserNotification> getNotificationsOrderedByDate() {

        List< UserNotification> notifications = new ArrayList<>();

        try {

            notifications = EntityManagerExtender.getEntityManager().createQuery("FROM USER_NOTIFICATIONS u ORDER BY u.creationDate DESC")
                    .getResultList();
        } catch (Exception e){
            return new ArrayList<>();
        }

        return notifications;
    }

    public long getLastNotificationId() {

        long singleResult = 0L;
        try {
            singleResult = (long) EntityManagerExtender.getEntityManager().createQuery("SELECT IFNULL( MAX( id ), 0 ) FROM USER_NOTIFICATIONS").getSingleResult();

        } catch (Exception e){
            return 0L;
        }

        return singleResult;
    }
}
