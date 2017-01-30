package org.itquasar.multiverse.jagrosk.persistence;

/**
 * Created by guilherme on 29/01/17.
 */
@FunctionalInterface
public interface RepositoryAction<T> {

    T execute();
}
