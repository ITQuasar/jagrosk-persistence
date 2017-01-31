package org.itquasar.multiverse.jagrosk.persistence;

import java.util.Optional;
import java.util.function.Function;

/**
 * Created by guilherme on 30/01/17.
 */
public abstract class Transaction<R> {

    private final JagroskPersistenceProvider provider;

    public Transaction(JagroskPersistenceProvider provider) {
        this.provider = provider;
    }

    protected abstract void begin();

    protected abstract void commit() throws Exception;

    protected abstract void rollback();

    protected abstract void finalizeResources();

    public <I, E extends Entity<I>> Repository<I,E> getRepository(Class<E> entityClass){
        return provider.buildRepository(entityClass, false);
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
