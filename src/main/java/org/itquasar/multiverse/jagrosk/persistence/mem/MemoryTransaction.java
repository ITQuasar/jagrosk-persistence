package org.itquasar.multiverse.jagrosk.persistence.mem;

import org.itquasar.multiverse.jagrosk.persistence.JagroskEntity;
import org.itquasar.multiverse.jagrosk.persistence.Repository;
import org.itquasar.multiverse.jagrosk.persistence.Transaction;
import org.mapdb.DB;

import java.util.Optional;

/**
 * Created by guilherme on 02/02/17.
 */
class MemoryTransaction<T> extends Transaction<T> {

    private final DB db;
    private final RepositoryProvider repositoryProvider = new RepositoryProvider() {
        @Override
        public <I, E extends JagroskEntity<I>> Repository<I, E> repository(Class<E> entityClass) {
            return getPersistence().repository(entityClass, false);
        }
    };
    private boolean active = false;

    public MemoryTransaction(DB db, MemoryPersistence persistence) {
        super(persistence);
        this.db = db;
    }

    @Override
    protected void begin() {
    }

    @Override
    protected boolean isActive() {
        return active;
    }

    @Override
    protected void commit() throws Throwable {
        db.commit();
        active = false;
    }

    @Override
    protected void rollback() {
        db.rollback();
        active = false;
    }

    @Override
    protected void finalizeResources() {
    }

    @Override
    public Optional<T> execute(Query<T> query) {
        synchronized (db) {
            return super.execute(query);
        }
    }

    @Override
    protected RepositoryProvider getRepositoryProvider() {
        return repositoryProvider;
    }
}
