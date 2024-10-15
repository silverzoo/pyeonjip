package com.team5.pyeonjip.category.repository;

import com.team5.pyeonjip.category.entity.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    List<Category> findAllById(Iterable<Long> ids);

    @EntityGraph(attributePaths = {"children"})
    List<Category> findByParentId(Long parentId);

    // 주석을 달아놨으니 공부해보셔도 좋을거 같아요
    @Query(value = """
        WITH RECURSIVE CategoryTree AS (
            -- 1. Anchor (시작조건 : parentId로 탐색을 시작)
            SELECT id, name, parent_id
            FROM Category
            WHERE id = :parentId

            UNION ALL
            -- 2. Recursive (재귀단계 : 자식을 찾기 위해 이전 단게의 결과와 Join)
            SELECT c.id, c.name, c.parent_id
            FROM Category c
            -- CategoryTree : 쿼리의 중간결과를 저장하는 가상 테이블
            -- 부모 id = 자식의 Parent_id와 일치할 때 선택
            JOIN CategoryTree ct ON c.parent_id = ct.id
        )
        -- Leaf : 최하위 카테고리 (나뭇잎 처럼 Tree 끝에 있음)
        -- LEFT JOIN을 사용하여 id가 다른 카테고리의 parent로 존재하는지 확인
        SELECT ct.* 
        FROM CategoryTree ct
        LEFT JOIN Category c ON ct.id = c.parent_id
        WHERE c.parent_id IS NULL
        """, nativeQuery = true)
    List<Category> findLeafCategoriesByParentId(@Param("parentId") Long parentId);
}

