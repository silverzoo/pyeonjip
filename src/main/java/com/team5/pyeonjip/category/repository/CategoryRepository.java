package com.team5.pyeonjip.category.repository;

import com.team5.pyeonjip.category.entity.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @EntityGraph(attributePaths = {"children"})
    List<Category> findAll();

    @EntityGraph(attributePaths = {"children"})
    Optional<Category> findById(Long id);

    @EntityGraph(attributePaths = {"children"})
    List<Category> findByParentId(Long parentId);

    @EntityGraph(attributePaths = {"children"})
    List<Category> findByParentIdIsNull();
}
