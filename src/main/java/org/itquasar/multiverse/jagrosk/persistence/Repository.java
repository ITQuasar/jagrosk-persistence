package org.itquasar.multiverse.jagrosk.persistence;

import java.util.List;

/**
 * Created by guilherme on 16/10/16.
 */
public interface Repository<I, M extends IdentifableEntity<I, M>> extends AutoCloseable {

    List<M> listAll();

    // FIXME: use option
    M findById(I id);

    void close() ;
}
