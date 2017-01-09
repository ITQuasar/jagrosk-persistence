package org.itquasar.multiverse.jagrosk.persistence;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by guilherme on 08/01/17.
 */
public class MemoryRepository<I, E extends Entity<I>> implements Repository<I, E> {

    private final Map<I, E> storage = new ConcurrentHashMap<I, E>();

    private final Class<E> entityClass;

    public MemoryRepository(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public RepositoryResult<I, E> add(E entity) {
        this.storage.put(entity.getId(), entity);
        return RepositoryResult.success(this);
    }

    @Override
    public RepositoryResult<I, E> remove(I id) {
        return this.storage.remove(this.storage.get(id)).getId().equals(id)
                ? RepositoryResult.success(this)
                : RepositoryResult.failure(this, "Error removing entity with given id " + id);
    }

    @Override
    public RepositoryResult<I, E> update(E entity) {
        return this.storage.replace(entity.getId(), entity) != null
                ? RepositoryResult.success(this)
                : RepositoryResult.failure(this, "Error updating given entity #" + entity.getId());
    }

    @Override
    public List<E> listAll() {
        return new LinkedList<>(this.storage.values());
    }

    @Override
    public Optional<E> findById(I id) {
        return Optional.ofNullable(this.storage.get(id));
    }

    @Override
    public List<E> findBy(String propertyName, Object value) {
        Optional<Method> reader = null;
        try {
            reader = Arrays.stream(
                    Introspector.getBeanInfo(entityClass).getPropertyDescriptors()
            ).filter((prop) -> prop.getName().equals(propertyName))
                    .findFirst()
                    .map(PropertyDescriptor::getReadMethod);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        if (reader.isPresent()) {
            Method m = reader.get();
            return this.storage.values().stream().filter(
                    (obj) -> {
                        try {
                            return m.invoke(obj).equals(value);
                        } catch (IllegalAccessException | InvocationTargetException ex) {
                            return false;
                        }
                    }
            ).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<E> findBy(RepositoryPredicate predicate) {
        return null;
    }
}
