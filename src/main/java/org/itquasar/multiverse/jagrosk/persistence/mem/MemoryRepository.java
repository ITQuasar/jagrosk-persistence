package org.itquasar.multiverse.jagrosk.persistence.mem;

import org.itquasar.multiverse.jagrosk.persistence.JagroskEntity;
import org.itquasar.multiverse.jagrosk.persistence.Repository;

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
public class MemoryRepository<I, E extends JagroskEntity<I>> implements Repository<I, E> {

    private final Map<I, E> storage = new ConcurrentHashMap<I, E>();

    private final Class<E> entityClass;

    public MemoryRepository(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public Optional<E> add(E entity) {
        return Optional.ofNullable(
                this.storage.put(entity.getId(), entity)
        );
    }

    @Override
    public Optional<E> remove(E entity) {
        Objects.requireNonNull(entity, "Entity must be not null to be removed from persistence.");
        I id = entity.getId();
        return Optional.ofNullable(
                this.storage.remove(this.storage.get(id))
        );
    }

    @Override
    public Optional<E> update(E entity) {
        return Optional.ofNullable(
                this.storage.replace(entity.getId(), entity)
        );
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
    public void close() {
        this.storage.clear();
    }

    @Override
    public Map<I, E> unwrap() {
        return storage;
    }
}
