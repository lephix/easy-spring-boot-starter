package com.lephix.easy.mvc;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface AbstractEasyRepository<T> extends PagingAndSortingRepository<T, Long>, JpaSpecificationExecutor<T> {
}
