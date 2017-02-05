package org.itquasar.multiverse.jagrosk.persistence.jpa;

import org.itquasar.multiverse.jagrosk.persistence.JagroskEntity;
import org.itquasar.multiverse.jagrosk.persistence.JagroskPersistence;
import org.itquasar.multiverse.jagrosk.persistence.Repository;
import org.itquasar.multiverse.jagrosk.persistence.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by guilherme on 16/10/16.
 */
public class JPAPersistence implements JagroskPersistence, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JPAPersistence.class);

    private static final String DEFAULT_PU_NAME = "default";


    private static final Map<String, EntityManagerFactory> FACTORIES =
            new ConcurrentHashMap<String, EntityManagerFactory>();


    static {
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> FACTORIES.keySet().forEach(
                        (name) -> {
                            EntityManagerFactory emf = FACTORIES.remove(name);
                            LOGGER.warn("Closing {} {}", emf.getClass().getSimpleName(), name);
                            emf.close();
                        }
                )
        ));
    }

    // ---------------------------------------- //
    private final String puName;

    private JPAPersistence(String puName) {
        Objects.requireNonNull(puName, "PU Name must be NOT null");
        this.puName = puName;
    }

    public static JPAPersistence create() {
        return create(DEFAULT_PU_NAME);
    }

    public static JPAPersistence create(String defaultPUName) {
        return new JPAPersistence(defaultPUName);
    }

    private static EntityManager getEntityManager(String name) {
        return getEntityManagerFactory(name).createEntityManager();
    }

    private static EntityManagerFactory getEntityManagerFactory(String name) {
        EntityManagerFactory emf = FACTORIES.get(name);
        if (emf == null) {
            emf = javax.persistence.Persistence.createEntityManagerFactory(name);
            FACTORIES.put(name, emf);
        }
        return emf;
    }

    public EntityManager getEntityManager() {
        return getEntityManager(this.puName);
    }

    @Override
    public void close() {
        EntityManagerFactory emf = this.FACTORIES.remove(this.puName);
        LOGGER.warn("Closing {} {}", emf.getClass().getSimpleName(), this.puName);
        emf.close();
    }

    public <I, E extends JagroskEntity<I>> Repository<I, E> repository(EntityManager entityManager, Class<E> entityClass) {
        return new JPARepository<>(entityManager, entityClass);
    }


    @Override
    public <I, E extends JagroskEntity<I>> Repository<I, E> repository(Class<E> entityClass) {
        return new JPARepository<>(this.getEntityManager(), entityClass);
    }

    @Override
    public <I, E extends JagroskEntity<I>> Repository<I, E> repositoryOneShot(Class<E> entityClass) {
        return new JPAOneShotRepository<>((JPARepository) repository(entityClass));
    }

    @Override
    public <T> Transaction<T> transaction() {
        return new JPATransaction(this);
    }

}
