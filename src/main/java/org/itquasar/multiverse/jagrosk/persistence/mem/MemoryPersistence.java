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

    private final DB db = DBMaker.memoryDB()
            .transactionEnable()
            .make();

    @Override
    public <I, E extends JagroskEntity<I>> Repository<I, E> repository(Class<E> entityClass) {
        ConcurrentMap map = db.hashMap(entityClass.getCanonicalName()).createOrOpen();
        return new MemoryRepository<>(map, entityClass);
    }

    @Override
    public <T> Transaction<T> transaction() {
        return new MemoryTransaction<>(db, this);
    }

}
