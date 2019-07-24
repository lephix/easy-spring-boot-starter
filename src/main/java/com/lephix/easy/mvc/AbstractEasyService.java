package com.lephix.easy.mvc;

import com.lephix.easy.utils.JPASpecUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public abstract class AbstractEasyService<T> {

    public T save(T entity) {
        return getRepository().save(entity);
    }

    public Iterable<T> saveAll(Iterable<T> entities) {
        return getRepository().saveAll(entities);
    }

    public void deleteById(long id) {
        getRepository().deleteById(id);
    }

    public T update(T entity) {
        return getRepository().save(entity);
    }

    public T findOne(long id) {
        return getRepository().findById(id).orElse(null);
    }

    public T findOne(List<Specification<T>> specs) {
        return getRepository().findOne(JPASpecUtils.andAll(specs)).orElse(null);
    }

    public Page<T> findAll(List<Specification<T>> specs, Pageable pageable) {
        return getRepository().findAll(JPASpecUtils.andAll(specs), pageable);
    }

    public abstract AbstractEasyRepository<T> getRepository();

}
