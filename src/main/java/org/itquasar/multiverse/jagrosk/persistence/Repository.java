package org.itquasar.multiverse.jagrosk.persistence;

import java.util.List;
import java.util.Optional;

/**
 * Created by guilherme on 16/10/16.
 */
public interface Repository<I, E extends Entity<I>> extends AutoCloseable {

    Optional<E> add(E entity);

    Optional<E> update(E entity);

    Optional<E> remove(E entity);

    List<E> listAll();

    Optional<E> findById(I id);

    List<E> findBy(String propertyName, Object value);

    @WorkInProgress
    default List<E> findBy(RepositoryPredicate predicate) {
        throw new RuntimeException("API not defined yet!");
    }

    @Override
    void close();
}
