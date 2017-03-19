package org.itquasar.multiverse.jagrosk.persistence.jpa;

import org.itquasar.multiverse.jagrosk.persistence.JagroskEntity;
import org.itquasar.multiverse.jagrosk.persistence.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by guilherme on 29/01/17.
 */
public class JPARepository<I, E extends JagroskEntity<I>> implements Repository<I, E> {

    private final EntityManager entityManager;
    private final Class<E> entityClass;


    public JPARepository(EntityManager entityManager, Class<E> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }

    private static <E> List<E> listOrEmpty(List<E> list) {
        return list != null ? list : Collections.emptyList();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public JPAQuery<I, E> createJPAQuery() {
        return new JPAQuery<>(this.entityManager, this.entityClass);
    }

    @Override
    public Optional<E> add(E entity) {
        this.entityManager.persist(entity);
        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<E> remove(E entity) {
        this.entityManager.remove(entity);
        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<E> remove(I id) {
        Optional<E> entity = this.findById(id);
        if (entity.isPresent()) {
            this.entityManager.remove(entity.get());
        }
        return entity;
    }

    @Override
    public Optional<E> update(E entity) {
        return Optional.ofNullable(
                this.entityManager.merge(entity)
        );
    }

    @Override
    public List<E> listAll() {
        return listOrEmpty(JPAQuery.listAllQuery(entityManager, entityClass).getResultList());
    }

    @Override
    public Optional<E> findById(I id) {
        return Optional.ofNullable(
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
    public List<E> findByIn(String propertyName, Collection<?> values) {
        JPAQuery jpaQuery = createJPAQuery();
        return this.findByIn(jpaQuery, jpaQuery.root().get(propertyName), values);
    }


    public List<E> findByIn(CollectionAttribute collectionAttribute, Collection<?> values) {
        JPAQuery jpaQuery = createJPAQuery();
        return this.findByIn(jpaQuery, jpaQuery.root().join(collectionAttribute), values);
    }

    public List<E> findByIn(SetAttribute setAttribute, Collection<?> values) {
        JPAQuery jpaQuery = createJPAQuery();
        return this.findByIn(jpaQuery, jpaQuery.root().join(setAttribute), values);
    }

    public List<E> findByIn(ListAttribute listAttribute, Collection<?> values) {
        JPAQuery jpaQuery = createJPAQuery();
        return this.findByIn(jpaQuery, jpaQuery.root().join(listAttribute), values);
    }

    public List<E> findByIn(MapAttribute mapAttribute, Collection<?> values) {
        JPAQuery jpaQuery = createJPAQuery();
        return this.findByIn(jpaQuery, jpaQuery.root().join(mapAttribute), values);
    }

    private List<E> findByIn(JPAQuery jpaQuery, Expression expression, Collection<?> values) {
        jpaQuery.criteria().where(
                expression.in(values)
        );
        return listOrEmpty(jpaQuery.toTypedQuery().getResultList());
    }

    @Override
    public void close() {
        this.entityManager.close();
    }

    @Override
    public EntityManager unwrap() {
        return this.getEntityManager();
    }
}
