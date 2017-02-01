package org.itquasar.multiverse.jagrosk.persistence;

import java.util.Optional;
import java.util.function.Function;

/**
 * Created by guilherme on 30/01/17.
 */
public abstract class Transaction<R> {

    private final JagroskPersistence persistence;

    public Transaction(JagroskPersistence persistence) {
        this.persistence = persistence;
    }

    public JagroskPersistence getPersistence() {
        return persistence;
    }

    protected abstract void begin();

    protected abstract boolean isActive();

    protected abstract void commit() throws Exception;

    protected abstract void rollback();

    protected abstract void finalizeResources();

    public <I, E extends JagroskEntity<I>> Repository<I,E> getRepository(Class<E> entityClass){
        return persistence.repository(entityClass, false);
    }

    public Optional<R> perform(Function<Transaction<R>, R> body){
        R r = null;
        try {
            this.begin();
            r = body.apply(this);
            this.commit();
        } catch (Exception ex){
            this.rollback();
        } finally {
            this.finalizeResources();
        }
        return Optional.ofNullable(r);
    }
}
