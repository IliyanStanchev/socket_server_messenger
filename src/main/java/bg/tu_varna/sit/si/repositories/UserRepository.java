package bg.tu_varna.sit.si.repositories;

import bg.tu_varna.sit.si.manager.EntityManagerExtender;
import bg.tu_varna.sit.si.models.User;
import jakarta.persistence.NoResultException;

public class UserRepository extends BaseRepository<User> {

    public UserRepository() {
        super.setClass(User.class);
    }

    public User getUserByEmail(String email) {

        User user;
        try {
            user = (User) EntityManagerExtender.getEntityManager().createQuery("FROM USERS u WHERE  u.email=: email ")
                    .setParameter("email", email)
                    .getSingleResult();

        } catch (NoResultException e) {
            user = null;
        }
        return user;
    }

    public User getUserByPhoneNumber(String phoneNumber) {

        User user;
        try {
            user = (User) EntityManagerExtender.getEntityManager().createQuery("FROM USERS u WHERE  u.phoneNumber=: phoneNumber ")
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();

        } catch (NoResultException e) {
            user = null;
        }
        return user;
    }
}
