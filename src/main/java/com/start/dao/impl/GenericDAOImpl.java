package com.start.dao.impl;

import com.start.dao.GenericDAO;
import lombok.val;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class GenericDAOImpl<T extends Serializable, ID extends Serializable> implements GenericDAO<T, ID> {

    @PersistenceContext(unitName = "DataBase")
    protected EntityManager em;

    protected Class<T> entityClass;

    protected GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public Optional<T> findById(ID id) {
        return findById(id, LockModeType.NONE);
    }

    @Override
    public Optional<T> findById(ID id, LockModeType lockModeType) {
        return Optional.ofNullable(
                em.find(entityClass, id, lockModeType)
        );
    }

    @Override
    public Optional<T> findReferenceById(ID id) {
        return Optional.ofNullable(
                em.getReference(entityClass, id)
        );
    }

    @Override
    public Set<T> findAll() {
        val query = em.getCriteriaBuilder().createQuery(entityClass);
        query.select(query.from(entityClass));
        return em.createQuery(query).getResultStream().collect(Collectors.toSet());
    }

    @Override
    public List<T> findsAll() {
        val query = em.getCriteriaBuilder().createQuery(entityClass);
        query.select(query.from(entityClass));
        return em.createQuery(query).getResultList();
    }

    @Override
    public T save(T instance) {
        return em.merge(instance);
    }

    @Override
    public void delete(T instance) {
        if (!em.contains(instance)) {
            instance = em.merge(instance);
        }

        em.remove(instance);
    }
}
