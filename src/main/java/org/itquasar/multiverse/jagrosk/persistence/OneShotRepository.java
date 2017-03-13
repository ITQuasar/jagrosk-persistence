package org.itquasar.multiverse.jagrosk.persistence;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Created by guilherme on 29/01/17.
 */
public class OneShotRepository<I, E extends JagroskEntity<I>> implements Repository<I, E> {

    protected final Repository<I, E> repository;

    public OneShotRepository(Repository<I, E> repository) {
        this.repository = repository;
    }

    protected <T> Optional<T> performAndReturnOptional(Supplier<Optional<T>> action) {
        Optional<T> opt = performAndReturn(action);
        return opt == null ? Optional.empty() : opt;
    }

    protected <T> List<T> performAndReturnList(Supplier<List<T>> action) {
        List<T> lst = performAndReturn(action);
        return lst == null ? Collections.emptyList() : lst;
    }

    protected <T> T performAndReturn(Supplier<T> action) {
        T t = action.get();
        this.close();
        return t;
    }

    @Override
    public Optional<E> add(E entity) {
        return performAndReturnOptional(() -> this.repository.add(entity));
    }

    @Override
    public Optional<E> remove(E entity) {
        return performAndReturnOptional(() -> this.repository.remove(entity));
    }

    @Override
    public Optional<E> remove(I id) {
        return performAndReturnOptional(() -> this.repository.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        return performAndReturnOptional(() -> this.repository.update(entity));
    }

    @Override
    public List<E> listAll() {
        return performAndReturnList(() -> this.repository.listAll());
    }

    @Override
    public Optional<E> findById(I id) {
        return performAndReturnOptional(() -> this.repository.findById(id));
    }

    @Override
    public List<E> findBy(String propertyName, Object value) {
        return performAndReturnList(() -> this.repository.findBy(propertyName, value));
    }

    @Override
    public List<E> findBy(JagroskPredicate predicate) {
        return performAndReturnList(() -> this.repository.findBy(predicate));
    }

    @Override
    public void close() {
        this.repository.close();
    }

    @Override
    public Repository<I, E> unwrap() {
        return this.repository;
    }
}
