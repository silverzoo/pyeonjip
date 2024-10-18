package com.team5.pyeonjip.category.repository;

import com.team5.pyeonjip.category.entity.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @EntityGraph(attributePaths = {"children"})
    @Query(value = "SELECT c FROM Category c ORDER BY c.sort")
    List<Category> findAll();

    @EntityGraph(attributePaths = {"children"})
    Optional<Category> findById(Long id);

    @EntityGraph(attributePaths = {"children"})
//    @Query(value = "SELECT c FROM Category c LEFT JOIN FETCH WHERE c.parentId IN :ids")
    List<Category> findAllById(Iterable<Long> ids);

    @EntityGraph(attributePaths = {"children"})
    List<Category> findByParentId(Long parentId);

    Boolean existsByName(String name);

    // 네이티브 쿼리 적용
    // Leaf (최하위 카테고리) 만을 찾아야 함) -> 나무의 나뭇잎 생각하면 편함
    @Query(value = """
    WITH RECURSIVE CategoryTree AS (
        -- 시작 카테고리를 선택 (주어진 ID)
        SELECT id, name, parent_id, sort
        FROM category
        WHERE id = :parentId

        -- SELECT 쿼리 결과 결합
        UNION ALL

        -- 재귀적으로 자식 카테고리를 찾기
        -- LEFT JOIN 으로 Leaf 필터링
        SELECT c.id, c.name, c.parent_id, c.sort
        FROM category c
        -- CategoryTree : 중간 결과를 저장하기 위한 임시 가상 테이블
        JOIN CategoryTree ct ON c.parent_id = ct.id
    )
    -- 최종 SELECT : Leaf 카테고리만 필터링
    SELECT ct.id
    FROM CategoryTree ct
        -- LEFT JOIN 사용하여 Category 테이블과 다시 조인
        -- 대신 LEAF 카테고리만 선택 
    LEFT JOIN category c ON ct.id = c.parent_id
    WHERE c.id IS NULL
    ORDER BY ct.sort ASC
    """, nativeQuery = true)
    List<Long> findLeafCategories(@Param("parentId") Long parentId);

    @Modifying
    @Query("DELETE FROM Category c WHERE c IN :categories")
    void deleteAll(Iterable<? extends Category> categories);
}

