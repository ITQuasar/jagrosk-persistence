package org.itquasar.multiverse.jagrosk.persistence;

/**
 * Created by guilherme on 16/10/16.
 *
 */
public interface IdentifableEntity<I, M> {

    Class<I> getIdClass();

    I getId();

}
