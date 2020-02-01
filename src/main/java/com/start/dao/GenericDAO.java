package com.start.dao;

import javax.persistence.LockModeType;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenericDAO<T extends Serializable, ID extends Serializable> {
    Optional<T> findById(ID id);

    Optional<T> findById(ID id, LockModeType lockModeType);

    Optional<T> findReferenceById(ID id);

    Set<T> findAll();

    List<T> findsAll();

    T save(T instance);

    void delete(T instance);
}
