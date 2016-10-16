package org.itquasar.multiverse.jagrosk.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by guilherme on 16/10/16.
 */
public class Persistence implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Persistence.class);

    private static final String DEFAULT_PU_NAME = "default";

    public static Persistence create(){
        return create(DEFAULT_PU_NAME);
    }

    public static Persistence create(String defaultPUName){
        return new Persistence(defaultPUName);
    }

    ///////
    ///////
    ///////

    private final Map<String, EntityManagerFactory> factories =
            new ConcurrentHashMap<String, EntityManagerFactory>();
    private final String defaultPuName;

    private Persistence(String defaultPUName) {
        this.defaultPuName = defaultPUName;
    }

    public EntityManager getEntityManager(){
        return getEntityManager(this.defaultPuName);
    }

    public EntityManager getEntityManager(String name){
        return getEntityManagerFactory(name).createEntityManager();
    }

    public EntityManagerFactory getEntityManagerFactory(String name){
        EntityManagerFactory emf = factories.get(name);
        if(emf == null) {
            emf = javax.persistence.Persistence.createEntityManagerFactory(name);
            factories.put(name, emf);
        }
        return emf;
    }

    public void close() {
        for(String name: this.factories.keySet()){
            EntityManagerFactory emf = this.factories.get(name);
            LOGGER.warn("Closing {} {}", emf.getClass().getSimpleName(), name);
            emf.close();
        }
    }
}