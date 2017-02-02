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
    // TODO: Add StatusListenner
    private Status status = Status.NOT_STARTED;
    // TODO: Add PhaseListenner
    private Phase phase = Phase.PRE_BEGIN;

    public Transaction(JagroskPersistence persistence) {
        this.persistence = persistence;
    }

    public Phase getPhase() {
        return phase;
    }

    public Status getStatus() {
        return status;
    }

    public JagroskPersistence getPersistence() {
        return persistence;
    }

    protected abstract void begin();

    protected abstract boolean isActive();

    protected abstract void commit() throws Throwable;

    protected abstract void rollback();

    protected abstract void finalizeResources();

    protected abstract RepositoryProvider getRepositoryProvider();

    // FIXME: should return status and value???
    public void execute(Statement statement) {
        execute((repoProvider) -> {
            statement.execute(repoProvider);
            return null;
        });
    }

    // FIXME: should return status and value???
    public Optional<R> execute(Query<R> query) {
        R r = null;
        try {
            LOGGER.trace("Begin [{}ms]", System.currentTimeMillis() / 1000);
            this.phase = Phase.BEGIN;
            this.begin();

            LOGGER.trace("Execute [{}ms]", System.currentTimeMillis() / 1000);
            this.phase = Phase.EXECUTION;
            r = query.execute(getRepositoryProvider());

            LOGGER.trace("Commit [{}ms]", System.currentTimeMillis() / 1000);
            this.phase = Phase.COMMIT;
            this.commit();
            this.status = Status.COMMITED;
        } catch (Throwable ex) {
            LOGGER.trace("Rollback: {} -> {} [{}ms]",
                    ex.getCause(), ex.getMessage(), System.currentTimeMillis());
            LOGGER.error("Error commiting transaction", ex);
            this.phase = Phase.ROLL_BACK;
            this.rollback();
            this.status = Status.ROLLED_BACK;
        } finally {
            LOGGER.trace("Finalize resources [{}ms]", System.currentTimeMillis());
            this.phase = Phase.FINALIZATION;
            this.finalizeResources();
            LOGGER.trace("End [{}ms]", System.currentTimeMillis());
            this.phase = Phase.POST_FINALIZATION;
        }
        return Optional.ofNullable(r);
    }

    public enum Status {
        NOT_STARTED, COMMITED, ROLLED_BACK
    }

    public enum Phase {
        PRE_BEGIN, BEGIN, EXECUTION, COMMIT, ROLL_BACK, FINALIZATION, POST_FINALIZATION;
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
