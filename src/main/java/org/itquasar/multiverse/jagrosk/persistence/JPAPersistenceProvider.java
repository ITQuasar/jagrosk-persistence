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
public class JPAPersistenceProvider implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JPAPersistenceProvider.class);

    private static final String DEFAULT_PU_NAME = "default";

    public static JPAPersistenceProvider create(){
        return create(DEFAULT_PU_NAME);
    }

    public static JPAPersistenceProvider create(String defaultPUName){
        return new JPAPersistenceProvider(defaultPUName);
    }

//    public <M> Repository getRepository(Class<M> modelClass){
//        return new GenericRepository(modelClass, getEntityManager());
//    }
//
//    public <M> Repository getOneShotRepository(Class<M> modelClass){
//        return new OneShotRepository(
//                new GenericRepository(modelClass, getEntityManager())
//        );
//    }

    ///////
    ///////
    ///////

    private final Map<String, EntityManagerFactory> factories =
            new ConcurrentHashMap<String, EntityManagerFactory>();
    private final String defaultPuName;

    private JPAPersistenceProvider(String defaultPUName) {
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
