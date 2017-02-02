package org.itquasar.multiverse.jagrosk.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by guilherme on 30/01/17.
 */
public abstract class Transaction<R> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Transaction.class);

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

    protected abstract RepositoryProvider getRepositoryProvider();

    public void execute(Statement statement) {
        execute((repoProvider) -> {
            statement.execute(repoProvider);
            return null;
        });
    }

    public Optional<R> execute(Query<R> query) {
        R r = null;
        try {
            LOGGER.trace("Begin [{}ms]", System.currentTimeMillis() / 1000);
            this.begin();

            LOGGER.trace("Execute [{}ms]", System.currentTimeMillis() / 1000);
            r = query.execute(getRepositoryProvider());

            LOGGER.trace("Commit [{}ms]", System.currentTimeMillis() / 1000);
            this.commit();
        } catch (Exception ex) {
            LOGGER.trace("Rollback: {} -> {} [{}ms]",
                    ex.getCause(), ex.getMessage(), System.currentTimeMillis());
            LOGGER.error("Error commiting transaction", ex);
            this.rollback();
        } finally {
            LOGGER.trace("Finalize resources [{}ms]", System.currentTimeMillis());
            this.finalizeResources();
            LOGGER.trace("End [{}ms]", System.currentTimeMillis());
        }
        return Optional.ofNullable(r);
    }

    @FunctionalInterface
    public interface RepositoryProvider {

        <I, E extends JagroskEntity<I>> Repository<I, E> get(Class<E> entityClass);
    }

    @FunctionalInterface
    public interface Query<R> {

        R execute(RepositoryProvider repositoryProvider);
    }

    @FunctionalInterface
    public interface Statement {

        void execute(RepositoryProvider repositoryProvider);
    }
}
