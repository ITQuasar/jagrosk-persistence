package org.itquasar.multiverse.jagrosk.persistence.jpa;

import org.itquasar.multiverse.jagrosk.persistence.Entity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by guilherme on 29/01/17.
 */
public class JPAQuery<I, E extends Entity<I>> {


    public static <I, E extends Entity<I>> TypedQuery<E> listAllQuery(EntityManager entityManager, Class<E> entityClass){
        return new JPAQuery<>(entityManager, entityClass).toTypedQuery();
    }

    private final EntityManager entityManager;
    private final Class<E> entityClass;
    private final CriteriaBuilder criteriaBuilder;
    private final CriteriaQuery<E> criteriaQuery;
    private final Root<E> root;

    public JPAQuery(EntityManager entityManager, Class<E> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
        this.criteriaQuery = criteriaBuilder.createQuery(entityClass);
        this.root = criteriaQuery.from(entityClass);
    }

    public CriteriaBuilder builder() {
        return this.criteriaBuilder;
    }

    public CriteriaQuery criteria() {
        return criteriaQuery;
    }

    public Root<E> root(){
        return root;
    }

    public TypedQuery<E> toTypedQuery(){
        return this.entityManager.createQuery(this.criteria());
    }
}
