package org.itquasar.multiverse.jagrosk.persistence.jpa;

import org.itquasar.multiverse.jagrosk.persistence.Entity;
import org.itquasar.multiverse.jagrosk.persistence.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by guilherme on 29/01/17.
 */
public class JPARepository<I, E extends Entity<I>> implements Repository<I, E> {

    private final EntityManager entityManager;
    private final Class<E> entityClass;


    private static <E> List<E> listOrEmpty(List<E> list) {
        return list != null ? list : Collections.emptyList();
    }


    public JPARepository(EntityManager entityManager, Class<E> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    private JPAQuery<I, E> createJPAQuery() {
        return new JPAQuery<>(this.entityManager, this.entityClass);
    }

    @Override
    public Optional<E> add(E entity) {
        this.entityManager.persist(entity);
        return Optional.of(entity);
    }

    @Override
    public Optional<E> remove(E entity) {
        this.entityManager.remove(entity);
        return Optional.of(entity);
    }

    @Override
    public Optional<E> update(E entity) {
        this.entityManager.merge(entity);
        this.entityManager.persist(entity);
        return Optional.of(entity);
    }

    @Override
    public List<E> listAll() {
        return listOrEmpty(JPAQuery.listAllQuery(entityManager, entityClass).getResultList());
    }

    @Override
    public Optional<E> findById(I id) {
        return Optional.of(
                this.entityManager.find(this.entityClass, id)
        );
    }

    @Override
    public List<E> findBy(String propertyName, Object value) {
        JPAQuery jpaQuery = createJPAQuery();
        return this.findBy(jpaQuery, jpaQuery.root().get(propertyName), value);
    }

    public List<E> findBy(SingularAttribute singularAttribute, Object value) {
        JPAQuery jpaQuery = createJPAQuery();
        return this.findBy(jpaQuery, jpaQuery.root().get(singularAttribute), value);
    }

    private List<E> findBy(JPAQuery jpaQuery, Path path, Object value) {
        jpaQuery.criteria().where(
                jpaQuery.builder().equal(
                        path, value
                )
        );
        return listOrEmpty(jpaQuery.toTypedQuery().getResultList());
    }

    @Override
    public void close() {
        this.entityManager.close();
    }
}
