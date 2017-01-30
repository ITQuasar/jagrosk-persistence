package org.itquasar.multiverse.jagrosk.persistence;

/**
 * Created by guilherme on 29/01/17.
 */
@FunctionalInterface
public interface JagroskPersistenceProvider {

    <I, E extends Entity<I>> Repository<I, E > buildRepository(Class<E> entityClass);

    default <I, E extends Entity<I>> Repository<I, E > buildRepository(Class<E> entityClass, boolean oneShot){
        return oneShot? buildRepository(entityClass): buildOneShotRepository(entityClass);
    }

    default <I, E extends Entity<I>> Repository<I, E > buildOneShotRepository(Class<E> entityClass){
        return new OneShotRepository<I, E>(buildRepository(entityClass));
    }
}
