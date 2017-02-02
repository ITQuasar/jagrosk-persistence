package org.itquasar.multiverse.jagrosk.persistence.mem;

import org.itquasar.multiverse.jagrosk.persistence.JagroskEntity;
import org.itquasar.multiverse.jagrosk.persistence.JagroskPersistence;
import org.itquasar.multiverse.jagrosk.persistence.Repository;
import org.itquasar.multiverse.jagrosk.persistence.Transaction;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by guilherme on 29/01/17.
 */
public class MemoryPersistence implements JagroskPersistence {

    private final DB db = DBMaker.memoryDB().make();

    @Override
    public <I, E extends JagroskEntity<I>> Repository<I, E> repository(Class<E> entityClass) {
        ConcurrentMap map = db.hashMap(entityClass.getCanonicalName()).createOrOpen();
        return new MemoryRepository<>(map, entityClass);
    }

    @Override
    public <T> Transaction<T> transaction() {
        // FIXME
        return new Transaction<T>(this) {

            private final RepositoryProvider repositoryProvider = new RepositoryProvider() {
                @Override
                public <I, E extends JagroskEntity<I>> Repository<I, E> get(Class<E> entityClass) {
                    return getPersistence().repository(entityClass, false);
                }
            };

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

            @Override
            protected RepositoryProvider getRepositoryProvider() {
                return repositoryProvider;
            }
        };
    }
}
