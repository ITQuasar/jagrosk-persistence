package org.itquasar.multiverse.jagrosk.persistence.mem;

import org.itquasar.multiverse.jagrosk.persistence.JagroskEntity;
import org.itquasar.multiverse.jagrosk.persistence.JagroskPersistence;
import org.itquasar.multiverse.jagrosk.persistence.Repository;
import org.itquasar.multiverse.jagrosk.persistence.Transaction;

/**
 * Created by guilherme on 29/01/17.
 */
public class MemoryPersistence implements JagroskPersistence {
    @Override
    public <I, E extends JagroskEntity<I>> Repository<I, E> repository(Class<E> entityClass) {
        return new MemoryRepository<>(entityClass);
    }

    @Override
    public <T> Transaction<T> transaction() {
        return new Transaction<T>(this) {

            private boolean isActive = false;

            @Override
            protected void begin() {
                this.isActive = true;
            }

            @Override
            protected boolean isActive() {
                return isActive;
            }

            @Override
            protected void commit() throws Exception {
                this.isActive = false;
            }

            @Override
            protected void rollback() {
                this.isActive = false;
            }

            @Override
            protected void finalizeResources() {
                this.isActive = false;
            }
        };
    }
}
