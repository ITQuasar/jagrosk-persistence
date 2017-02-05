package org.itquasar.multiverse.jagrosk.persistence;

import java.util.List;
import java.util.Optional;

/**
 * Created by guilherme on 16/10/16.
 */
public interface Repository<I, E extends JagroskEntity<I>> extends AutoCloseable {

    Optional<E> add(E entity);

    default Optional<E> save(E entity){
        return add(entity);
    }

    Optional<E> update(E entity);

    Optional<E> remove(E entity);

    default Optional<E> delete(E entity) {
        return remove(entity);
    }

    List<E> listAll();

    Optional<E> findById(I id);

    List<E> findBy(String propertyName, Object value);

    /**
     * API not defined
     * @param predicate
     * @return
     */
    @Deprecated
    default List<E> findBy(JagroskPredicate predicate) {
        throw new RuntimeException("API not defined yet!");
    }

    @Override
    void close();

    Object unwrap();
}
