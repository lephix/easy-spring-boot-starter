package com.lephix.easy.utils;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.criteria.Expression;
import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class JPASpecUtils {

    public enum OP {
        EQ, NEQ, GTE, GT, LTE, LT, LIKE, IS_NULL, IS_NOT_NULL
    }

    @SuppressWarnings("unchecked")
    public static <T> Specification<T> of(T t, String fieldName, OP op, Object value) {
        Entity entity = AnnotationUtils.findAnnotation(t.getClass(), Entity.class);
        checkNotNull(entity, "Only support for the class with @Entity annotation.");

        if (value == null && op != OP.IS_NULL && op != OP.IS_NOT_NULL) {
            return null;
        }

        return (root, criteriaQuery, cb) -> {
            try {
                Field field = t.getClass().getDeclaredField(fieldName);
                Expression<? extends Comparable> expr =
                        (Expression<? extends Comparable>) root.get(fieldName).as(field.getType());
                switch (op) {
                    case EQ:
                        return cb.equal(expr, value);
                    case NEQ:
                        return cb.notEqual(expr, value);
                    case GTE:
                        return cb.greaterThanOrEqualTo(expr, (Comparable) value);
                    case GT:
                        return cb.greaterThan(expr, (Comparable) value);
                    case LTE:
                        return cb.lessThanOrEqualTo(expr, (Comparable) value);
                    case LT:
                        return cb.lessThan(expr, (Comparable) value);
                    case LIKE:
                        return cb.like((Expression<String>) expr, "%" + value + "%");
                    case IS_NULL:
                        return cb.isNull(expr);
                    case IS_NOT_NULL:
                        return cb.isNotNull(expr);
                    default:
                        throw new UnsupportedOperationException();
                }
            } catch (Exception e) {
                throw new RuntimeException("Building JPA specification error.", e);
            }
        };
    }

    public static <T> Specification<T> andAll(List<Specification<T>> specs) {
        if (CollectionUtils.isEmpty(specs)) {
            return null;
        }
        Specification<T> spec = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            spec.and(specs.get(i));
        }
        return spec;
    }

    public static <T> Specification<T> orAll(List<Specification<T>> specs) {
        if (CollectionUtils.isEmpty(specs)) {
            return null;
        }
        Specification<T> spec = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            spec.or(specs.get(i));
        }
        return spec;
    }

}
