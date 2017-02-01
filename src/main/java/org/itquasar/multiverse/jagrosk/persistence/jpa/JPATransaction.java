package org.itquasar.multiverse.jagrosk.persistence.jpa;

import org.itquasar.multiverse.jagrosk.persistence.Transaction;

import javax.persistence.EntityManager;

/**
 * Created by guilherme on 31/01/17.
 */
public class JPATransaction<R> extends Transaction<R> {

    private final EntityManager entityManager;

    public JPATransaction(JPAPersistence persistence) {
        super(persistence);
        this.entityManager = persistence.getEntityManager();
    }

    @Override
    protected void begin() {
        this.entityManager.getTransaction().begin();
    }

    @Override
    protected boolean isActive() {
        return this.entityManager.getTransaction().isActive();
    }

    @Override
    protected void commit() throws Exception {
        this.entityManager.getTransaction().commit();
    }

    @Override
    protected void rollback() {
        this.entityManager.getTransaction().rollback();
    }

    @Override
    protected void finalizeResources() {
        this.entityManager.close();
    }
}
