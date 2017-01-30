package org.itquasar.multiverse.jagrosk.persistence.mem;

import org.itquasar.multiverse.jagrosk.persistence.Entity;
import org.itquasar.multiverse.jagrosk.persistence.JagroskPersistenceProvider;
import org.itquasar.multiverse.jagrosk.persistence.Repository;

/**
 * Created by guilherme on 29/01/17.
 */
public class MemoryPersistenceProvider implements JagroskPersistenceProvider {
    @Override
    public <I, E extends Entity<I>> Repository<I, E> buildRepository(Class<E> entityClass) {
        return new MemoryRepository<>(entityClass);
    }
}
