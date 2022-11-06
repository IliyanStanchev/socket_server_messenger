package bg.tu_varna.sit.si.repository;

import bg.tu_varna.sit.si.manager.EntityManagerExtender;
import bg.tu_varna.sit.si.models.User;
import jakarta.persistence.NoResultException;

public class UserRepository extends BaseRepository<User> {

    public UserRepository() {
        super.setClass(User.class);
    }

    public User getUserByUsername(String username) {

        User user;
        try {
            user = (User) EntityManagerExtender.getEntityManager().createQuery("FROM USERS u WHERE  u.username=: username ")
                    .setParameter("username", username)
                    .getSingleResult();

        } catch (NoResultException e) {
            user = null;
        }
        return user;
    }
}
