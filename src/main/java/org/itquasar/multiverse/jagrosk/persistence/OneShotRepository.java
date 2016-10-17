package org.itquasar.multiverse.jagrosk.persistence;

import java.util.List;

/**
 * Created by guilherme on 16/10/16.
 */
public class OneShotRepository<I, M extends IdentifableEntity<I, M>> implements Repository<I, M> {

    private final Repository<I, M> repository;

    public OneShotRepository(Repository<I, M> repository) {
        this.repository = repository;
    }

    @Override
    public List<M> listAll() {
        return closeAndReturn(repository.listAll());
    }

    @Override
    public M findById(I id) {
        return closeAndReturn(repository.findById(id));
    }

    @Override
    public void close() {
        repository.close();
    }

    private <T> T closeAndReturn(T value){
        this.close();
        return value;
    }

}
