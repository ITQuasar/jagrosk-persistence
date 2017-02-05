package org.itquasar.multiverse.jagrosk.persistence;

import java.util.Optional;

/**
 * Created by guilherme on 29/01/17.
 */
public interface JagroskPersistence extends AutoCloseable {

    <I, E extends JagroskEntity<I>> Repository<I, E> repository(Class<E> entityClass);

    default <I, E extends JagroskEntity<I>> Repository<I, E> repository(Class<E> entityClass, boolean oneShot) {
        return oneShot ? repositoryOneShot(entityClass) : repository(entityClass);
    }

    default <I, E extends JagroskEntity<I>> Repository<I, E> repositoryOneShot(Class<E> entityClass) {
        return new OneShotRepository<>(repository(entityClass));
    }

    <R> Transaction<R> transaction();

    default <R> Optional<R> transaction(Transaction.Query<R> transactionQuery) {
        return ((Transaction<R>) this.transaction()).execute(transactionQuery);
    }

    default void transaction(Transaction.Statement transactionStatements) {
        this.transaction().execute(transactionStatements);
    }

    @Override
    void close();
}
