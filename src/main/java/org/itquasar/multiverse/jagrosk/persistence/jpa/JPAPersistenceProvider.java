package org.itquasar.multiverse.jagrosk.persistence.jpa;

import org.itquasar.multiverse.jagrosk.persistence.Entity;
import org.itquasar.multiverse.jagrosk.persistence.JagroskPersistenceProvider;
import org.itquasar.multiverse.jagrosk.persistence.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by guilherme on 16/10/16.
 */
public class JPAPersistenceProvider implements JagroskPersistenceProvider, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JPAPersistenceProvider.class);

    private static final String DEFAULT_PU_NAME = "default";


    private static final Map<String, EntityManagerFactory> FACTORIES =
            new ConcurrentHashMap<String, EntityManagerFactory>();
    // ---------------------------------------- //
    private final String puName;

    private JPAPersistenceProvider(String puName) {
        this.puName = puName;
    }

    public static JPAPersistenceProvider create() {
        return create(DEFAULT_PU_NAME);
    }

    public static JPAPersistenceProvider create(String defaultPUName) {
        return new JPAPersistenceProvider(defaultPUName);
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

    public static void closeAll() {
        for (String name : FACTORIES.keySet()) {
            EntityManagerFactory emf = FACTORIES.remove(name);
            LOGGER.warn("Closing {} {}", emf.getClass().getSimpleName(), name);
            emf.close();
        }
    }

    public EntityManager getEntityManager() {
        return getEntityManager(this.puName);
    }

    public void close() {
        EntityManagerFactory emf = this.FACTORIES.remove(this.puName);
        LOGGER.warn("Closing {} {}", emf.getClass().getSimpleName(), this.puName);
        emf.close();
    }


    @Override
    public <I, E extends Entity<I>> Repository<I, E> buildRepository(Class<E> entityClass) {
        return new JPARepository<>(this.getEntityManager(), entityClass);
    }

    @Override
    public <I, E extends Entity<I>> Repository<I, E> buildOneShotRepository(Class<E> entityClass) {
        return new JPAOneShotRepository<>((JPARepository) buildRepository(entityClass));
    }
}
