package org.itquasar.multiverse.jagrosk.persistence;

import java.util.List;
import java.util.Optional;

/**
 * Created by guilherme on 16/10/16.
 */
public interface Repository<I, E extends Entity<I>> {

    RepositoryResult<I, E> add(E entity);

    RepositoryResult<I, E> remove(I id);

    RepositoryResult<I, E> update(E entity);

    List<E> listAll();

    Optional<E> findById(I id);

    List<E> findBy(String propertyName, Object value);

    @WorkInProgress
    Optional<E> findBy(RepositoryPredicate predicate);

}
