package com.team5.pyeonjip.category.repository;

import com.team5.pyeonjip.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentIdIsNull();

    List<Category> findByParentId(Long parentId);
}
