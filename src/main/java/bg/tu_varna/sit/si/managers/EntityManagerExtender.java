package bg.tu_varna.sit.si.managers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.function.Consumer;

public class EntityManagerExtender {

    private static final EntityManagerFactory entityManagerFactory;
    private static final ThreadLocal<EntityManager> threadEntityManager;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        threadEntityManager = new ThreadLocal<EntityManager>();
        Thread closeHook = new Thread(() -> {
            closeEntityManager();
            closeEntityManagerFactory();
        });

        Runtime.getRuntime().addShutdownHook(closeHook);
    }

    public static EntityManager getEntityManager() {
        EntityManager entityManager = EntityManagerExtender.threadEntityManager.get();

        if (entityManager == null) {
            entityManager = entityManagerFactory.createEntityManager();
            EntityManagerExtender.threadEntityManager.set(entityManager);
        }
        return entityManager;
    }

    public static void closeEntityManager() {
        EntityManager entityManager = threadEntityManager.get();
        if (entityManager != null) {
            entityManager.close();
            threadEntityManager.set(null);
        }
    }

    public static void closeEntityManagerFactory() {
        entityManagerFactory.close();
    }

    public static void beginTransaction() {
        getEntityManager().getTransaction().begin();
    }

    public static void rollbackTransaction() {
        getEntityManager().getTransaction().rollback();
    }

    public static void commitTransaction() {
        getEntityManager().getTransaction().commit();
    }

    public static void executeInsideTransaction(Consumer<EntityManager> action) {

        EntityTransaction entityTransaction = threadEntityManager.get().getTransaction();
        try {
            entityTransaction.begin();

            action.accept(threadEntityManager.get());

            entityTransaction.commit();
        } catch (RuntimeException e) {
            entityTransaction.rollback();
            throw e;
        }
    }



}