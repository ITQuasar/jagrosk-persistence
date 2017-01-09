package org.itquasar.multiverse.jagrosk.persistence;

/**
 * Created by guilherme on 08/01/17.
 */
public class RepositoryResult<I, E extends Entity<I>> {

    private static final String EMPTY = "";

    private final Repository<I, E> repository;
    private final boolean success;
    private final String message;

    public static <I, E extends Entity<I>> RepositoryResult<I, E> success(Repository<I, E> repository) {
        return success(repository, EMPTY);
    }

    public static <I, E extends Entity<I>> RepositoryResult<I, E> success(Repository<I, E> repository, String message) {
        return new RepositoryResult<>(repository, true, message);
    }

    public static <I, E extends Entity<I>> RepositoryResult<I, E> failure(Repository<I, E> repository, String message) {
        return new RepositoryResult<>(repository, false, message);
    }

    private RepositoryResult(Repository<I, E> repository, boolean success, String message) {
        this.repository = repository;
        this.success = success;
        this.message = message != null ? message : EMPTY;
    }

    public Repository<I, E> getRepository() {
        return repository;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "RepositoryResult#success?" + success + "[" + message + ']';
    }
}
