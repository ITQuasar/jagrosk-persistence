package org.itquasar.multiverse.jagrosk.persistence;

import java.util.function.Function;

/**
 * Created by guilherme on 29/01/17.
 */
public interface JagroskPersistence {

    <I, E extends JagroskEntity<I>> Repository<I, E > repository(Class<E> entityClass);

    default <I, E extends JagroskEntity<I>> Repository<I, E > repository(Class<E> entityClass, boolean oneShot){
        return oneShot? repository(entityClass): repositoryOneShot(entityClass);
    }

    default <I, E extends JagroskEntity<I>> Repository<I, E > repositoryOneShot(Class<E> entityClass){
        return new OneShotRepository<>(repository(entityClass));
    }

    <T> Transaction<T> transaction();

    default <T> T transaction(Function<Transaction<T>, T> transaction){
        return transaction.apply(this.transaction());
    }
}
