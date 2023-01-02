package bg.tu_varna.sit.si.managers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.function.Consumer;

public class EntityManagerExtender {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();

    public static void executeInsideTransaction(Consumer<EntityManager> action) {

        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();

            action.accept(entityManager);

            entityTransaction.commit();
        } catch (RuntimeException e) {
            entityTransaction.rollback();
            throw e;
        }
    }

    public static EntityManager getEntityManager() {

        return entityManager;
    }

}