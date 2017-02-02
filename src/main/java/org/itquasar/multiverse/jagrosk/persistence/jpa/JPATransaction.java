package org.itquasar.multiverse.jagrosk.persistence.jpa;

import org.itquasar.multiverse.jagrosk.persistence.JagroskEntity;
import org.itquasar.multiverse.jagrosk.persistence.Repository;
import org.itquasar.multiverse.jagrosk.persistence.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Created by guilherme on 31/01/17.
 */
public class JPATransaction<R> extends Transaction<R> {

    private final EntityManager entityManager;
    private final EntityTransaction transaction;

    private final RepositoryProvider repositoryProvider;

    public JPATransaction(JPAPersistence persistence) {
        super(persistence);
        this.entityManager = persistence.getEntityManager();
        this.transaction = this.entityManager.getTransaction();
        this.repositoryProvider = new RepositoryProvider() {
            @Override
            public <I, E extends JagroskEntity<I>> Repository<I, E> get(Class<E> entityClass) {
                return persistence.repository(entityManager, entityClass);
            }
        };
    }

    @Override
    protected void begin() {
        this.transaction.begin();
    }

    @Override
    protected boolean isActive() {
        return this.transaction.isActive();
    }

    @Override
    protected void commit() throws Throwable {
        this.transaction.commit();
    }

    @Override
    protected void rollback() {
        this.transaction.rollback();
    }

    @Override
    protected void finalizeResources() {
        this.entityManager.close();
    }

    @Override
    protected RepositoryProvider getRepositoryProvider() {
        return repositoryProvider;
    }
}
